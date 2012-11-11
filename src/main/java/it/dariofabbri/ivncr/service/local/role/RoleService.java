package it.dariofabbri.ivncr.service.local.role;

import it.dariofabbri.ivncr.model.security.Permission;
import it.dariofabbri.ivncr.model.security.Role;
import it.dariofabbri.ivncr.service.local.QueryResult;
import it.dariofabbri.ivncr.service.local.Service;

import java.util.List;

public interface RoleService extends Service {

	QueryResult<Role> listRoles(
			String rolename,
			String description,
			Integer offset,
			Integer limit);

	Role retrieveRoleById(Integer id);
	Role retrieveRoleByRolename(String rolename);

	void deleteRoleById(Integer id);

	Role createRole(String rolename, String description);

	Role updateRole(Integer id, String rolename, String description);

	List<Permission> retrieveRolePermissions(Integer id);
	
	Permission addPermissionToRole(Integer roleId, Integer permissionId);
	
	void deletePermissionFromRole(Integer roleId, Integer permissionId);
}