package it.dariofabbri.ivncr.service.rest.resource;


import it.dariofabbri.ivncr.model.security.Role;
import it.dariofabbri.ivncr.service.local.QueryResult;
import it.dariofabbri.ivncr.service.local.ServiceFactory;
import it.dariofabbri.ivncr.service.local.role.RoleService;
import it.dariofabbri.ivncr.service.rest.dto.RoleDTO;
import it.dariofabbri.ivncr.service.rest.dto.RolesDTO;

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

@Path("/roles")
@Produces("application/json")
public class RoleResource {

	private static final Logger logger = LoggerFactory.getLogger(RoleResource.class);
	
	@GET
	public Response getRoles(
			@QueryParam("rolename") String rolename,
			@QueryParam("description") String description,
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit) {

		logger.debug("getRoles called!");
		
		Subject currentUser = SecurityUtils.getSubject();
		if(!currentUser.isPermitted("roles:list")) {
			return Response.status(Status.UNAUTHORIZED).entity("Operation not permitted.").build();
		}
		
		// Execute query.
		//
		RoleService rs = ServiceFactory.createRoleService();
		QueryResult<Role> result = rs.listRoles(
				rolename,
				description, 
				offset, 
				limit);
		
		// Set up response.
		//
		Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance();
        RolesDTO dto = mapper.map(result, RolesDTO.class);
		
		return Response.ok().entity(dto).build();
	}
	
	@GET
	@Path("/{id}")
	public Response getRole(@PathParam("id") Integer id) {

		logger.debug("getRole called!");
		
		Subject currentUser = SecurityUtils.getSubject();
		if(!currentUser.isPermitted("roles:get")) {
			return Response.status(Status.UNAUTHORIZED).entity("Operation not permitted.").build();
		}
		
		RoleService rs = ServiceFactory.createRoleService();
		Role role = rs.retrieveRoleById(id);
		if(role == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance();
        RoleDTO dto = mapper.map(role, RoleDTO.class);

		return Response.ok().entity(dto).build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response deleteRole(@PathParam("id") Integer id) {
		
		logger.debug("deleteRole called!");
		
		Subject currentUser = SecurityUtils.getSubject();
		if(!currentUser.isPermitted("roles:delete")) {
			return Response.status(Status.UNAUTHORIZED).entity("Operation not permitted.").build();
		}
		
		RoleService rs = ServiceFactory.createRoleService();
		boolean result = rs.deleteRoleById(id);
		
		if(result) {
			return Response.ok().build();
		}
		else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}
	
	@POST
	@Consumes("application/json")
	public Response createRole(RoleDTO role) {
		
		logger.debug("createRole called!");
		
		Subject currentUser = SecurityUtils.getSubject();
		if(!currentUser.isPermitted("roles:create")) {
			return Response.status(Status.UNAUTHORIZED).entity("Operation not permitted.").build();
		}

		RoleService rs = ServiceFactory.createRoleService();
		boolean result = rs.createRole(
				role.getRolename(),
				role.getDescription());
		
		if(result) {
			return Response.ok().build();
		}
		else {
			return Response.status(Status.CONFLICT).build();
		}
	}
	
	@PUT
	@Consumes("application/json")
	@Path("/{id}")
	public Response updateRole(@PathParam("id") Integer id, RoleDTO role) {
		
		logger.debug("updateRole called!");
		
		Subject currentUser = SecurityUtils.getSubject();
		if(!currentUser.isPermitted("roles:update")) {
			return Response.status(Status.UNAUTHORIZED).entity("Operation not permitted.").build();
		}

		RoleService rs = ServiceFactory.createRoleService();
		boolean result = rs.updateRole(
				id,
				role.getRolename(),
				role.getDescription());
		
		if(result) {
			return Response.ok().build();
		}
		else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}
}