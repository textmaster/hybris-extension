package com.textmaster.core.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"support_messages"
})
public class TextMasterSupportMessagesResponseDto
{

	@JsonProperty("support_messages")
	private List<TextMasterSupportMessageDto> supportMessages = null;

	public List<TextMasterSupportMessageDto> getSupportMessages()
	{
		return supportMessages;
	}

	public void setSupportMessages(List<TextMasterSupportMessageDto> supportMessages)
	{
		this.supportMessages = supportMessages;
	}
}
