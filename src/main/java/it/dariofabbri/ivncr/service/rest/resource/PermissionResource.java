package it.dariofabbri.ivncr.service.rest.resource;


import it.dariofabbri.ivncr.model.security.Permission;
import it.dariofabbri.ivncr.service.local.NotFoundException;
import it.dariofabbri.ivncr.service.local.QueryResult;
import it.dariofabbri.ivncr.service.local.ServiceFactory;
import it.dariofabbri.ivncr.service.local.permission.PermissionService;
import it.dariofabbri.ivncr.service.rest.dto.PermissionDTO;
import it.dariofabbri.ivncr.service.rest.dto.PermissionsDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/permissions")
@Produces("application/json")
public class PermissionResource {

	private static final Logger logger = LoggerFactory.getLogger(PermissionResource.class);
	
	@GET
	public Response getPermissions(
			@QueryParam("permissionString") String permissionString,
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit) {

		logger.debug("getPermissions called!");
		
		Subject currentUser = SecurityUtils.getSubject();
		if(!currentUser.isPermitted("permissions:list")) {
			return Response.status(Status.UNAUTHORIZED).entity("Operation not permitted.").build();
		}
		
		// Execute query.
		//
		PermissionService ps = ServiceFactory.createPermissionService();
		QueryResult<Permission> result = ps.listPermissions(
				permissionString,
				offset, 
				limit);
		
		// Set up response.
		//
		Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance();
        PermissionsDTO dto = mapper.map(result, PermissionsDTO.class);
		
		return Response.ok().entity(dto).build();
	}
	
	@GET
	@Path("/{id}")
	public Response getPermission(@PathParam("id") Integer id) {

		logger.debug("getPermission called!");
		
		Subject currentUser = SecurityUtils.getSubject();
		if(!currentUser.isPermitted("permissions:get")) {
			return Response.status(Status.UNAUTHORIZED).entity("Operation not permitted.").build();
		}
		
		PermissionService ps = ServiceFactory.createPermissionService();
		Permission permission = ps.retrievePermissionById(id);
		if(permission == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance();
        PermissionDTO dto = mapper.map(permission, PermissionDTO.class);

		return Response.ok().entity(dto).build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response deletePermission(@PathParam("id") Integer id) {
		
		logger.debug("deletePermission called!");
		
		Subject currentUser = SecurityUtils.getSubject();
		if(!currentUser.isPermitted("permissions:delete")) {
			return Response.status(Status.UNAUTHORIZED).entity("Operation not permitted.").build();
		}
		
		PermissionService ps = ServiceFactory.createPermissionService();
		try {
			ps.deletePermissionById(id);
		}
		catch(NotFoundException nfe) {
			return Response.status(Status.NOT_FOUND).entity(nfe.getMessage()).build();			
		}
		
		return Response.ok().build();
	}
	
	@POST
	@Consumes("application/json")
	public Response createPermission(PermissionDTO permission) {
		
		logger.debug("createPermission called!");
		
		Subject currentUser = SecurityUtils.getSubject();
		if(!currentUser.isPermitted("permissions:create")) {
			return Response.status(Status.UNAUTHORIZED).entity("Operation not permitted.").build();
		}

		PermissionService ps = ServiceFactory.createPermissionService();
		Permission entity = ps.createPermission(
				permission.getPermissionString());
		
		if(entity == null) {
			return Response.status(Status.CONFLICT).build();
		}
		
		Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance();
		PermissionDTO dto = mapper.map(entity, PermissionDTO.class);
		return Response.status(Status.CREATED).entity(dto).build();
	}

	@PUT
	@Consumes("application/json")
	@Path("/{id}")
	public Response updatePermission(@PathParam("id") Integer id, PermissionDTO permission) {
		
		logger.debug("updatePermission called!");
		
		Subject currentUser = SecurityUtils.getSubject();
		if(!currentUser.isPermitted("permissions:update")) {
			return Response.status(Status.UNAUTHORIZED).entity("Operation not permitted.").build();
		}

		PermissionService ps = ServiceFactory.createPermissionService();
		try {
			ps.updatePermission(
				id,
				permission.getPermissionString());
			
		} catch(NotFoundException nfe) {
			return Response.status(Status.NOT_FOUND).entity(nfe.getMessage()).build();
		}
		
		return Response.ok().build();
	}
}