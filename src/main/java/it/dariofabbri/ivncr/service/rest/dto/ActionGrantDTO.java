package it.dariofabbri.ivncr.service.rest.dto;


public class ActionGrantDTO {

	private String action;
	private Boolean allowed;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Boolean getAllowed() {
		return allowed;
	}

	public void setAllowed(Boolean allowed) {
		this.allowed = allowed;
	}
}
