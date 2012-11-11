package it.dariofabbri.ivncr.service.rest.dto;

public class PermissionDTO {

	private Integer id;
	private String permissionString;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPermissionString() {
		return permissionString;
	}

	public void setPermissionString(String permissionString) {
		this.permissionString = permissionString;
	}
}
