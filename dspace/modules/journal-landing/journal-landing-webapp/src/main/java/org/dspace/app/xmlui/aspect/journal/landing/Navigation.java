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
=======
import java.net.URLEncoder;
>>>>>>> 1f11d49ccd30292c63576a7b9b2e536c7699a90a
import java.sql.SQLException;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.log4j.Logger;
import static org.dspace.app.xmlui.aspect.journal.landing.Const.*;
import org.dspace.app.xmlui.cocoon.AbstractDSpaceTransformer;
import org.dspace.app.xmlui.utils.UIException;
import org.dspace.app.xmlui.wing.WingException;
import org.dspace.app.xmlui.wing.element.Options;
import org.dspace.app.xmlui.wing.element.PageMeta;
import org.dspace.authorize.AuthorizeException;
import org.dspace.core.ConfigurationManager;
<<<<<<< HEAD
import org.xml.sax.SAXException;

/**
 * Add options and meta DRI elements to the journal landing page.
 * 
=======
import org.dspace.submit.utils.DryadJournalSubmissionUtils;
import org.xml.sax.SAXException;

/**
 *
>>>>>>> 1f11d49ccd30292c63576a7b9b2e536c7699a90a
 * @author Nathan Day
 */
   
public class Navigation extends AbstractDSpaceTransformer {
    
    private static final Logger log = Logger.getLogger(Navigation.class);
<<<<<<< HEAD

    @Override
=======
    private static final String ENCODING = "UTF-8";

>>>>>>> 1f11d49ccd30292c63576a7b9b2e536c7699a90a
    public void addOptions(Options options) throws SAXException, WingException,
            UIException, SQLException, IOException, AuthorizeException
    {
        options.addList("DryadSubmitData");
        options.addList("DryadSearch");
        options.addList("DryadConnect");
        options.addList("DryadMail");
    }

<<<<<<< HEAD
=======
    /**
     * Insure that the context path is added to the page meta.
     * @param pageMeta
     * @throws org.xml.sax.SAXException
     * @throws org.dspace.app.xmlui.wing.WingException
     * @throws org.dspace.app.xmlui.utils.UIException
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
>>>>>>> 1f11d49ccd30292c63576a7b9b2e536c7699a90a
    @Override
    public void addPageMeta(PageMeta pageMeta) throws SAXException,
            WingException, UIException, SQLException, IOException,
            AuthorizeException
    {
        // standard values
    	Request request = ObjectModelHelper.getRequest(objectModel);
        pageMeta.addMetadata("contextPath").addContent(contextPath);
        pageMeta.addMetadata("request","queryString").addContent(request.getQueryString());
        pageMeta.addMetadata("request","scheme").addContent(request.getScheme());
        pageMeta.addMetadata("request","serverPort").addContent(request.getServerPort());
        pageMeta.addMetadata("request","serverName").addContent(request.getServerName());
        pageMeta.addMetadata("request","URI").addContent(request.getSitemapURI());

        // Get realPort and nodeName
        String port = ConfigurationManager.getProperty("dspace.port");
        String nodeName = ConfigurationManager.getProperty("dryad.home");

        // If we're using Apache, we may not have the real port in serverPort
        if (port != null) {
            pageMeta.addMetadata("request", "realServerPort").addContent(port);
        }
        else {
            pageMeta.addMetadata("request", "realServerPort").addContent("80");
        }
        if (nodeName != null) {
            pageMeta.addMetadata("dryad", "node").addContent(nodeName);
        }
        // journal landing page values
        String journalName = null;
        String journalAbbr = null;
        try {
            journalName = parameters.getParameter(PARAM_JOURNAL_NAME);
            journalAbbr = parameters.getParameter(PARAM_JOURNAL_ABBR);
        } catch (ParameterException ex) {
            log.error(ex);
        }
        if (journalName != null && journalName.length() != 0) {
            pageMeta.addMetadata("request","journalName").addContent(journalName);
<<<<<<< HEAD
            pageMeta.addMetadata("request","journalAbbr").addContent(journalAbbr);
=======
            pageMeta.addMetadata("request","journalAbbr").addContent(journalAbbr);        
>>>>>>> 1f11d49ccd30292c63576a7b9b2e536c7699a90a
        }
    }
}