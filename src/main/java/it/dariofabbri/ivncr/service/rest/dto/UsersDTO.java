package it.dariofabbri.ivncr.service.rest.dto;

import java.util.List;

public class UsersDTO {

	private Integer offset;
	private Integer limit;
	private Integer records;

	private List<UserDTO> results;

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getRecords() {
		return records;
	}

	public void setRecords(Integer records) {
		this.records = records;
	}

	public List<UserDTO> getResults() {
		return results;
	}

	public void setResults(List<UserDTO> results) {
		this.results = results;
	}
}
