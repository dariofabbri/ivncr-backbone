package it.dariofabbri.ivncr.service.local.permission;

import it.dariofabbri.ivncr.model.security.Permission;
import it.dariofabbri.ivncr.service.local.AbstractService;
import it.dariofabbri.ivncr.service.local.NotFoundException;
import it.dariofabbri.ivncr.service.local.QueryResult;

public class PermissionServiceImpl extends AbstractService implements PermissionService {

	@Override
	public QueryResult<Permission> listPermissions(
			String permissionString,
			Integer offset,
			Integer limit) {

		QueryPermissionByPermissionString q = new QueryPermissionByPermissionString(session);

		q.setPermissionString(permissionString);
		q.setOffset(offset);
		q.setLimit(limit);
		
		return q.query();
	}

	@Override
	public Permission retrievePermissionById(Integer id) {

		Permission permission = (Permission)session.get(Permission.class, id);
		logger.debug("Permission found: " + permission);
		
		return permission;
	}

	@Override
	public void deletePermissionById(Integer id) {
		
		Permission permission = retrievePermissionById(id);
		if(permission == null) {
			String message = String.format("It has not been possible to retrieve specified permission: %d", id);
			logger.info(message);
			throw new NotFoundException(message);
		}
		
		session.delete(permission);
	}

	@Override
	public Permission createPermission(
			String permissionString) {
		
		Permission permission = new Permission();
		permission.setPermissionString(permissionString);
		
		session.save(permission);
		
		return permission;
	}

	@Override
	public Permission updatePermission(
			Integer id, 
			String permissionString) {
		
		Permission permission = retrievePermissionById(id);
		if(permission == null) {
			String message = String.format("It has not been possible to retrieve specified permission: %d", id);
			logger.info(message);
			throw new NotFoundException(message);
		}
		
		permission.setPermissionString(permissionString);
		
		session.update(permission);
		
		return permission;
	}
}