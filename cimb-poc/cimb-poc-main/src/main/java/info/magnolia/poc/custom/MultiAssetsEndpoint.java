package info.magnolia.poc.custom;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.magnolia.cms.util.ExclusiveWrite;
import info.magnolia.context.MgnlContext;
import info.magnolia.rest.AbstractEndpoint;
import info.magnolia.rest.service.node.definition.NodeEndpointDefinition;
import info.magnolia.rest.service.node.v1.RepositoryMarshaller;
import info.magnolia.rest.service.node.v1.RepositoryNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


/**
 * Endpoint for accessing and manipulating nodes.
 *
 * @param <D> The endpoint definition
 */
@Path("/multiassets/v1")
@Tag(name = "Nodes API")
public class MultiAssetsEndpoint<D extends NodeEndpointDefinition> extends AbstractEndpoint<D> {

    private static final String STATUS_MESSAGE_OK = "OK";
    private static final String STATUS_MESSAGE_BAD_REQUEST = "Request not understood due to errors or malformed syntax";
    private static final String STATUS_MESSAGE_UNAUTHORIZED = "Unauthorized";
    private static final String STATUS_MESSAGE_ACCESS_DENIED = "Access denied";
    private static final String STATUS_MESSAGE_NODE_NOT_FOUND = "Node not found";
    private static final String STATUS_MESSAGE_ERROR_OCCURRED = "Error occurred";

    private final Logger log = LoggerFactory.getLogger(getClass());

    private MigrateAssetMarshaller assetMarshaller = new MigrateAssetMarshaller();
    
    private RepositoryMarshaller marshaller = new RepositoryMarshaller();

    @Inject
    public MultiAssetsEndpoint(final D endpointDefinition) {
        super(endpointDefinition);
    }

    /**
     * Returns a node including its properties and child nodes down to a certain depth.
     */
    @GET
    @Path("/{workspace}{path:(/.+)?}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Operation(summary = "Get a node", description = "Returns a node from the specified workspace and path")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = STATUS_MESSAGE_OK, content = @Content(schema = @Schema(implementation = RepositoryNode.class))),
            @ApiResponse(responseCode = "401", description = STATUS_MESSAGE_UNAUTHORIZED),
            @ApiResponse(responseCode = "404", description = STATUS_MESSAGE_NODE_NOT_FOUND),
            @ApiResponse(responseCode = "500", description = STATUS_MESSAGE_ERROR_OCCURRED)
    })
    public Response readNode(
            @PathParam("workspace") String workspaceName,
            @PathParam("path") @DefaultValue("/") String path,
            @QueryParam("depth") @DefaultValue("0") int depth,
            @QueryParam("excludeNodeTypes") @DefaultValue("") String excludeNodeTypes,
            @QueryParam("includeMetadata") @DefaultValue("false") boolean includeMetadata) throws RepositoryException {

        final String absPath = StringUtils.defaultIfEmpty(path, "/");

        final Session session = MgnlContext.getJCRSession(workspaceName);

        if (!session.nodeExists(absPath)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        final Node node = session.getNode(absPath);

        final RepositoryNode response = marshaller.marshallNode(node, depth, splitExcludeNodeTypesString(excludeNodeTypes), includeMetadata);

        log.debug("Returned node [{}]", node.getPath());

        return Response.ok(response).build();
    }

    /**
     * Creates a new node and populates it with the supplied properties. Does not support adding sub nodes. The
     * submitted node must contain name and type.
     */
    @PUT
    @Path("/{workspace}{path:(/.+)?}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Operation(summary = "Create a node", description = "Creates a node and adds passed properties")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = STATUS_MESSAGE_OK),
            @ApiResponse(responseCode = "400", description = STATUS_MESSAGE_BAD_REQUEST),
            @ApiResponse(responseCode = "401", description = STATUS_MESSAGE_UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = STATUS_MESSAGE_ACCESS_DENIED),
            @ApiResponse(responseCode = "404", description = STATUS_MESSAGE_NODE_NOT_FOUND),
            @ApiResponse(responseCode = "500", description = STATUS_MESSAGE_ERROR_OCCURRED)
    })
    public Response createNode(
            @PathParam("workspace") String workspaceName,
            @PathParam("path") @DefaultValue("/") String parentPath,
            List<MigrateAsset> assets) throws RepositoryException {

        final String parentAbsPath = StringUtils.defaultIfEmpty(parentPath, "/");
//
//        if (StringUtils.isEmpty(repositoryNode.getName())) {
//            return Response.status(Response.Status.BAD_REQUEST).build();
//        }
//
//        if (StringUtils.isEmpty(repositoryNode.getType())) {
//            return Response.status(Response.Status.BAD_REQUEST).build();
//        }
//
//        if (!StringUtils.isEmpty(repositoryNode.getPath()) && !repositoryNode.getPath().equals(PathUtil.createPath(parentAbsPath, repositoryNode.getName()))) {
//            return Response.status(Response.Status.BAD_REQUEST).build();
//        }
//
//        if (repositoryNode.getNodes() != null && !repositoryNode.getNodes().isEmpty()) {
//            return Response.status(Response.Status.BAD_REQUEST).build();
//        }

        final Session session = MgnlContext.getJCRSession(workspaceName);

        if (!session.nodeExists(parentAbsPath)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        final Node parentNode = session.getNode(parentAbsPath);

//        if (parentNode.hasNode(repositoryNode.getName())) {
//            return Response.status(Response.Status.BAD_REQUEST).build();
//        }
        
        for(MigrateAsset asset : assets) {
        	 final Node node = parentNode.addNode(asset.getFn(), "mgnl:asset");
                 assetMarshaller.marshallNode(node, asset);

             synchronized (ExclusiveWrite.getInstance()) {
                 session.save();
             }

             log.debug("Created a new node [{}]", node.getPath());
        }

        return Response.ok().build();
    }

    /**
     * Adds properties to a node. Existing properties are changed if present in the request. Existing properties not
     * present in the request are not removed.
     */
    @POST
    @Path("/{workspace}{path:(/.+)?}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Operation(summary = "Update a node", description = "Updates a node by adding passed properties")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = STATUS_MESSAGE_OK),
            @ApiResponse(responseCode = "400", description = STATUS_MESSAGE_BAD_REQUEST),
            @ApiResponse(responseCode = "401", description = STATUS_MESSAGE_UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = STATUS_MESSAGE_ACCESS_DENIED),
            @ApiResponse(responseCode = "404", description = STATUS_MESSAGE_NODE_NOT_FOUND),
            @ApiResponse(responseCode = "500", description = STATUS_MESSAGE_ERROR_OCCURRED)
    })
    public Response updateNode(
            @PathParam("workspace") String workspaceName,
            @PathParam("path") @DefaultValue("/") String path,
            RepositoryNode repositoryNode) throws RepositoryException {

        final String absPath = StringUtils.defaultIfEmpty(path, "/");

        if (repositoryNode.getPath() != null && !StringUtils.equals(absPath, repositoryNode.getPath())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        String name = StringUtils.substringAfterLast(absPath, "/");
        if (repositoryNode.getName() != null && !StringUtils.equals(name, repositoryNode.getName())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (repositoryNode.getNodes() != null && !repositoryNode.getNodes().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        final Session session = MgnlContext.getJCRSession(workspaceName);

        if (!session.nodeExists(absPath)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        final Node node = session.getNode(absPath);

        if (repositoryNode.getType() != null && !repositoryNode.getType().equals(node.getPrimaryNodeType().getName())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (repositoryNode.getIdentifier() != null && !repositoryNode.getIdentifier().equals(node.getIdentifier())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (repositoryNode.getProperties() != null) {
            marshaller.unmarshallProperties(node, repositoryNode.getProperties());
        }

        synchronized (ExclusiveWrite.getInstance()) {
            session.save();
        }

        log.debug("Updated node [{}]", node.getPath());

        return Response.ok().build();
    }

    /**
     * Delete a node.
     */
    @DELETE
    @Path("/{workspace}{path:(/.+)?}")
    @Operation(summary = "Delete a node", description = "Deletes a node")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = STATUS_MESSAGE_OK),
            @ApiResponse(responseCode = "401", description = STATUS_MESSAGE_UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = STATUS_MESSAGE_ACCESS_DENIED),
            @ApiResponse(responseCode = "404", description = STATUS_MESSAGE_NODE_NOT_FOUND),
            @ApiResponse(responseCode = "500", description = STATUS_MESSAGE_ERROR_OCCURRED)
    })
    public Response deleteNode(
            @PathParam("workspace") String workspaceName,
            @PathParam("path") @DefaultValue("/") String path) throws RepositoryException {

        final String absPath = StringUtils.defaultIfEmpty(path, "/");

        final Session session = MgnlContext.getJCRSession(workspaceName);

        if (!session.nodeExists(absPath)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        final Node node = session.getNode(absPath);

        node.remove();

        synchronized (ExclusiveWrite.getInstance()) {
            session.save();
        }

        log.debug("Deleted node [{}]", absPath);

        return Response.ok().build();
    }

    protected List<String> splitExcludeNodeTypesString(String excludes) {
        List<String> excludeList = new ArrayList<String>();

        if (excludes != null) {
            excludes = StringUtils.replace(excludes, " ", "");
            excludeList = Arrays.asList(StringUtils.split(excludes, ","));
        }

        return excludeList;
    }
}
