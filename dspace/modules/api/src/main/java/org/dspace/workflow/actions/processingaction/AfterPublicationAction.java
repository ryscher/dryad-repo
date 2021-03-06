/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dspace.workflow.actions.processingaction;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import org.dspace.authorize.AuthorizeException;
import org.dspace.content.DCDate;
import org.dspace.content.DCValue;
import org.dspace.content.Item;
import org.dspace.content.MetadataSchema;
import org.dspace.core.Context;
import org.dspace.workflow.DryadWorkflowUtils;
import org.dspace.workflow.Step;
import org.dspace.workflow.WorkflowItem;
import org.dspace.workflow.WorkflowManager;
import org.dspace.workflow.actions.ActionResult;

/**
 *
 * @author dan.leehr@nescent.org
 */
public class AfterPublicationAction extends EditMetadataAction {

    @Override
    public ActionResult processMainPage(Context c, WorkflowItem wfi, Step step, HttpServletRequest request) throws SQLException, AuthorizeException, IOException {
        if(request.getParameter("after_blackout_submit") != null) {
            addApprovedProvenance(c, wfi);
            removeEmbargoIfUntilArticleAppears(c, wfi);
            // Finished, archive
            return new ActionResult(ActionResult.TYPE.TYPE_OUTCOME, ActionResult.OUTCOME_COMPLETE);
        } else {
            // User did not click "after_blackout_submit",  defer to base implementation for processing.
            return super.processMainPage(c, wfi, step, request);
        }
    }

    // Derived from EditMetadataAction
    private void addApprovedProvenance(Context c, WorkflowItem wfi) throws SQLException, AuthorizeException {
        //Add the provenance for the accept
        String now = DCDate.getCurrent().toString();

        // Get user's name + email address
        String usersName = WorkflowManager.getEPersonName(c.getCurrentUser());

        String provDescription = getProvenanceStartId() + " Approved for entry into archive by "
                + usersName + " on " + now + " (GMT) ";

        // Add to item as a DC field
        wfi.getItem().addMetadata(MetadataSchema.DC_SCHEMA, "description", "provenance", "en", provDescription);
        wfi.getItem().update();
    }

    private void removeEmbargoIfUntilArticleAppears(Context c, WorkflowItem wfi)
    throws SQLException, AuthorizeException, IOException {
        Item[] dataFiles = DryadWorkflowUtils.getDataFiles(c, wfi.getItem());
        for(Item i : dataFiles){
            DCValue[] values = i.getMetadata("dc.type.embargo");
            if(values!=null && values.length > 0){
                if(values[0].value.equals("untilArticleAppears")){
                    i.clearMetadata("dc", "type", "embargo", Item.ANY);
                    i.addMetadata("dc", "type", "embargo", "en", "none");
                }
            }
            i.update();
        }
    }
}
