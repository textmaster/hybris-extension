package com.textmaster.core.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"support_message"
})
public class TextMasterSupportMessageRequestDto
{
	@JsonProperty("support_message")
	private TextMasterSupportMessageDto supportMessage;

	public TextMasterSupportMessageDto getSupportMessage()
	{
		return supportMessage;
	}

	public void setSupportMessage(TextMasterSupportMessageDto supportMessage)
	{
		this.supportMessage = supportMessage;
	}
}
