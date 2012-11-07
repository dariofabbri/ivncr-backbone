package it.dariofabbri.ivncr.service.local.role;

import it.dariofabbri.ivncr.model.security.Role;
import it.dariofabbri.ivncr.service.local.AbstractService;
import it.dariofabbri.ivncr.service.local.QueryResult;

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

		String hql = 
				"from Role rol " +
				"where rol.id = :id";
		Query query = session.createQuery(hql);
		query.setParameter("id", id);
		Role role = (Role)query.uniqueResult();
		logger.debug("Role found: " + role);
		
		return role;
	}

	@Override
	public boolean deleteRoleById(Integer id) {
		
		Role role = retrieveRoleById(id);
		if(role == null)
			return false;
		
		session.delete(role);
		return true;
	}

	@Override
	public boolean createRole(
			String rolename, 
			String description) {
		
		Role role = new Role();
		
		role.setRolename(rolename);
		role.setDescription(description);
		
		session.save(role);
		
		return true;
	}

	@Override
	public boolean updateRole(
			Integer id, 
			String rolename,
			String description) {
		
		Role role = retrieveRoleById(id);
		if(role == null)
			return false;
		
		role.setRolename(rolename);
		role.setDescription(description);
		
		session.update(role);
		
		return true;
	}
}