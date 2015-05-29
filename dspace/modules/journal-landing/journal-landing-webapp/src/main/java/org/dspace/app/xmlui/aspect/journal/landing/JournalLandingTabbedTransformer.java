<<<<<<< HEAD
/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
=======
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
>>>>>>> 1f11d49ccd30292c63576a7b9b2e536c7699a90a
 */
package org.dspace.app.xmlui.aspect.journal.landing;

import java.io.IOException;
<<<<<<< HEAD
import java.sql.SQLException;
import java.text.SimpleDateFormat;
=======
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
>>>>>>> 1f11d49ccd30292c63576a7b9b2e536c7699a90a
import java.util.Map;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
<<<<<<< HEAD
import org.apache.log4j.Logger;
=======
import org.apache.commons.collections.ExtendedProperties;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.dspace.app.xmlui.aspect.discovery.AbstractFiltersTransformer;
>>>>>>> 1f11d49ccd30292c63576a7b9b2e536c7699a90a
import static org.dspace.app.xmlui.aspect.journal.landing.Const.*;
import org.dspace.app.xmlui.utils.UIException;
import org.dspace.app.xmlui.wing.Message;
import org.dspace.app.xmlui.wing.WingException;
import org.dspace.app.xmlui.wing.element.Body;
import org.dspace.app.xmlui.wing.element.Division;
import org.dspace.app.xmlui.wing.element.List;
import org.dspace.app.xmlui.wing.element.ReferenceSet;
import org.dspace.authorize.AuthorizeException;
<<<<<<< HEAD
import org.xml.sax.SAXException;
import java.util.LinkedHashMap;
import org.datadryad.api.DryadJournal;
import org.dspace.app.xmlui.cocoon.AbstractDSpaceTransformer;
import org.dspace.content.DCValue;
import org.dspace.content.Item;
import org.dspace.workflow.DryadWorkflowUtils;

/**
 * Cocoon/DSpace transformer to produce a panel for the journal landing page,
 * with multiple tabs. The DRI produced here is handled by the Mirage xsl
 * stylesheet lib/xsl/aspect/JournalLandingPage/main.xsl.
 * 
 * @author Nathan Day
 */
public abstract class JournalLandingTabbedTransformer extends AbstractDSpaceTransformer {

    private static final Logger log = Logger.getLogger(JournalLandingTabbedTransformer.class);
    private final static SimpleDateFormat fmt = new SimpleDateFormat(fmtDateView);

    private String journalName;
    private DryadJournal dryadJournal;

    protected class DivData {
        public String n;
        public Message T_div_head;
        public int maxResults;
=======
import org.dspace.content.DSpaceObject;
import org.dspace.content.Item;
import org.dspace.core.ConfigurationManager;
import org.dspace.discovery.SearchService;
import org.dspace.discovery.SearchServiceException;
import org.dspace.discovery.SearchUtils;
import org.dspace.workflow.DryadWorkflowUtils;
import org.xml.sax.SAXException;

/**
 *
 * @author Nathan Day
 */
public class JournalLandingTabbedTransformer extends AbstractFiltersTransformer {
    
    private static final Logger log = Logger.getLogger(TopTenDownloads.class);
   
    // cocoon parameters
    protected String journalName;
        
    // performSearch() values
    private ArrayList<DSpaceObject> references;
    private ArrayList<String> values;
    private String q;
    private String sortOption;
    private String sortFieldOption;
    private TabData currentTabData;
    
    // config values (final)
    private final String submissionSize = "solr.recent-submissions.size";
    private final int submissionSizeDefault = 5;
    private final String includeRestricted = "harvest.includerestricted.rss";
    private final boolean includeRestrictedDefault = false;
    
    // container for data pertaining to entire div
    protected class DivData {
        public String sortOption;
        public String sortFieldOption;
        public String n;
        public Message T_div_head;
>>>>>>> 1f11d49ccd30292c63576a7b9b2e536c7699a90a
    }
    protected DivData divData;
    protected class TabData {
        public String n;
        public Message buttonLabel;
<<<<<<< HEAD
        public Message refHead;
        public Message valHead;
        public String dateFilter;
        public String facetQueryField;
        public QueryType queryType;
    }
    protected java.util.List<TabData> tabData;

=======
        public String query;
        public Message refHead;
        public Message valHead;
    }
    protected ArrayList<TabData> tabData;
    
>>>>>>> 1f11d49ccd30292c63576a7b9b2e536c7699a90a
    @Override
    public void setup(SourceResolver resolver, Map objectModel, String src,
            Parameters parameters) throws ProcessingException, SAXException,
            IOException
    {
        super.setup(resolver, objectModel, src, parameters);
        try {
            journalName = parameters.getParameter(PARAM_JOURNAL_NAME);
        } catch (ParameterException ex) {
            log.error(ex);
            throw new ProcessingException(ex.getMessage());
        }
<<<<<<< HEAD
        dryadJournal = new DryadJournal(this.context, this.journalName);
    }

    /**
     * Method to add a div element with multiple tabs, each containing a listing
     * of Dryad references and an associated value, e.g., an accessioned date
     * or a download count.
     * @param body DRI body element
     * @throws SAXException
     * @throws WingException
     * @throws UIException
     * @throws SQLException
     * @throws IOException
     * @throws AuthorizeException 
     */
    protected void addStatsLists(Body body) throws SAXException, WingException,
=======
    }
    
    @Override
    public void addBody(Body body) throws SAXException, WingException,
>>>>>>> 1f11d49ccd30292c63576a7b9b2e536c7699a90a
            UIException, SQLException, IOException, AuthorizeException
    {
        Division outer = body.addDivision(divData.n,divData.n);
        outer.setHead(divData.T_div_head);

        // tab buttons
        List tablist = outer.addList(TABLIST, List.TYPE_ORDERED, TABLIST);
        for(TabData t : tabData) {
            tablist.addItem(t.buttonLabel);
<<<<<<< HEAD
        }
        for(TabData t : tabData) {
            Division wrapper = outer.addDivision(t.n, t.n);
            // dspace referenceset or list to hold references or countries
            Division items = wrapper.addDivision(ITEMS);
            ReferenceSet itemsContainer = items.addReferenceSet(t.n, ReferenceSet.TYPE_SUMMARY_LIST);
            itemsContainer.setHead(t.refHead);
            // dspace value list, to hold counts
            Division vals = wrapper.addDivision(VALS);
            List valsList = vals.addList(t.n, List.TYPE_SIMPLE, t.n);
            valsList.setHead(t.valHead);
            if (t.queryType == QueryType.DOWNLOADS ) {
                doDownloadsQuery(itemsContainer, valsList, dryadJournal, divData, t);
            } else if (t.queryType == QueryType.DEPOSITS ) {
                doDepositsQuery(itemsContainer, valsList, dryadJournal, divData, t);
=======
        }        
        sortOption = divData.sortOption;
        sortFieldOption = divData.sortFieldOption;
        for(TabData t : tabData) {
            q = t.query;
            Division wrapper = outer.addDivision(t.n, t.n);
            Division items = wrapper.addDivision(ITEMS);
            // reference list
            ReferenceSet refs = items.addReferenceSet(t.n, ReferenceSet.TYPE_SUMMARY_LIST);
            refs.setHead(t.refHead);
            // dspace item value list
            Division count = wrapper.addDivision(VALS);
            List list = count.addList(t.n, List.TYPE_SIMPLE, t.n);
            list.setHead(t.valHead);

            references = new ArrayList<DSpaceObject>();
            values = new ArrayList<String>();
            try {
                performSearch(null);
            } catch (SearchServiceException e) {
                log.error(e.getMessage(), e);
            }
            if (references.size() == 0) {
                list.addItem(EMPTY_VAL);
            } else {
                for (DSpaceObject ref : references)
                    refs.addReference(ref);
                for (String s : values)
                    list.addItem().addContent(s);
>>>>>>> 1f11d49ccd30292c63576a7b9b2e536c7699a90a
            }
        }
    }

    /**
<<<<<<< HEAD
     * Populate the given reference and values lists with data from Solr on 
     * download statistics.
     * @param itemsContainer DRI ReferenceSet element to contain retrieved Items
     * @param countList DRI List element to contain download counts
     * @param dryadJournal DryadJournal object for the given 
     * @param divData query parameters for the current div
     * @param t query parameters for the current tab
     */
    private void doDownloadsQuery(ReferenceSet itemsContainer, List countList, DryadJournal dryadJournal, DivData divData, TabData t) {
        LinkedHashMap<Item, String> results = dryadJournal.getRequestsPerJournal(
            t.facetQueryField, t.dateFilter, divData.maxResults
        );
        if (results != null) {
            for (Item item : results.keySet()) {
                Item dataPackage = DryadWorkflowUtils.getDataPackage(context, item);
                try {
                    itemsContainer.addReference(dataPackage);
                    countList.addItem().addContent(results.get(item));
                } catch (WingException ex) {
                    log.error(ex);
                }
            }
        }
    }

    /**
     * Populate the given reference and values lists with recent deposit data
     * from Postgres.
     * @param itemsContainer DRI ReferenceSet element to contain retrieved Items
     * @param countList DRI List element to contain download counts
     * @param dryadJournal DryadJournal object for the given 
     * @param divData query parameters for the current div
     * @param t query parameters for the current tab
     */
    private void doDepositsQuery(ReferenceSet itemsContainer, List countList, DryadJournal dryadJournal, DivData divData, TabData t) {
        java.util.List<Item> packages = null;
        try {
            packages = dryadJournal.getArchivedPackagesSortedRecent(divData.maxResults);
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            return;
        }
        for (Item item : packages) {
            DCValue[] dateAccessioned = item.getMetadata(dcDateAccessioned);
            if (dateAccessioned.length >= 1) {
                String dateStr = dateAccessioned[0].value;
                //String date = fmt.format(dateStr);
                if (dateStr != null) {
                    try {
                        itemsContainer.addReference(item);
                        countList.addItem().addContent(fmt.format(fmt.parse(dateStr)));
                    } catch (Exception ex) {
                        log.error(ex);
                    }
=======
     * 
     *
     * @param object
     */
    @Override
    public void performSearch(DSpaceObject object) throws SearchServiceException, UIException {
        if (queryResults != null) return;        
        queryArgs = prepareDefaultFilters(getView());
        queryArgs.setQuery(q);
        queryArgs.setRows(10);
        String sortField = SearchUtils.getConfig().getString(sortFieldOption);
        if(sortField != null){
            queryArgs.setSortField(
                    sortField,
                    SolrQuery.ORDER.desc
            );
        }
        SearchService service = getSearchService();
log.debug(queryArgs);
        queryResults = (QueryResponse) service.search(context, queryArgs);
        boolean includeRestrictedItems = ConfigurationManager.getBooleanProperty(includeRestricted,includeRestrictedDefault);
        int numberOfItemsToShow= SearchUtils.getConfig().getInt(submissionSize, submissionSizeDefault);
        String config = SearchUtils.getConfig().getString(sortOption);
        if (queryResults != null && !includeRestrictedItems)  {
            for (SolrDocument doc : queryResults.getResults()) {
                if (references.size() > numberOfItemsToShow) break;
                DSpaceObject obj = null;
                try {
                    obj = SearchUtils.findDSpaceObject(context, doc);
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
                }
                try {
                    if (obj != null
                        && DryadWorkflowUtils.isAtLeastOneDataFileVisible(context, (Item) obj))
                    {
                        references.add(obj);
                        Object o = doc.getFieldValue(config);
                        
                        if (o instanceof ArrayList) {
                            values.add(((ArrayList) o).get(0).toString());
                        } else if (o instanceof String) {
                            values.add(o.toString());
                        }

                    }
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
>>>>>>> 1f11d49ccd30292c63576a7b9b2e536c7699a90a
                }
            }
        }
    }
<<<<<<< HEAD
=======

    @Override
    public String getView() {
        return "site";
    }
    
    @Override
    public Serializable getKey() {
        // do not allow this to be cached
        return null;
    }
    
>>>>>>> 1f11d49ccd30292c63576a7b9b2e536c7699a90a
}
