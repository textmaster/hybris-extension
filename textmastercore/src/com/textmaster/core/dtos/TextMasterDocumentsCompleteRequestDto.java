package com.textmaster.core.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"documents"
})
public class TextMasterDocumentsCompleteRequestDto
{
	@JsonProperty("documents")
	private List<String> documents = null;

	public List<String> getDocuments()
	{
		return documents;
	}

	public void setDocuments(List<String> documents)
	{
		this.documents = documents;
	}
}
