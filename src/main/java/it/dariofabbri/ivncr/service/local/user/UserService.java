package it.dariofabbri.ivncr.service.local.user;

import java.util.List;

import it.dariofabbri.ivncr.model.security.Role;
import it.dariofabbri.ivncr.model.security.User;
import it.dariofabbri.ivncr.service.local.QueryResult;
import it.dariofabbri.ivncr.service.local.Service;

public interface UserService extends Service {

	QueryResult<User> listUsers(
			String username,
			String firstName,
			String lastName,
			String description,
			Integer offset,
			Integer limit);

	User retrieveUserById(Integer id);
	User retrieveUserByUsername(String username);

	void deleteUserById(Integer id);

	User createUser(String username, String password, String firstName, String lastName, String description);

	User updateUser(Integer id, String username, String password, String firstName, String lastName, String description);

	List<Role> retrieveUserRoles(Integer id);
	
	Role addRoleToUser(Integer userId, Integer roleId);
	
	void deleteRoleFromUser(Integer userId, Integer roleId);
}