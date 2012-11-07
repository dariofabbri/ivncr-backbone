package it.dariofabbri.ivncr.service.rest.dto;

import java.util.List;

public class RolesDTO extends BaseQueryResultDTO {

	private List<RoleDTO> results;

	public List<RoleDTO> getResults() {
		return results;
	}

	public void setResults(List<RoleDTO> results) {
		this.results = results;
	}
}
