package com.textmaster.core.populators.reverse;

import com.textmaster.core.dtos.TextMasterSupportMessageDto;
import com.textmaster.core.model.TextMasterSupportMessageModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import org.apache.log4j.Logger;


public class TextMasterSupportMessageReversePopulator
		implements Populator<TextMasterSupportMessageDto, TextMasterSupportMessageModel>
{

	private static final Logger LOG = Logger.getLogger(TextMasterSupportMessageReversePopulator.class);

	@Override
	public void populate(TextMasterSupportMessageDto source, TextMasterSupportMessageModel target) throws ConversionException
	{
		target.setAuthorId(source.getAuthorId());
		target.setAuthorReference(source.getAuthorRef());
		target.setDate(source.getCreatedAt().getFull());
		target.setHybrisOwner(source.getWrittenByYou());
		target.setMessage(source.getContent());
	}
}
