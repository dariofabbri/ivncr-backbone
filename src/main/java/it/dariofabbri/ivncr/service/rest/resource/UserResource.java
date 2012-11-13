package it.dariofabbri.ivncr.service.rest.resource;


import it.dariofabbri.ivncr.model.security.Role;
import it.dariofabbri.ivncr.model.security.User;
import it.dariofabbri.ivncr.service.local.AlreadyPresentException;
import it.dariofabbri.ivncr.service.local.NotFoundException;
import it.dariofabbri.ivncr.service.local.QueryResult;
import it.dariofabbri.ivncr.service.local.ServiceFactory;
import it.dariofabbri.ivncr.service.local.user.UserService;
import it.dariofabbri.ivncr.service.rest.dto.RoleDTO;
import it.dariofabbri.ivncr.service.rest.dto.UserDTO;
import it.dariofabbri.ivncr.service.rest.dto.UsersDTO;
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

@Path("/users")
@Produces("application/json")
public class UserResource {

	private static final Logger logger = LoggerFactory.getLogger(UserResource.class);
	
	@GET
	public Response getUsers(
			@QueryParam("username") String username,
			@QueryParam("firstName") String firstName,
			@QueryParam("lastName") String lastName,
			@QueryParam("description") String description,
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit) {

		logger.debug("getUsers called!");
		
		Subject currentUser = SecurityUtils.getSubject();
		if(!currentUser.isPermitted("users:list")) {
			return Response.status(Status.UNAUTHORIZED).entity("Operation not permitted.").build();
		}
		
		// Execute query.
		//
		UserService us = ServiceFactory.createUserService();
		QueryResult<User> result = us.listUsers(
				username,
				firstName, 
				lastName, 
				description, 
				offset, 
				limit);
		
		// Set up response.
		//
		Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance();
        UsersDTO dto = mapper.map(result, UsersDTO.class);
		
		return Response.ok().entity(dto).build();
	}
	
	@GET
	@Path("/{id}")
	public Response getUser(@PathParam("id") Integer id) {

		logger.debug("getUser called!");
		
		Subject currentUser = SecurityUtils.getSubject();
		if(!currentUser.isPermitted("users:get")) {
			return Response.status(Status.UNAUTHORIZED).entity("Operation not permitted.").build();
		}
		
		UserService us = ServiceFactory.createUserService();
		User user = us.retrieveUserById(id);
		if(user == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance();
        UserDTO dto = mapper.map(user, UserDTO.class);

		return Response.ok().entity(dto).build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response deleteUser(@PathParam("id") Integer id) {
		
		logger.debug("deleteUser called!");
		
		Subject currentUser = SecurityUtils.getSubject();
		if(!currentUser.isPermitted("users:delete")) {
			return Response.status(Status.UNAUTHORIZED).entity("Operation not permitted.").build();
		}
		
		UserService us = ServiceFactory.createUserService();
		try {
			us.deleteUserById(id);
		}
		catch(NotFoundException nfe) {
			return Response.status(Status.NOT_FOUND).entity(nfe.getMessage()).build();			
		}
		
		return Response.ok().build();
	}
	
	@POST
	@Consumes("application/json")
	public Response createUser(UserDTO user) {
		
		logger.debug("createUser called!");
		
		Subject currentUser = SecurityUtils.getSubject();
		if(!currentUser.isPermitted("users:create")) {
			return Response.status(Status.UNAUTHORIZED).entity("Operation not permitted.").build();
		}

		UserService us = ServiceFactory.createUserService();
		User entity = us.createUser(
				user.getUsername(),
				user.getFirstName(),
				user.getLastName(),
				user.getDescription());
		
		if(entity == null) {
			return Response.status(Status.CONFLICT).build();
		}
		
		Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance();
        UserDTO dto = mapper.map(entity, UserDTO.class);
		return Response.status(Status.CREATED).entity(dto).build();
	}

	@PUT
	@Consumes("application/json")
	@Path("/{id}")
	public Response updateUser(@PathParam("id") Integer id, UserDTO user) {
		
		logger.debug("updateUser called!");
		
		Subject currentUser = SecurityUtils.getSubject();
		if(!currentUser.isPermitted("users:update")) {
			return Response.status(Status.UNAUTHORIZED).entity("Operation not permitted.").build();
		}

		UserService us = ServiceFactory.createUserService();
		try {
			us.updateUser(
				id,
				user.getUsername(),
				user.getFirstName(),
				user.getLastName(),
				user.getDescription());
			
		} catch(NotFoundException nfe) {
			return Response.status(Status.NOT_FOUND).entity(nfe.getMessage()).build();
		}
		
		return Response.ok().build();
	}

	
	@GET
	@Path("/{id}/roles")
	public Response getRoles(@PathParam("id") Integer id) {

		logger.debug("getRoles called!");
		
		Subject currentUser = SecurityUtils.getSubject();
		if(!currentUser.isPermitted("users:getroles")) {
			return Response.status(Status.UNAUTHORIZED).entity("Operation not permitted.").build();
		}
		
		UserService us = ServiceFactory.createUserService();
		List<Role> roles;
		try {
			roles = us.retrieveUserRoles(id);
		} catch (NotFoundException nfe) {
			return Response.status(Status.NOT_FOUND).entity(nfe.getMessage()).build();
		}

		MappingUtil<RoleDTO> mu = new MappingUtil<RoleDTO>();
		List<RoleDTO> dto = mu.map(roles, RoleDTO.class);

		return Response.ok().entity(dto).build();
	}
	
	
	@PUT
	@Path("/{id}/roles")
	@Consumes("application/json")
	public Response addRole(
			@PathParam("userid") Integer userId, 
			@PathParam("roleid") Integer roleId) {

		logger.debug("addRole called!");
		
		Subject currentUser = SecurityUtils.getSubject();
		if(!currentUser.isPermitted("users:addrole")) {
			return Response.status(Status.UNAUTHORIZED).entity("Operation not permitted.").build();
		}
		
		UserService us = ServiceFactory.createUserService();
		Role entity = null;
		try {
			entity = us.addRoleToUser(userId, roleId);
		} catch(NotFoundException nfe) {
			return Response.status(Status.NOT_FOUND).entity(nfe.getMessage()).build();
		} catch(AlreadyPresentException ape) {
			return Response.status(Status.CONFLICT).entity(ape.getMessage()).build();
		}
				
		Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance();
        RoleDTO dto = mapper.map(entity, RoleDTO.class);
		return Response.ok(dto).build();
	}
	
	
	@DELETE
	@Path("/{userid}/roles/{roleid}")
	public Response deleteRole(
			@PathParam("userid") Integer userId, 
			@PathParam("roleid") Integer roleId) {

		logger.debug("deleteRole called!");
		
		Subject currentUser = SecurityUtils.getSubject();
		if(!currentUser.isPermitted("users:deleterole")) {
			return Response.status(Status.UNAUTHORIZED).entity("Operation not permitted.").build();
		}
		
		UserService us = ServiceFactory.createUserService();
		try {
			us.deleteRoleFromUser(userId, roleId);
		}
		catch(NotFoundException nfe) {
			return Response.status(Status.NOT_FOUND).entity(nfe.getMessage()).build();			
		}

		return Response.ok().build();
	}
}