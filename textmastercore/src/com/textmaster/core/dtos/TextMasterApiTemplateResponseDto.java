package com.textmaster.core.dtos;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"api_templates",
		"total_pages",
		"count",
		"page",
		"per_page",
		"previous_page",
		"next_page"
})
public class TextMasterApiTemplateResponseDto implements TextMasterPagedDto {

	@JsonProperty("api_templates")
    private List<TextMasterApiTemplateDto> apiTemplates = null;
	@JsonProperty("total_pages")
	private Integer totalPages;
	@JsonProperty("count")
	private Integer count;
	@JsonProperty("page")
	private Integer page;
	@JsonProperty("per_page")
	private Integer perPage;
	@JsonProperty("previous_page")
	private String previousPage;
	@JsonProperty("next_page")
	private String nextPage;

	public List<TextMasterApiTemplateDto> getApiTemplates() {
		return apiTemplates;
	}

	public void setApiTemplates(List<TextMasterApiTemplateDto> apiTemplates) {
		this.apiTemplates = apiTemplates;
	}

	@Override
	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	@Override
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Override
	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	@Override
	public Integer getPerPage() {
		return perPage;
	}

	public void setPerPage(Integer perPage) {
		this.perPage = perPage;
	}

	@Override
	public String getPreviousPage() {
		return previousPage;
	}

	public void setPreviousPage(String previousPage) {
		this.previousPage = previousPage;
	}

	@Override
	public String getNextPage() {
		return nextPage;
	}

	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}
}
