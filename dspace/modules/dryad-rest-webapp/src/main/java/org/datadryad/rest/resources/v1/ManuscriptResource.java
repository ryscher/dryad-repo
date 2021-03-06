/*
 */
package org.datadryad.rest.resources.v1;

import org.apache.log4j.Logger;
import org.datadryad.api.DryadJournalConcept;
import org.datadryad.rest.handler.ManuscriptHandlerGroup;
import org.datadryad.rest.models.Journal;
import org.datadryad.rest.models.Manuscript;
import org.datadryad.rest.models.ResultSet;
import org.datadryad.rest.responses.ErrorsResponse;
import org.datadryad.rest.responses.ResponseFactory;
import org.datadryad.rest.storage.AbstractOrganizationConceptStorage;
import org.datadryad.rest.storage.AbstractManuscriptStorage;
import org.datadryad.rest.storage.StorageException;
import org.datadryad.rest.storage.StoragePath;
import org.dspace.content.authority.Concept;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.net.URI;
import java.util.ArrayList;

/**
 *
 * @author Dan Leehr <dan.leehr@nescent.org>
 */
@Path("organizations/{journalRef}/manuscripts")

public class ManuscriptResource {
    private static final Logger log = Logger.getLogger(ManuscriptResource.class);
    @Inject AbstractManuscriptStorage manuscriptStorage;
    @Inject AbstractOrganizationConceptStorage journalStorage;
    @Context UriInfo uriInfo;
    @Context SecurityContext securityContext;
    private ManuscriptHandlerGroup handlers = new ManuscriptHandlerGroup();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getManuscripts(@PathParam(StoragePath.JOURNAL_PATH) String journalRef,
                                   @QueryParam("search") String searchParam,
                                   @DefaultValue("20") @QueryParam("count") Integer resultParam,
                                   @DefaultValue("0") @QueryParam("cursor") Integer cursorParam) {
        try {
            // Returning a list requires POJO turned on
            StoragePath path = StoragePath.createJournalPath(journalRef);
            ArrayList<Manuscript> manuscripts = new ArrayList<Manuscript>();
            ResultSet resultSet = manuscriptStorage.getResults(path, manuscripts, searchParam, resultParam, cursorParam);

            URI firstLink = uriInfo.getRequestUriBuilder().replaceQueryParam("cursor",resultSet.getFirstCursor()).build();
            URI lastLink = uriInfo.getRequestUriBuilder().replaceQueryParam("cursor",resultSet.getLastCursor()).build();
            int total = resultSet.itemList.size();
            Response.ResponseBuilder responseBuilder = Response.ok(manuscripts).link(firstLink, "first").link(lastLink, "last").header("X-Total-Count", total);
            if (resultSet.getNextCursor() > 0) {
                URI nextLink = uriInfo.getRequestUriBuilder().replaceQueryParam("cursor", resultSet.getNextCursor()).build();
                responseBuilder.link(nextLink, "next");
            }
            if (resultSet.getPreviousCursor() > 0) {
                URI prevLink = uriInfo.getRequestUriBuilder().replaceQueryParam("cursor", resultSet.getPreviousCursor()).build();
                responseBuilder.link(prevLink, "prev");
            }
            return responseBuilder.build();
        } catch (StorageException ex) {
            log.error("Exception getting manuscripts", ex);
            ErrorsResponse error = ResponseFactory.makeError(ex.getMessage(), "Unable to list manuscripts", uriInfo, Status.INTERNAL_SERVER_ERROR.getStatusCode());
            return error.toResponse().build();
        }
    }

    @Path("/{manuscriptId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getManuscript(@PathParam(StoragePath.JOURNAL_PATH) String journalCode, @PathParam(StoragePath.MANUSCRIPT_PATH) String manuscriptId) {
        try {
            StoragePath manuscriptPath = StoragePath.createManuscriptPath(journalCode, manuscriptId);
            Manuscript manuscript = manuscriptStorage.findByPath(manuscriptPath);
            if(manuscript == null) {
                ErrorsResponse error = ResponseFactory.makeError("Manuscript with ID " + manuscriptId + " does not exist", "Manuscript not found", uriInfo, Status.NOT_FOUND.getStatusCode());
                return Response.status(Status.NOT_FOUND).entity(error).build();
            } else {
                return Response.ok(manuscript).build();
            }
        } catch (StorageException ex) {
            log.error("Exception getting manuscript", ex);
            ErrorsResponse error = ResponseFactory.makeError(ex.getMessage(), "Unable to get manuscript", uriInfo, Status.INTERNAL_SERVER_ERROR.getStatusCode());
            return error.toResponse().build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createManuscript(@PathParam(StoragePath.JOURNAL_PATH) String journalCode, Manuscript manuscript) {
        StoragePath journalPath = StoragePath.createJournalPath(journalCode);
        if(manuscript.isValid()) {
            try {
                // Find the journal in database first.
                manuscript.setJournalConcept(journalStorage.findByPath(journalPath));
                manuscriptStorage.create(journalPath, manuscript);
            } catch (StorageException ex) {
                log.error("Exception creating manuscript", ex);
                ErrorsResponse error = ResponseFactory.makeError(ex.getMessage(), "Unable to create manuscript", uriInfo, Status.INTERNAL_SERVER_ERROR.getStatusCode());
                return error.toResponse().build();
            }
            // call handlers - must set journal first
            handlers.handleObjectCreated(journalPath, manuscript);
            UriBuilder ub = uriInfo.getAbsolutePathBuilder();
            URI uri = ub.path(manuscript.getManuscriptId()).build();
            return Response.created(uri).entity(manuscript).build();
        } else {
            ErrorsResponse error = ResponseFactory.makeError("Please check the structure of your object", "Invalid manuscript object", uriInfo, Status.BAD_REQUEST.getStatusCode());
            return error.toResponse().build();
        }
    }

    @Path("/{manuscriptId}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateManuscript(@PathParam(StoragePath.JOURNAL_PATH) String journalCode, @PathParam(StoragePath.MANUSCRIPT_PATH) String manuscriptId, Manuscript manuscript) {
        StoragePath path = StoragePath.createManuscriptPath(journalCode, manuscriptId);
        if(manuscript.isValid()) {
            try {
                StoragePath journalPath = StoragePath.createJournalPath(journalCode);
                manuscript.setJournalConcept(journalStorage.findByPath(journalPath));
                manuscriptStorage.update(path, manuscript);
            } catch (StorageException ex) {
                log.error("Exception updating manuscript", ex);
                ErrorsResponse error = ResponseFactory.makeError(ex.getMessage(), "Unable to update manuscript", uriInfo, Status.INTERNAL_SERVER_ERROR.getStatusCode());
                return error.toResponse().build();
            }
            // call handlers - must set journal first.
            handlers.handleObjectUpdated(path, manuscript);
            return Response.ok(manuscript).build();
        } else {
            ErrorsResponse error = ResponseFactory.makeError("Please check the structure of your object",  "Invalid manuscript object", uriInfo, Status.BAD_REQUEST.getStatusCode());
            return error.toResponse().build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateManuscript(@PathParam(StoragePath.JOURNAL_PATH) String journalCode, Manuscript manuscript) {
        String manuscriptId = manuscript.getManuscriptId();
        return updateManuscript(journalCode, manuscriptId, manuscript);
    }

    @Path("/{manuscriptId}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteManuscript(@PathParam(StoragePath.JOURNAL_PATH) String journalCode, @PathParam(StoragePath.MANUSCRIPT_PATH) String manuscriptId) {
        StoragePath path = StoragePath.createManuscriptPath(journalCode, manuscriptId);
        try {
            manuscriptStorage.deleteByPath(path);
        } catch (StorageException ex) {
            log.error("Exception deleting manuscript", ex);
            ErrorsResponse error = ResponseFactory.makeError(ex.getMessage(), "Unable to delete manuscript", uriInfo, Status.INTERNAL_SERVER_ERROR.getStatusCode());
            return error.toResponse().build();
        }
        // TODO: invoke handlers on deleted object - if we need to
        return Response.noContent().build();
    }
}
