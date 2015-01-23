/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dspace.app.xmlui.aspect.journal.landing;

import org.apache.log4j.Logger;

import org.dspace.app.xmlui.wing.Message;
import org.dspace.app.xmlui.wing.element.Body;

import java.sql.SQLException;
import org.dspace.app.xmlui.utils.UIException;
import org.dspace.app.xmlui.wing.WingException;
import org.dspace.authorize.AuthorizeException;

import org.xml.sax.SAXException;
import java.io.IOException;
import java.text.DateFormatSymbols;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import org.apache.avalon.framework.parameters.ParameterException;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.dspace.app.xmlui.aspect.discovery.AbstractFiltersTransformer;
import static org.dspace.app.xmlui.aspect.journal.landing.Const.*;
import org.dspace.app.xmlui.utils.ContextUtil;
import static org.dspace.app.xmlui.wing.AbstractWingTransformer.*;
import org.dspace.app.xmlui.wing.element.Division;
import org.dspace.app.xmlui.wing.element.List;
import org.dspace.app.xmlui.wing.element.ReferenceSet;
import org.dspace.content.DSpaceObject;
import org.dspace.content.Item;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Constants;
import org.dspace.core.Context;

import org.dspace.discovery.SearchUtils;
import org.dspace.discovery.SearchService;
import org.dspace.discovery.SearchServiceException;
import org.dspace.workflow.DryadWorkflowUtils;

/**
 *
 * @author Nathan Day
 */
public class TopTenDownloads extends AbstractFiltersTransformer {
    
    private static final Logger log = Logger.getLogger(TopTenDownloads.class);
    private static final Message T_head = message("xmlui.JournalLandingPage.TopTenDownloads.panel_head");
    private static final Message T_val_head = message("xmlui.JournalLandingPage.TopTenDownloads.val_head");
    
    private ArrayList<DSpaceObject> references;
    private ArrayList<String> downloads;
    private String journalName;
        
    @Override
    public void addBody(Body body) throws SAXException, WingException,
            UIException, SQLException, IOException, AuthorizeException
    {
        try {
            journalName = parameters.getParameter(PARAM_JOURNAL_NAME);
        } catch (ParameterException ex) {
            log.error((ex));
        }
        if (journalName == null || journalName.length() == 0) return;

        Division topTen = body.addDivision(TOPTEN_DOWNLOADS,TOPTEN_DOWNLOADS);
        topTen.setHead(T_head);
        
        Calendar now = Calendar.getInstance();
        String month = new DateFormatSymbols().getMonths()[now.get(Calendar.MONTH)];
        String year = Integer.toString(now.get(Calendar.YEAR));

        Division items = topTen.addDivision(ITEMS);
        ReferenceSet refs = items.addReferenceSet(TOPTEN_DOWNLOADS_REFS, "summaryList");
        refs.setHead(month + " " + year);

        Division count = topTen.addDivision(VALS);
        List list = count.addList("most-viewed-count", List.TYPE_SIMPLE, "most-viewed-count");
        list.setHead(T_val_head);

        references = new ArrayList<DSpaceObject>();
        downloads = new ArrayList<String>();
        try {
            performSearch(null);
        } catch (SearchServiceException e) {
            log.error(e.getMessage(), e);
        }
        for (DSpaceObject ref : references)
            refs.addReference(ref);
        for (String s : downloads)
            list.addItem(s);
        references = null;
        downloads = null;
    }

    /**
     * 
     *
     * @param object
     */
    @Override
    public void performSearch(DSpaceObject object) throws SearchServiceException, UIException {
        if (queryResults != null) return;
        queryArgs = prepareDefaultFilters(getView());
        queryArgs.setQuery("search.resourcetype:" + Constants.BITSTREAM + " AND prism.publicationName:" + journalName);
        queryArgs.setRows(10);
        String sortField = SearchUtils.getConfig().getString("total.download.sort-option");
        if(sortField != null){
            queryArgs.setSortField(
                    sortField,
                    SolrQuery.ORDER.desc
            );
        }
        SearchService service = getSearchService();
        Context c = null;
        try {
            c = ContextUtil.obtainContext(objectModel);
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
        queryResults = (QueryResponse) service.search(c, queryArgs);
        boolean includeRestrictedItems = ConfigurationManager.getBooleanProperty("harvest.includerestricted.rss", false);
        int numberOfItemsToShow= SearchUtils.getConfig().getInt("solr.recent-submissions.size", 5);
        ExtendedProperties config = SearchUtils.getConfig();
        if (queryResults != null && !includeRestrictedItems)  {
            for (Iterator<SolrDocument> it = queryResults.getResults().iterator(); it.hasNext() && references.size() < numberOfItemsToShow;) {
                SolrDocument doc = it.next();
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
                        downloads.add(doc.getFieldValue(config.getString("total.download.sort-option")).toString());
                    }
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
                }
            }
        }
    }

    @Override
    public String getView() {
        return "site";
    }
}
