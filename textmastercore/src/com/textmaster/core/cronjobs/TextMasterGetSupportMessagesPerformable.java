package com.textmaster.core.cronjobs;

import com.textmaster.core.dtos.TextMasterSupportMessageDto;
import com.textmaster.core.dtos.TextMasterSupportMessagesResponseDto;
import com.textmaster.core.enums.TextMasterDocumentStatusEnum;
import com.textmaster.core.model.TextMasterDocumentModel;
import com.textmaster.core.model.TextMasterSupportMessageModel;
import com.textmaster.core.services.TextMasterDocumentService;
import com.textmaster.core.services.TextMasterMessageService;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Get support messages to import on hybris platform.
 */
public class TextMasterGetSupportMessagesPerformable extends AbstractJobPerformable<CronJobModel>
{
	private final static Logger LOG = LoggerFactory.getLogger(TextMasterGetSupportMessagesPerformable.class);

	private TextMasterDocumentService textMasterDocumentService;
	private Converter<TextMasterSupportMessageDto, TextMasterSupportMessageModel> textMasterSupportMessageReverseConverter;
	private TextMasterMessageService textMasterMessageService;

	@Override
	@Transactional

	public PerformResult perform(CronJobModel cronJobModel)
	{
		List<TextMasterDocumentModel> documents = getTextMasterDocumentService()
				.getDocumentsForStatuses(Collections.singletonList(TextMasterDocumentStatusEnum.INCOMPLETE));

		if (CollectionUtils.isNotEmpty(documents))
		{
			documents.stream()
					.forEach(d -> {

						// Get all messages for this document
						TextMasterSupportMessagesResponseDto messages = getTextMasterMessageService().getSupportMessages(d);

						// Replace local messages: with partof, the current will be automatically deleted
						List<TextMasterSupportMessageModel> supportMessages = new ArrayList<>();
						if (CollectionUtils.isNotEmpty(messages.getSupportMessages()))
						{
							supportMessages = messages.getSupportMessages().stream()
									.map(dto -> getTextMasterSupportMessageReverseConverter().convert(dto))
									.collect(Collectors.toList());
						}
						d.setSupportMessages(supportMessages);
					});
			getModelService().saveAll(documents);
		}

		// TODO: Performance problem ?

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected TextMasterDocumentService getTextMasterDocumentService()
	{
		return textMasterDocumentService;
	}

	@Required
	public void setTextMasterDocumentService(TextMasterDocumentService textMasterDocumentService)
	{
		this.textMasterDocumentService = textMasterDocumentService;
	}

	protected Converter<TextMasterSupportMessageDto, TextMasterSupportMessageModel> getTextMasterSupportMessageReverseConverter()
	{
		return textMasterSupportMessageReverseConverter;
	}

	@Required
	public void setTextMasterSupportMessageReverseConverter(
			Converter<TextMasterSupportMessageDto, TextMasterSupportMessageModel> textMasterSupportMessageReverseConverter)
	{
		this.textMasterSupportMessageReverseConverter = textMasterSupportMessageReverseConverter;
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
