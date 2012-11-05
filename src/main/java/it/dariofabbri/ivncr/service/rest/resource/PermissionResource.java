package it.dariofabbri.ivncr.service.rest.resource;


import it.dariofabbri.ivncr.service.rest.dto.PermissionDTO;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/permission")
@Produces("application/json")
public class PermissionResource {

	private static final Logger logger = LoggerFactory.getLogger(PermissionResource.class);
	
	
	@GET
	@Path("/{action}")
	public Response checkPermission(
			@PathParam("action") String action) {

		logger.debug("checkPermission called!");

		Subject currentUser = SecurityUtils.getSubject();
		
		boolean allowed = currentUser.isPermitted(action);

		PermissionDTO dto = new PermissionDTO();
		dto.setAction(action);
		dto.setAllowed(allowed);
		
		return Response
			.ok()
			.entity(dto)
			.build();
	}
	
	
	@GET
	public Response checkPermissions(
			@QueryParam("actions") String actions) {

		logger.debug("checkPermissions called!");

		String[] permissions = actions.split(",");
		
		Subject currentUser = SecurityUtils.getSubject();

		boolean[] result = currentUser.isPermitted(permissions);
		if(result.length != permissions.length)
			throw new RuntimeException("Unexpected number of results obtained while checking permissions.");
		
		List<PermissionDTO> list = new ArrayList<PermissionDTO>();
		for(int i = 0; i < result.length; ++i) {
			
			PermissionDTO dto = new PermissionDTO();
			dto.setAction(permissions[i]);
			dto.setAllowed(result[i]);
			
			list.add(dto);
		}

		return Response
			.ok()
			.entity(list)
			.build();
	}	
}
