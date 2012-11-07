package it.dariofabbri.ivncr.service.local.user;

import it.dariofabbri.ivncr.model.security.Role;
import it.dariofabbri.ivncr.model.security.User;
import it.dariofabbri.ivncr.service.local.AbstractService;
import it.dariofabbri.ivncr.service.local.QueryResult;

import java.util.List;

import org.hibernate.Query;

public class UserServiceImpl extends AbstractService implements UserService {

	@Override
	public QueryResult<User> listUsers(
			String username,
			String firstName,
			String lastName,
			String description,
			Integer offset,
			Integer limit) {

		QueryUserByUsernameFirstNameLastNameDescription q = new QueryUserByUsernameFirstNameLastNameDescription(session);

		q.setUsername(username);
		q.setFirstName(firstName);
		q.setLastName(lastName);
		q.setDescription(description);
		q.setOffset(offset);
		q.setLimit(limit);
		
		return q.query();
	}

	@Override
	public User retrieveUserById(Integer id) {

		String hql = 
				"from User use " +
				"where use.id = :id";
		Query query = session.createQuery(hql);
		query.setParameter("id", id);
		User user = (User)query.uniqueResult();
		logger.debug("User found: " + user);
		
		return user;
	}

	@Override
	public boolean deleteUserById(Integer id) {
		
		User user = retrieveUserById(id);
		if(user == null)
			return false;
		
		session.delete(user);
		return true;
	}

	@Override
	public boolean createUser(
			String username, 
			String firstName, 
			String lastName, 
			String description) {
		
		User user = new User();
		
		user.setUsername(username);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setDescription(description);
		
		session.save(user);
		
		return true;
	}

	@Override
	public boolean updateUser(
			Integer id, 
			String username,
			String firstName, 
			String lastName, 
			String description) {
		
		User user = retrieveUserById(id);
		if(user == null)
			return false;
		
		user.setUsername(username);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setDescription(description);
		
		session.update(user);
		
		return true;
	}
		
	@SuppressWarnings("unchecked")
	@Override
	public List<Role> retrieveRolesByUserId(Integer id) {

		String hql = 
				"select distinct rol from Role rol " +
				"inner join rol.users use " +
				"where use.id = :id";
		Query query = session.createQuery(hql);
		query.setParameter("id", id);
		List<Role> list = (List<Role>)query.list();
		logger.debug("Roles found: " + list);
		
		return list;
	}
}