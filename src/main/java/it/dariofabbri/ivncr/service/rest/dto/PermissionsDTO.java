package it.dariofabbri.ivncr.service.rest.dto;

import java.util.ArrayList;
import java.util.List;

public class PermissionsDTO extends BaseQueryResultDTO {

	private List<PermissionDTO> results = new ArrayList<PermissionDTO>();

	public List<PermissionDTO> getResults() {
		return results;
	}

	public void setResults(List<PermissionDTO> results) {
		this.results = results;
	}
}
