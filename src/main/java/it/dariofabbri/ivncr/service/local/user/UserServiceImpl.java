package it.dariofabbri.ivncr.service.local.user;

import it.dariofabbri.ivncr.model.security.Role;
import it.dariofabbri.ivncr.model.security.User;
import it.dariofabbri.ivncr.service.local.AbstractService;
import it.dariofabbri.ivncr.service.local.AlreadyPresentException;
import it.dariofabbri.ivncr.service.local.NotFoundException;
import it.dariofabbri.ivncr.service.local.QueryResult;

import java.util.List;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.hibernate.Query;

public class UserServiceImpl extends AbstractService implements UserService {

	private static final int HASH_ITERATIONS = 10000;
	
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

		User user = (User)session.get(User.class, id);
		logger.debug("User found: " + user);
		
		return user;
	}

	@Override
	public User retrieveUserByUsername(String username) {

		String hql = 
				"from User use " +
				"where use.username = :username";
		Query query = session.createQuery(hql);
		query.setParameter("username", username);
		User user = (User)query.uniqueResult();
		logger.debug("User found: " + user);
		
		return user;
	}

	@Override
	public void deleteUserById(Integer id) {
		
		User user = retrieveUserById(id);
		if(user == null) {
			String message = String.format("It has not been possible to retrieve specified user: %d", id);
			logger.info(message);
			throw new NotFoundException(message);
		}
		
		session.delete(user);
	}

	@Override
	public User createUser(
			String username,
			String password,
			String firstName, 
			String lastName, 
			String description) {
		
		User user = new User();
		
		user.setUsername(username);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setDescription(description);
		
		String salt = generateSalt();
		String digest = generateDigest(password, salt, HASH_ITERATIONS);

		user.setDigest(digest);
		user.setSalt(salt);
		user.setIterations(HASH_ITERATIONS);
		
		session.save(user);
		
		return user;
	}

	@Override
	public User updateUser(
			Integer id, 
			String username,
			String password,
			String firstName, 
			String lastName, 
			String description) {

		User user = retrieveUserById(id);
		if(user == null) {
			String message = String.format("It has not been possible to retrieve specified user: %d", id);
			logger.info(message);
			throw new NotFoundException(message);
		}
		
		user.setUsername(username);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setDescription(description);
		
		if(password != null && password.trim().length() > 0) {
			String salt = generateSalt();
			String digest = generateDigest(password, salt, HASH_ITERATIONS);
	
			user.setDigest(digest);
			user.setSalt(salt);
			user.setIterations(HASH_ITERATIONS);
		}
		
		session.update(user);
		
		return user;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Role> retrieveUserRoles(Integer id) {

		User user = (User)session.get(User.class, id);
		if(user == null) {
			String message = String.format("It has not been possible to retrieve specified user: %d", id);
			logger.info(message);
			throw new NotFoundException(message);
		}

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
	
	@Override
	public Role addRoleToUser(Integer userId, Integer roleId) {

		User user = (User)session.get(User.class, userId);
		if(user == null) {
			String message = String.format("It has not been possible to retrieve specified user: %d", userId);
			logger.info(message);
			throw new NotFoundException(message);
		}
		
		Role role = (Role)session.get(Role.class, roleId);
		if(role == null) {
			String message = String.format("It has not been possible to retrieve specified role: %d", roleId);
			logger.info(message);
			throw new NotFoundException(message);
		}
		
		if(user.getRoles().contains(role)) {
			String message = String.format("Specified role %d already associated to specified user %d.", roleId, userId);
			logger.info(message);
			throw new AlreadyPresentException(message);
		}
		
		user.getRoles().add(role);
		session.update(user);
		
		return role;
	}
	
	@Override
	public void deleteRoleFromUser(Integer userId, Integer roleId) {

		User user = (User)session.get(User.class, userId);
		if(user == null) {
			String message = String.format("It has not been possible to retrieve specified user: %d", userId);
			logger.info(message);
			throw new NotFoundException(message);
		}
		
		Role foundRole = null;
		for(Role role : user.getRoles()) {
			
			if(role.getId().equals(roleId)) {
				foundRole = role;
				break;
			}
		}
		
		if(foundRole == null) {
			String message = String.format("It has not been possible to find role %d associated to user %d.", roleId, userId);
			logger.info(message);
			throw new NotFoundException(message);
		}
		
		user.getRoles().remove(foundRole);
		session.update(user);
	}
	
	
	private String generateSalt() {
		
		SecureRandomNumberGenerator srng = new SecureRandomNumberGenerator();
		String salt = srng.nextBytes(64).toHex();

		return salt;
	}
	
	
	private String generateDigest(String password, String salt, int iterations) {
		
		String hashed = new SimpleHash("SHA-512", password, salt, iterations).toHex();
		return hashed;		
	}
}