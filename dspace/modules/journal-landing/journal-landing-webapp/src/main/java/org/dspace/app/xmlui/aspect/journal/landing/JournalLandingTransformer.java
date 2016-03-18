/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.xmlui.aspect.journal.landing;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.log4j.Logger;
import org.datadryad.api.DryadJournal;
import org.dspace.JournalUtils;
import org.dspace.app.xmlui.cocoon.AbstractDSpaceTransformer;
import org.dspace.app.xmlui.utils.UIException;
import org.dspace.app.xmlui.wing.Message;
import org.dspace.app.xmlui.wing.WingException;
import org.dspace.app.xmlui.wing.element.Body;
import org.dspace.app.xmlui.wing.element.Division;
import org.dspace.app.xmlui.wing.element.Para;
import org.dspace.authorize.AuthorizeException;
import org.dspace.content.authority.AuthorityMetadataValue;
import org.dspace.content.authority.Concept;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import static org.dspace.app.xmlui.aspect.journal.landing.Const.*;

/**
 * Journal landing page transformer base class.
 *
 * @author Nathan Day
 */
public class JournalLandingTransformer extends AbstractDSpaceTransformer {

    private static final Logger log = Logger.getLogger(JournalLandingTransformer.class);

    protected DryadJournal dryadJournal;
    protected Concept journalConcept;
    protected String journalISSN;
    protected String journalName;
    protected String journalAbbr;

    @Override
    public void setup(SourceResolver resolver, Map objectModel, String src,
            Parameters parameters) throws ProcessingException, SAXException,
            IOException
    {
        super.setup(resolver, objectModel, src, parameters);
        Integer conceptID = null;
        try {
            conceptID = Integer.parseInt(parameters.getParameter(PARAM_CONCEPT_ID));
            journalConcept = Concept.find(this.context, conceptID);
            journalISSN = parameters.getParameter(PARAM_JOURNAL_ISSN);
            journalName = journalConcept.getLabel();
            AuthorityMetadataValue[] mdvs = journalConcept.getMetadata("journal","journalID", null, null);
            if (mdvs != null && mdvs.length > 0) {
                journalAbbr = mdvs[0].getValue();
            }
        } catch (Exception ex) {
            log.error(ex);
            throw(new ProcessingException("Bad access of journal concept: " + ex.getMessage()));
        }
        try {
            dryadJournal = new DryadJournal(context, journalName);
        } catch (Exception ex) {
            log.error(ex);
            throw(new ProcessingException("Failed to make DryadJournal for concept id: " + conceptID));
        }
        if (journalConcept == null || dryadJournal == null) {
            throw(new ProcessingException("Failed to retrieve journal for concept id: " + conceptID));
        }
    }
}
