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

import org.apache.log4j.Logger;

import org.dspace.app.xmlui.cocoon.AbstractDSpaceTransformer;

import org.dspace.app.xmlui.wing.Message;
import org.dspace.app.xmlui.wing.element.Body;
import org.dspace.app.xmlui.wing.element.Division;
import static org.dspace.app.xmlui.aspect.journal.landing.Const.*;

import java.sql.SQLException;
import org.dspace.app.xmlui.utils.UIException;
import org.dspace.app.xmlui.wing.WingException;
import org.dspace.authorize.AuthorizeException;
import org.xml.sax.SAXException;
import java.io.IOException;
<<<<<<< HEAD
import java.util.Map;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;

/**
 * Add "Search data in Dryad associated with ..." by-journal search field
 * to the journal-landing page.
 * 
=======
import org.apache.avalon.framework.parameters.ParameterException;
import java.net.URLEncoder;

/**
 *
>>>>>>> 1f11d49ccd30292c63576a7b9b2e536c7699a90a
 * @author Nathan Day
 */
public class JournalSearch extends AbstractDSpaceTransformer {
    
    private static final Logger log = Logger.getLogger(JournalSearch.class);
    private static final Message T_panel_head = message("xmlui.JournalLandingPage.JournalSearch.panel_head"); 
    
<<<<<<< HEAD
    private String journalName;
    
    @Override
    public void setup(SourceResolver resolver, Map objectModel, String src,
            Parameters parameters) throws ProcessingException, SAXException,
            IOException
    {
        super.setup(resolver, objectModel, src, parameters);
        try {
            journalName = parameters.getParameter(PARAM_JOURNAL_NAME);
        } catch (Exception ex) {
            log.error(ex);
            throw(new ProcessingException("Bad access of journal name"));
        }
    }
    
    @Override
    public void addBody(Body body) throws SAXException, WingException,
            UIException, SQLException, IOException, AuthorizeException
    {
        Division searchDiv = body.addDivision(SEARCH_DIV, SEARCH_DIV);
        searchDiv.setHead(T_panel_head.parameterize(journalName));
    }
}
=======
    @Override
    public void addBody(Body body) throws SAXException, WingException,
            UIException, SQLException, IOException, AuthorizeException
    {
        String journalName = null;
        try {
            journalName = parameters.getParameter(PARAM_JOURNAL_NAME);
        } catch (ParameterException ex) {
            log.error((ex));
        }
        if (journalName == null || journalName.length() == 0) return;

        // ------------------
        // Search data in Dryad associated with Journal X
        // 
        // ------------------
        Division searchDiv = body.addDivision(SEARCH_DIV, SEARCH_DIV);
        searchDiv.setHead(T_panel_head.parameterize(journalName));
    }
}
>>>>>>> 1f11d49ccd30292c63576a7b9b2e536c7699a90a
