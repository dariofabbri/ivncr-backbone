package it.dariofabbri.ivncr.service.rest.dto;

import java.util.ArrayList;
import java.util.List;

public class UsersDTO extends BaseQueryResultDTO {

	private List<UserDTO> results = new ArrayList<UserDTO>();

	public List<UserDTO> getResults() {
		return results;
	}

	public void setResults(List<UserDTO> results) {
		this.results = results;
	}
}
