package it.dariofabbri.ivncr.service.rest.resource;


import it.dariofabbri.ivncr.model.security.Permission;
import it.dariofabbri.ivncr.model.security.Role;
import it.dariofabbri.ivncr.service.local.AlreadyPresentException;
import it.dariofabbri.ivncr.service.local.NotFoundException;
import it.dariofabbri.ivncr.service.local.QueryResult;
import it.dariofabbri.ivncr.service.local.ServiceFactory;
import it.dariofabbri.ivncr.service.local.role.RoleService;
import it.dariofabbri.ivncr.service.rest.dto.PermissionDTO;
import it.dariofabbri.ivncr.service.rest.dto.PermissionsDTO;
import it.dariofabbri.ivncr.service.rest.dto.RoleDTO;
import it.dariofabbri.ivncr.service.rest.dto.RolesDTO;
import it.dariofabbri.ivncr.util.MappingUtil;

import java.util.List;

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
		try {
			rs.deleteRoleById(id);
		}
		catch(NotFoundException nfe) {
			return Response.status(Status.NOT_FOUND).entity(nfe.getMessage()).build();			
		}
		
		return Response.ok().build();
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
		Role entity = rs.createRole(
				role.getRolename(),
				role.getDescription());
		
		if(entity == null) {
			return Response.status(Status.CONFLICT).build();
		}
		
		Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance();
        RoleDTO dto = mapper.map(entity, RoleDTO.class);
		return Response.status(Status.CREATED).entity(dto).build();
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
		try {
			rs.updateRole(
				id,
				role.getRolename(),
				role.getDescription());
			
		} catch(NotFoundException nfe) {
			return Response.status(Status.NOT_FOUND).entity(nfe.getMessage()).build();
		}
		
		return Response.ok().build();
	}

	
	@GET
	@Path("/{id}/permissions")
	public Response getPermissions(@PathParam("id") Integer id) {

		logger.debug("getPermissions called!");
		
		Subject currentUser = SecurityUtils.getSubject();
		if(!currentUser.isPermitted("roles:getpermissions")) {
			return Response.status(Status.UNAUTHORIZED).entity("Operation not permitted.").build();
		}
		
		RoleService rs = ServiceFactory.createRoleService();
		List<Permission> permissions;
		try {
			permissions = rs.retrieveRolePermissions(id);
		} catch (NotFoundException nfe) {
			return Response.status(Status.NOT_FOUND).entity(nfe.getMessage()).build();
		}
		

		MappingUtil<PermissionDTO> mu = new MappingUtil<PermissionDTO>();
		List<PermissionDTO> list = mu.map(permissions, PermissionDTO.class);
		PermissionsDTO dto = new PermissionsDTO();
		dto.setRecords(permissions.size());
		dto.setResults(list);

		return Response.ok().entity(dto).build();
	}
	
	
	@PUT
	@Path("/{roleid}/permissions/{permissionid}")
	public Response addPermission(
			@PathParam("roleid") Integer roleId,
			@PathParam("permissionid") Integer permissionId) {
		
		logger.debug("addPermission called!");
		
		Subject currentUser = SecurityUtils.getSubject();
		if(!currentUser.isPermitted("roles:addpermission")) {
			return Response.status(Status.UNAUTHORIZED).entity("Operation not permitted.").build();
		}
		
		RoleService rs = ServiceFactory.createRoleService();
		Permission entity = null;
		try {
			entity = rs.addPermissionToRole(roleId, permissionId);
		} catch(NotFoundException nfe) {
			return Response.status(Status.NOT_FOUND).entity(nfe.getMessage()).build();
		} catch(AlreadyPresentException ape) {
			return Response.status(Status.CONFLICT).entity(ape.getMessage()).build();
		}
				
		Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance();
        PermissionDTO dto = mapper.map(entity, PermissionDTO.class);
		return Response.ok(dto).build();
	}
	
	
	@DELETE
	@Path("/{roleid}/permissions/{permissionid}")
	public Response deletePermission(
			@PathParam("roleid") Integer roleId,
			@PathParam("permissionid") Integer permissionId) {

		logger.debug("deletePermission called!");
		
		Subject currentUser = SecurityUtils.getSubject();
		if(!currentUser.isPermitted("roles:deletepermission")) {
			return Response.status(Status.UNAUTHORIZED).entity("Operation not permitted.").build();
		}
		
		RoleService rs = ServiceFactory.createRoleService();
		try {
			rs.deletePermissionFromRole(roleId, permissionId);
		}
		catch(NotFoundException nfe) {
			return Response.status(Status.NOT_FOUND).entity(nfe.getMessage()).build();			
		}

		return Response.ok().build();
	}
}