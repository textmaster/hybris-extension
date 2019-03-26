package com.textmaster.core.services.impl;

import com.textmaster.core.dtos.TextMasterSupportMessageDto;
import com.textmaster.core.dtos.TextMasterSupportMessageRequestDto;
import com.textmaster.core.dtos.TextMasterSupportMessageResponseDto;
import com.textmaster.core.dtos.TextMasterSupportMessagesResponseDto;
import com.textmaster.core.model.TextMasterDocumentModel;
import com.textmaster.core.services.TextMasterApiService;
import com.textmaster.core.services.TextMasterMessageService;
import org.springframework.beans.factory.annotation.Required;


public class DefaultTextMasterMessageService implements TextMasterMessageService
{
	private TextMasterApiService textMasterApiService;

	/**
	 *{@inheritDoc}
	 */
	@Override
	public TextMasterSupportMessageResponseDto createSupportMessage(TextMasterDocumentModel document, String message)
	{
		// Prepare request
		TextMasterSupportMessageDto supportMessage = new TextMasterSupportMessageDto();
		supportMessage.setMessage(message);
		TextMasterSupportMessageRequestDto requestSupportMessage = new TextMasterSupportMessageRequestDto();
		requestSupportMessage.setSupportMessage(supportMessage);

		// Create message on TextMaster platform
		return getTextMasterApiService()
				.createSupportMessage(document.getProject().getAccount().getApiKey(), document.getProject().getAccount().getApiSecret(), document.getProject().getRemoteId(),
						document.getRemoteId(), requestSupportMessage);
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public TextMasterSupportMessagesResponseDto getSupportMessages(TextMasterDocumentModel document)
	{
		return getTextMasterApiService().getSupportMessages(
				document.getProject().getAccount().getApiKey(),
				document.getProject().getAccount().getApiSecret(),
				document.getProject().getRemoteId(),
				document.getRemoteId());
	}

	protected TextMasterApiService getTextMasterApiService()
	{
		return textMasterApiService;
	}

	@Required
	public void setTextMasterApiService(TextMasterApiService textMasterApiService)
	{
		this.textMasterApiService = textMasterApiService;
	}
}
