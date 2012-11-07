package it.dariofabbri.ivncr.service.rest.dto;

import java.util.List;

public class ContactsDTO extends BaseQueryResultDTO {

	private List<ContactDTO> results;

	public List<ContactDTO> getResults() {
		return results;
	}

	public void setResults(List<ContactDTO> results) {
		this.results = results;
	}
}
