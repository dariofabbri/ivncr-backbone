package it.dariofabbri.ivncr.service.local.user;

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

	boolean deleteUserById(Integer id);

	boolean createUser(String username, String firstName, String lastName, String description);

	boolean updateUser(Integer id, String username, String firstName, String lastName, String description);
}