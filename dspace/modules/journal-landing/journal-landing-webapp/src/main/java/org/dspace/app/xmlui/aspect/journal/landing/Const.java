<<<<<<< HEAD
/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.xmlui.aspect.journal.landing;

import org.apache.solr.client.solrj.SolrQuery;

/**
 * @author Nathan Day
 */
public class Const {

    // parameters added to model during request validation action
    public static final String PARAM_JOURNAL_NAME = "journalName";
    public static final String PARAM_JOURNAL_ABBR = "journalAbbr";

    // banner
    public static final String BANNER_DIV_OUTER = "journal-landing-banner-outer";
    public static final String BANNER_DIV_INNER = "journal-landing-banner-inner";
    public static final String BANNER_MEM = "journal-landing-banner-mem";
    public static final String BANNER_SPO = "journal-landing-banner-spo";
    public static final String BANNER_DAT = "journal-landing-banner-dat";
    public static final String BANNER_AUT = "journal-landing-banner-aut";
    public static final String BANNER_MET = "journal-landing-banner-met";
    public static final String BANNER_INT = "journal-landing-banner-int";
    public static final String BANNER_PAC = "journal-landing-banner-pac";

    public static final String SEARCH_DIV = "journal-landing-search";

    // most recent deposits
    public static final String JOURNAL_STATS = "journal-landing-stats";
    public static final String JOURNAL_STATS_DEPS = "journal-landing-stats-deps";
    public static final String JOURNAL_STATS_MONTH = "journal-landing-stats-month";
    public static final String JOURNAL_STATS_YEAR = "journal-landing-stats-year";
    public static final String JOURNAL_STATS_ALLTIME = "journal-landing-stats-alltime";

    public static final String ITEMS = "items";
    public static final String VALS = "vals";
    public static final String TABLIST = "tablist";

    public static enum QueryType { DOWNLOADS, DEPOSITS };

    public static final String fmtDateView = "yyyy-MM-dd";
    public static final String solrDatePastMonth = "time:[NOW-1MONTH TO NOW]";
    public static final String solrDatePastYear = "time:[NOW-1YEAR TO NOW]";
    public static final String solrDateAllTime = "time:[* TO NOW]";
    public static final int displayCount = 10;
    public static final String depositsDisplayField = "dc.date.accessioned_dt";
    public static final String depositsDisplaySortField = "dc.date.accessioned_dt";
    public static final String dcDateAccessioned = "dc.date.accessioned";
    public static final SolrQuery.ORDER depositsDisplaySortOrder = SolrQuery.ORDER.desc;
    public static final String facetQueryId = "id";
    public static final String facetQueryOwningId = "owningItem";
    public static final String facetQueryCountryCode = "countryCode";
}
=======
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dspace.app.xmlui.aspect.journal.landing;

/**
 * String values in use in journal landing page aspect.
 * @author Nathan Day
 */
public class Const {
    
    // parameters added to model during request validation action
    public static final String PARAM_JOURNAL_NAME = "journalName";
    public static final String PARAM_JOURNAL_ABBR = "journalAbbr";
    
    // banner
    public static final String BANNER_DIV_OUTER = "journal-landing-banner-outer";
    public static final String BANNER_DIV_INNER = "journal-landing-banner-inner";
    public static final String BANNER_PUB = "journal-landing-banner-pub";
    public static final String BANNER_SOC = "journal-landing-banner-soc";
    public static final String BANNER_EDI = "journal-landing-banner-edi";

    // dryad info
    public static final String DRYAD_INFO_WRA = "journal-landing-dryadinfo-wrapper";
    public static final String DRYAD_INFO_INN = "journal-landing-dryadinfo-inner";
    public static final String DRYAD_INFO_INF = "journal-landing-dryadinfo-inf";
    public static final String DRYAD_INFO_MEM = "journal-landing-dryadinfo-mem";
    public static final String DRYAD_INFO_PAY = "journal-landing-dryadinfo-pay";
    public static final String DRYAD_INFO_DAT = "journal-landing-dryadinfo-dat";
    public static final String DRYAD_INFO_MET = "journal-landing-dryadinfo-met";
    
    public static final String SEARCH_DIV = "journal-landing-search";
    
    // most recent deposits
    public static final String MOST_RECENT_DEPOSITS_DIV = "journal-landing-recent";
    public static final String MOST_RECENT_DEPOSITS_REFS = "journal-landing-recent-refs";
    
    public static final String TOPTEN_DOWNLOADS = "journal-landing-topten-downloads";
    public static final String TOPTEN_DOWNLOADS_MONTH = "journal-landing-topten-downloads-month";
    public static final String TOPTEN_DOWNLOADS_YEAR = "journal-landing-topten-downloads-year";
    public static final String TOPTEN_DOWNLOADS_ALLTIME = "journal-landing-topten-downloads-alltime";

    public static final String TOPTEN_VIEWS = "journal-landing-topten-views";
    public static final String TOPTEN_VIEWS_MONTH = "journal-landing-topten-views-month";
    public static final String TOPTEN_VIEWS_YEAR = "journal-landing-topten-views-year";
    public static final String TOPTEN_VIEWS_ALLTIME = "journal-landing-topten-views-alltime";
    
    public static final String USER_GEO = "journal-landing-user-geo";
    public static final String USER_GEO_VIEWS = "journal-landing-user-geo-views";
    public static final String USER_GEO_DOWNLOADS = "journal-landing-user-geo-downloads";
    
    public static final String ITEMS = "items";
    public static final String VALS = "vals";
    public static final String TABLIST = "tablist";
    public static final String EMPTY_VAL = "-";
    
    // TODO: make this this-month
    public static final String dateMonth = "dc.date.available_dt:[NOW-30DAY/DAY TO NOW]";
    public static final String dateYear = "dc.date.available_dt:[NOW-1YEAR/DAY TO NOW]";
    
}
>>>>>>> 1f11d49ccd30292c63576a7b9b2e536c7699a90a
