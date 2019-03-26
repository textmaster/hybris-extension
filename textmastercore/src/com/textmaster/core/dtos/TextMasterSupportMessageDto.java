package com.textmaster.core.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"content",
		"message",
		"author_id",
		"written_by_you",
		"written_by_author",
		"author_ref",
		"created_at"
})
public class TextMasterSupportMessageDto
{
	@JsonProperty("content")
	private String content;
	@JsonProperty("message")
	private String message;
	@JsonProperty("author_id")
	private String authorId;
	@JsonProperty("written_by_you")
	private Boolean writtenByYou;
	@JsonProperty("written_by_author")
	private Boolean writtenByAuthor;
	@JsonProperty("author_ref")
	private String authorRef;
	@JsonProperty("created_at")
	private TextMasterDateDto createdAt;

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getAuthorId()
	{
		return authorId;
	}

	public void setAuthorId(String authorId)
	{
		this.authorId = authorId;
	}

	public Boolean getWrittenByYou()
	{
		return writtenByYou;
	}

	public void setWrittenByYou(Boolean writtenByYou)
	{
		this.writtenByYou = writtenByYou;
	}

	public Boolean getWrittenByAuthor()
	{
		return writtenByAuthor;
	}

	public void setWrittenByAuthor(Boolean writtenByAuthor)
	{
		this.writtenByAuthor = writtenByAuthor;
	}

	public String getAuthorRef()
	{
		return authorRef;
	}

	public void setAuthorRef(String authorRef)
	{
		this.authorRef = authorRef;
	}

	public TextMasterDateDto getCreatedAt()
	{
		return createdAt;
	}

	public void setCreatedAt(TextMasterDateDto createdAt)
	{
		this.createdAt = createdAt;
	}
}
