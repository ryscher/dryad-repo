/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.datadryad.api;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.dspace.content.Item;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.storage.rdbms.DatabaseManager;
import org.dspace.storage.rdbms.TableRow;
import org.dspace.storage.rdbms.TableRowIterator;
import java.net.MalformedURLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


/**
 * Convenience class for querying Solr and Postgres for data related to a given 
 * journal, as used by the journal landing pages reporting.
 * 
 * @author Nathan Day <nday@datadryad.org>
 */
public class DryadJournal {

    private static Logger log = Logger.getLogger(DryadJournal.class);
    private static final String solrStatsUrl = ConfigurationManager.getProperty("solr.stats.server");

    private static final String archivedDataFilesQuery    = "SELECT * FROM ArchivedPackageDataFileItemIdsByJournal(?)";
    private static final String archivedDataFilesQueryCol =               "archivedpackagedatafileitemidsbyjournal";

    private static final String archivedDataPackageIds    = "SELECT * FROM ArchivedPackageItemIdsByJournal(?,?);";
    private static final String archivedDataPackageIdsCol =               "archivedpackageitemidsbyjournal";

    private static final String archivedPackageCount    = "SELECT * FROM ArchivedPackageCountByJournal(?)";
    private static final String archivedPackageCountCol =               "archivedpackagecountbyjournal";


    private Context context;
    private String journalName;

    public DryadJournal(Context context, String journalName) throws IllegalArgumentException {
        if (context == null) {
            throw new IllegalArgumentException("Illegal null Context.");
        } else if (journalName == null) {
            throw new IllegalArgumentException("Illegal null journal name.");
        }
        this.context = context;
        this.journalName = journalName;
    }

    /**
     * Executes query to Postgres to get archived data file item ids for a given 
     * journal, returning the item ids.
     * @return a List of {@link Integer} values representing item.item_id values
     * @throws SQLException
     */
    public List<Integer> getArchivedDataFiles() throws SQLException {
        TableRowIterator tri = DatabaseManager.query(this.context, archivedDataFilesQuery, journalName);
        List<Integer> dataFiles = new ArrayList<Integer>();
        while(tri.hasNext()) {
            TableRow row = tri.next();
            int itemId = row.getIntColumn(archivedDataFilesQueryCol);
            dataFiles.add(itemId);
        }
        return dataFiles;
    }

    /**
     * Return count of archived data packages for the journal associated with this object.
     * @return int count
     */
    public int getArchivedPackagesCount() {
        int count = 0;
        try {
            PreparedStatement statement = context.getDBConnection().prepareStatement(archivedPackageCount);
            statement.setString(1,journalName);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                count = rs.getInt(archivedPackageCountCol);
            }
        } catch (Exception ex) {
            log.error(ex);
        }
        return count;
    }

    /**
     * Return a sorted list of archived data packages (Item objects) for the journal 
     * associated with this object. The data packages are sorted according to 
     * date-accessioned, with most recently accessioned package first.
     * @param max total number of items to return
     * @return List<org.dspace.content.Item> data packages
     * @throws SQLException 
     */
    public List<Item> getArchivedPackagesSortedRecent(int max) throws SQLException {
        TableRowIterator tri = DatabaseManager.query(this.context, archivedDataPackageIds, journalName, max);
        List<Item> dataPackages = new ArrayList<Item>();
        while (tri.hasNext() && dataPackages.size() < max) {
            TableRow row = tri.next();
            int itemId = row.getIntColumn(archivedDataPackageIdsCol);
            try {
                Item dso = Item.find(context, itemId);
                dataPackages.add(dso);
            } catch (SQLException ex) {
                log.error("Error making DSO from " + itemId + ": " + ex.getMessage());
            }
        }
        return dataPackages;
    }

    /**
     * Return sorted listing of data file Items for the journal associated with
     * this object, faceted by a given field, for example, page views or data file 
     * downloads.
     * @param facetQueryField value for "&facet.query=..." parameter, e.g., "owningItem"
     * @param time query value to provide in Solr "q=time:[...]" field
     * @param max maximum number of items to return
     * @return LinkedHashMap<Item, String> of data package Items and counts. This 
     *      HashMap structure's iterator returns items in the order in which they were
     *      inserted, preserving the sort order performed in sortFilterQuery().
     */
    public LinkedHashMap<Item, String> getRequestsPerJournal(String facetQueryField, String time, int max) {
        // default solr query for site
        SolrQuery queryArgs = new SolrQuery();
        queryArgs.setQuery(time);
        queryArgs.setRows(0);
        queryArgs.set("omitHeader", "true");
        queryArgs.setFacet(true);
        Iterator<Integer> itDataFileIds = null;
        try {
            itDataFileIds = getArchivedDataFiles().iterator();
        } catch (SQLException ex) {
            log.error(ex);
            return new LinkedHashMap<Item, String>();
        }
        while(itDataFileIds.hasNext()) {
            queryArgs.addFacetQuery(facetQueryField + ":" + itDataFileIds.next());
        }
        try {
            return sortFilterQuery(doSolrPost(solrStatsUrl, queryArgs), facetQueryField, max);
        } catch (Exception ex) {
            log.error(ex);
            return new LinkedHashMap<Item, String>();
        }
    }

    /**
     * Given a solrResponse produced by a faceted query, return a list of items 
     * sorted by facet query value. Note that this method assumes
     * that the query producing the response was faceted (&facet=true) and that 
     * it had one or more query facets (&facet.query=...) that correspond to
     * Dryad Items, as is produced by getRequestsPerJournal above.
     * @param solrResponse
     * @param facetQueryField facet.query field to use as sort key, e.g., "owningItem"
     * @param max maximum number of items to return
     * @return LinkedHashMap<Item, String> of 
     * @throws SQLException 
     */
    private LinkedHashMap<Item, String> sortFilterQuery(QueryResponse solrResponse, String facetQueryField, int max) throws SQLException {
        final Map<String,Integer> facets = solrResponse.getFacetQuery();
        ArrayList<String> sortedKeys = new ArrayList<String>();
        sortedKeys.addAll(facets.keySet());
        Collections.sort(sortedKeys, new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                return facets.get(b).compareTo(facets.get(a));
            }
        });
        LinkedHashMap<Item, String> result = new LinkedHashMap<Item, String>();
        int pfxLen = (facetQueryField + ":").length();
        int keyMax = sortedKeys.size();
        for (int i = 0; i < keyMax && i < max; ++i) {
            String itemStr = sortedKeys.get(i);
            int itemId = Integer.valueOf(itemStr.substring(pfxLen));
            Item item = Item.find(this.context, itemId);
            result.put(item, Integer.toString(facets.get(itemStr)));
        }
        return result;
    }
    
    /**
     * Query Solr server using an HTTP POST request method to avoid the query
     * URL length limitations for a GET.
     * @param baseUrl url of solr server, e.g., http://datadryad.org/solr
     * @param solrQuery
     * @return QueryResponse
     * @throws MalformedURLException
     * @throws SolrServerException 
     */
    private QueryResponse doSolrPost(String baseUrl, SolrQuery solrQuery) throws MalformedURLException, SolrServerException {
        CommonsHttpSolrServer server = null;
        try {
            server = new CommonsHttpSolrServer(baseUrl);
        } catch (MalformedURLException ex) {
            log.error(ex);
            throw(ex);
        }
        QueryResponse response;
        try {
            response = server.query(solrQuery, SolrRequest.METHOD.POST);
        } catch (SolrServerException ex) {
            log.error(ex);
            throw(ex);
        }
        return response;
    }
}
