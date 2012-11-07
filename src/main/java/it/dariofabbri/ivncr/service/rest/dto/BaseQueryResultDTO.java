package it.dariofabbri.ivncr.service.rest.dto;


public abstract class BaseQueryResultDTO {

	private Integer offset;
	private Integer limit;
	private Integer records;

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
}