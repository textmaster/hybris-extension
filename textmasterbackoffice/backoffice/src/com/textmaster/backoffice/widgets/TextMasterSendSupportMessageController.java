/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package com.textmaster.backoffice.widgets;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.textmaster.core.dtos.TextMasterSupportMessageResponseDto;
import com.textmaster.core.enums.TextMasterDocumentStatusEnum;
import com.textmaster.core.model.TextMasterAccountModel;
import com.textmaster.core.model.TextMasterDocumentModel;
import com.textmaster.core.model.TextMasterProjectModel;
import com.textmaster.core.model.TextMasterSupportMessageModel;
import com.textmaster.core.services.TextMasterMessageService;
import de.hybris.platform.servicelayer.model.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.Map;


public class TextMasterSendSupportMessageController extends DefaultWidgetController
{
	public static final Logger LOG = LoggerFactory.getLogger(TextMasterSendSupportMessageController.class);

	@WireVariable
	private ModelService modelService;

	private TextMasterMessageService textMasterMessageService;

	private TextMasterAccountModel account;
	private TextMasterProjectModel project;
	private TextMasterDocumentModel document;

	@Override
	public void initialize(final Component comp)
	{
		super.initialize(comp);
	}

	@SocketEvent(socketId = "account")
	public void account(final TextMasterAccountModel data)
	{
		this.account = data;
	}

	@SocketEvent(socketId = "project")
	public void project(final TextMasterProjectModel data)
	{
		this.project = data;
	}

	@SocketEvent(socketId = "document")
	public void document(final TextMasterDocumentModel data)
	{
		this.document = data;
	}

	@SocketEvent(socketId = "wizardResult")
	public void wizardResult(final Map<String, Object> data)
	{
		if (document == null)
		{
			return;
		}

		TextMasterSupportMessageModel message = (TextMasterSupportMessageModel) data.get("newSupportMessage");

		// Change document status to incomplete
		document.setStatus(TextMasterDocumentStatusEnum.INCOMPLETE);
		getModelService().save(document);

		// Create message on TextMaster platform
		TextMasterSupportMessageResponseDto responseSupportMessage = getTextMasterMessageService()
				.createSupportMessage(document, message.getMessage());
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected TextMasterMessageService getTextMasterMessageService()
	{
		return textMasterMessageService;
	}

	@Required
	public void setTextMasterMessageService(TextMasterMessageService textMasterMessageService)
	{
		this.textMasterMessageService = textMasterMessageService;
	}
}
