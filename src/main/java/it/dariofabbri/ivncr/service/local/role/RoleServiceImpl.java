package it.dariofabbri.ivncr.service.local.role;

import it.dariofabbri.ivncr.model.security.Permission;
import it.dariofabbri.ivncr.model.security.Role;
import it.dariofabbri.ivncr.service.local.AbstractService;
import it.dariofabbri.ivncr.service.local.AlreadyPresentException;
import it.dariofabbri.ivncr.service.local.NotFoundException;
import it.dariofabbri.ivncr.service.local.QueryResult;

import java.util.List;

import org.hibernate.Query;

public class RoleServiceImpl extends AbstractService implements RoleService {

	@Override
	public QueryResult<Role> listRoles(
			String rolename,
			String description,
			Integer offset,
			Integer limit) {

		QueryRoleByRolenameDescription q = new QueryRoleByRolenameDescription(session);

		q.setRolename(rolename);
		q.setDescription(description);
		q.setOffset(offset);
		q.setLimit(limit);
		
		return q.query();
	}

	@Override
	public Role retrieveRoleById(Integer id) {

		Role role = (Role)session.get(Role.class, id);
		logger.debug("Role found: " + role);
		
		return role;
	}

	@Override
	public Role retrieveRoleByRolename(String rolename) {

		String hql = 
				"from Role rol " +
				"where rol.rolename = :rolename";
		Query query = session.createQuery(hql);
		query.setParameter("rolename", rolename);
		Role role = (Role)query.uniqueResult();
		logger.debug("Role found: " + role);
		
		return role;
	}

	@Override
	public void deleteRoleById(Integer id) {
		
		Role role = retrieveRoleById(id);
		if(role == null) {
			String message = String.format("It has not been possible to retrieve specified role: %d", id);
			logger.info(message);
			throw new NotFoundException(message);
		}
		
		session.delete(role);
	}

	@Override
	public Role createRole(
			String rolename, 
			String description) {
		
		Role role = new Role();
		
		role.setRolename(rolename);
		role.setDescription(description);
		
		session.save(role);
		
		return role;
	}

	@Override
	public Role updateRole(
			Integer id, 
			String rolename,
			String description) {
		
		Role role = retrieveRoleById(id);
		if(role == null) {
			String message = String.format("It has not been possible to retrieve specified role: %d", id);
			logger.info(message);
			throw new NotFoundException(message);
		}
		
		role.setRolename(rolename);
		role.setDescription(description);
		
		session.update(role);
		
		return role;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Permission> retrieveRolePermissions(Integer id) {

		Role role = (Role)session.get(Role.class, id);
		if(role == null) {
			String message = String.format("It has not been possible to retrieve specified role: %d", id);
			logger.info(message);
			throw new NotFoundException(message);
		}

		String hql = 
				"select distinct per from Permission per " +
				"inner join per.roles rol " +
				"where rol.id = :id " +
				"order by per.permissionString ";
		Query query = session.createQuery(hql);
		query.setParameter("id", id);
		List<Permission> list = (List<Permission>)query.list();
		logger.debug("Permissions found: " + list);
		
		return list;
	}
	
	@Override
	public Permission addPermissionToRole(Integer roleId, Integer permissionId) {

		Role role = (Role)session.get(Role.class, roleId);
		if(role == null) {
			String message = String.format("It has not been possible to retrieve specified role: %d", roleId);
			logger.info(message);
			throw new NotFoundException(message);
		}
		
		Permission permission = (Permission)session.get(Permission.class, permissionId);
		if(permission == null) {
			String message = String.format("It has not been possible to retrieve specified permission: %d", permissionId);
			logger.info(message);
			throw new NotFoundException(message);
		}
		
		if(role.getPermissions().contains(permission)) {
			String message = String.format("Specified permission %d already associated to specified role %d.", permissionId, roleId);
			logger.info(message);
			throw new AlreadyPresentException(message);
		}
		
		role.getPermissions().add(permission);
		session.update(role);
		
		return permission;
	}
	
	@Override
	public void deletePermissionFromRole(Integer roleId, Integer permissionId) {

		Role role = (Role)session.get(Role.class, roleId);
		if(role == null) {
			String message = String.format("It has not been possible to retrieve specified role: %d", roleId);
			logger.info(message);
			throw new NotFoundException(message);
		}
		
		Permission foundPermission = null;
		for(Permission permission : role.getPermissions()) {
			
			if(permission.getId().equals(permissionId)) {
				foundPermission = permission;
				break;
			}
		}
		
		if(foundPermission == null) {
			String message = String.format("It has not been possible to find permission %d associated to role %d.", permissionId, roleId);
			logger.info(message);
			throw new NotFoundException(message);
		}
		
		role.getPermissions().remove(foundPermission);
		session.update(role);
		session.flush();
	}
}