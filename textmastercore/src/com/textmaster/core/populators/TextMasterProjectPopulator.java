package com.textmaster.core.populators;

import com.textmaster.core.dtos.TextMasterProjectDto;
import com.textmaster.core.model.TextMasterProjectModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class TextMasterProjectPopulator implements Populator<TextMasterProjectModel, TextMasterProjectDto> {

    @Override
    public void populate(TextMasterProjectModel source, TextMasterProjectDto target) throws ConversionException {
        target.setApiTemplateId(source.getTemplateId());
        target.setName(source.getName());
    }
}
