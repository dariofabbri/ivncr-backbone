package it.dariofabbri.ivncr.service.local.security;

import it.dariofabbri.ivncr.model.security.Permission;
import it.dariofabbri.ivncr.model.security.Role;
import it.dariofabbri.ivncr.model.security.User;
import it.dariofabbri.ivncr.service.local.Service;

import java.util.List;

public interface SecurityService extends Service {

	User getByUsername(String username);

	List<Role> getUserRoles(String username);

	List<Permission> getUserPermissions(String username);
}