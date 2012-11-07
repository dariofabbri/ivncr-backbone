package it.dariofabbri.ivncr.service.rest.resource;


import it.dariofabbri.ivncr.model.security.User;
import it.dariofabbri.ivncr.service.local.QueryResult;
import it.dariofabbri.ivncr.service.local.ServiceFactory;
import it.dariofabbri.ivncr.service.local.user.UserService;
import it.dariofabbri.ivncr.service.rest.dto.UserDTO;
import it.dariofabbri.ivncr.service.rest.dto.UsersDTO;

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
		boolean result = us.deleteUserById(id);
		
		if(result) {
			return Response.ok().build();
		}
		else {
			return Response.status(Status.NOT_FOUND).build();
		}
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
		boolean result = us.createUser(
				user.getUsername(),
				user.getFirstName(),
				user.getLastName(),
				user.getDescription());
		
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
	public Response updateUser(@PathParam("id") Integer id, UserDTO user) {
		
		logger.debug("updateUser called!");
		
		Subject currentUser = SecurityUtils.getSubject();
		if(!currentUser.isPermitted("users:update")) {
			return Response.status(Status.UNAUTHORIZED).entity("Operation not permitted.").build();
		}

		UserService us = ServiceFactory.createUserService();
		boolean result = us.updateUser(
				id,
				user.getUsername(),
				user.getFirstName(),
				user.getLastName(),
				user.getDescription());
		
		if(result) {
			return Response.ok().build();
		}
		else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}
}