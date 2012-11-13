package it.dariofabbri.ivncr.service.rest.dto;

import java.util.ArrayList;
import java.util.List;

public class RolesDTO extends BaseQueryResultDTO {

	private List<RoleDTO> results = new ArrayList<RoleDTO>();

	public List<RoleDTO> getResults() {
		return results;
	}

	public void setResults(List<RoleDTO> results) {
		this.results = results;
	}
}
