/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dspace.app.xmlui.aspect.journal.landing;

import org.apache.log4j.Logger;

import org.dspace.app.xmlui.cocoon.AbstractDSpaceTransformer;

import org.dspace.app.xmlui.wing.Message;
import org.dspace.app.xmlui.wing.element.Body;
import org.dspace.app.xmlui.wing.element.Division;
import org.dspace.app.xmlui.wing.element.Para;
/*
import org.dspace.app.xmlui.wing.element.Item;
import org.dspace.app.xmlui.wing.element.List;
import org.dspace.app.xmlui.wing.element.Table;
*/
import static org.dspace.app.xmlui.aspect.journal.landing.Const.*;

import java.sql.SQLException;
import org.dspace.app.xmlui.utils.UIException;
import org.dspace.app.xmlui.wing.WingException;
import org.dspace.authorize.AuthorizeException;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import org.dspace.app.xmlui.wing.element.List;
import org.dspace.app.xmlui.wing.element.ReferenceSet;

/**
 *
 * @author Nathan Day
 */
public class UserGeography extends AbstractDSpaceTransformer {
    
    private static final Logger log = Logger.getLogger(UserGeography.class);

    private static final Message T_head = message("xmlui.JournalLandingPage.UserGeography.panel_head");
    private static final Message T_items = message("xmlui.JournalLandingPage.UserGeography.item_head"); 
    private static final Message T_vals = message("xmlui.JournalLandingPage.UserGeography.val_head"); 
    
    @Override
    public void addBody(Body body) throws SAXException, WingException,
            UIException, SQLException, IOException, AuthorizeException
    {
        // ------------------
        // Geographic breakdown of users
        // 
        // ------------------
        Division userGeo = body.addDivision(USER_GEO_DIV, USER_GEO_DIV);
        userGeo.setHead(T_head);

        Division items = userGeo.addDivision(ITEMS);
        ReferenceSet refs = items.addReferenceSet(TOPTEN_DOWNLOADS_REFS, "summaryList");
        refs.setHead(T_items);

        Division count = userGeo.addDivision(VALS);
        List list = count.addList("most-viewed-count", List.TYPE_SIMPLE, "most-viewed-count");
        list.setHead(T_vals);
        
    }

}
