package it.dariofabbri.ivncr.service.local.role;

import it.dariofabbri.ivncr.model.security.Role;
import it.dariofabbri.ivncr.service.local.QueryResult;
import it.dariofabbri.ivncr.service.local.Service;

public interface RoleService extends Service {

	QueryResult<Role> listRoles(
			String rolename,
			String description,
			Integer offset,
			Integer limit);

	Role retrieveRoleById(Integer id);

	boolean deleteRoleById(Integer id);

	boolean createRole(String rolename, String description);

	boolean updateRole(Integer id, String rolename, String description);
}