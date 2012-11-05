package it.dariofabbri.ivncr.service.rest.resource;


import it.dariofabbri.ivncr.service.rest.dto.PermissionDTO;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
			List<PermissionDTO> permissions) {

		logger.debug("checkPermissions called!");

		Subject currentUser = SecurityUtils.getSubject();

		String[] list = new String[permissions.size()];
		for(int i = 0; i < permissions.size(); ++i) {
			PermissionDTO dto = permissions.get(i);
			list[i] = dto.getAction();
		}
		
		boolean[] result = currentUser.isPermitted(list);
		if(result.length != permissions.size())
			throw new RuntimeException("Unexpected number of results obtained while checking permissions.");
		
		for(int i = 0; i < result.length; ++i) {
			permissions.get(i).setAllowed(result[i]);
		}

		return Response
			.ok()
			.entity(permissions)
			.build();
	}	
}
