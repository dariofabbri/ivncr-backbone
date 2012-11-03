package it.dariofabbri.ivncr.service.rest.dto;

import java.util.Date;
import java.util.List;

public class SecurityDTO {

	private Boolean loggedOn;
	private String username;
	private String password;
	private String name;
	private String surname;
	private Date logonTs;
	private String securityToken;
	private List<String> roles;
	private List<String> permissions;

	public Boolean getLoggedOn() {
		return loggedOn;
	}

	public void setLoggedOn(Boolean loggedOn) {
		this.loggedOn = loggedOn;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Date getLogonTs() {
		return logonTs;
	}

	public void setLogonTs(Date logonTs) {
		this.logonTs = logonTs;
	}

	public String getSecurityToken() {
		return securityToken;
	}

	public void setSecurityToken(String securityToken) {
		this.securityToken = securityToken;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public List<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}
}
