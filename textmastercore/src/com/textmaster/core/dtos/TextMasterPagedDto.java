package com.textmaster.core.dtos;

/**
 * Define contract for paged DTO.
 */
public interface TextMasterPagedDto
{
	public Integer getTotalPages();

	public Integer getCount();

	public Integer getPage();

	public Integer getPerPage();

	public String getPreviousPage();

	public String getNextPage();
}
