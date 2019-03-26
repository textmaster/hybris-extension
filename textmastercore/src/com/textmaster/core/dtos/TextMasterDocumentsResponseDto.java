package com.textmaster.core.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"documents"
})
public class TextMasterDocumentsResponseDto implements TextMasterPagedDto
{
	@JsonProperty("documents")
	private List<TextMasterDocumentDto> documents = null;

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

	public List<TextMasterDocumentDto> getDocuments()
	{
		return documents;
	}

	public void setDocuments(List<TextMasterDocumentDto> documents)
	{
		this.documents = documents;
	}

	@Override
	public Integer getTotalPages()
	{
		return totalPages;
	}

	public void setTotalPages(Integer totalPages)
	{
		this.totalPages = totalPages;
	}

	@Override
	public Integer getCount()
	{
		return count;
	}

	public void setCount(Integer count)
	{
		this.count = count;
	}

	@Override
	public Integer getPage()
	{
		return page;
	}

	public void setPage(Integer page)
	{
		this.page = page;
	}

	@Override
	public Integer getPerPage()
	{
		return perPage;
	}

	public void setPerPage(Integer perPage)
	{
		this.perPage = perPage;
	}

	@Override
	public String getPreviousPage()
	{
		return previousPage;
	}

	public void setPreviousPage(String previousPage)
	{
		this.previousPage = previousPage;
	}

	@Override
	public String getNextPage()
	{
		return nextPage;
	}

	public void setNextPage(String nextPage)
	{
		this.nextPage = nextPage;
	}
}
