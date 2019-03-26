package com.textmaster.core.services;

import com.textmaster.core.dtos.*;
import com.textmaster.core.model.TextMasterDocumentModel;

import java.util.List;
import java.util.Map;


public interface TextMasterMessageService
{
	/**
	 * Create support a message.
	 *
	 * @param document
	 * @param message
	 * @return
	 */
	public TextMasterSupportMessageResponseDto createSupportMessage(TextMasterDocumentModel document, String message);

	/**
	 * Get support a messages.
	 *
	 * @param document
	 * @return
	 */
	public TextMasterSupportMessagesResponseDto getSupportMessages(TextMasterDocumentModel document);
}
