package com.textmaster.core.populators;

import com.textmaster.core.constants.TextmastercoreConstants;
import com.textmaster.core.dtos.TextMasterCustomDataDto;
import com.textmaster.core.dtos.TextMasterDocumentDto;
import com.textmaster.core.model.TextMasterDocumentModel;
import com.textmaster.core.services.TextMasterDocumentService;
import com.textmaster.core.strategies.TextMasterDocumentNameStrategy;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class TextMasterDocumentPopulator implements Populator<TextMasterDocumentModel, TextMasterDocumentDto>
{

	private static final Logger LOG = Logger.getLogger(TextMasterDocumentPopulator.class);

	private ModelService modelService;
	private CommonI18NService commonI18NService;
	private Map<Class, TextMasterDocumentNameStrategy> textMasterTypeNameStrategies;
	private TextMasterDocumentService textMasterDocumentService;

	@Override
	public void populate(TextMasterDocumentModel source, TextMasterDocumentDto target) throws ConversionException
	{

		// Prepare document title according to type
		String title = source.getCode();
		Class rootType = getTextMasterDocumentService().getRootClassFromDocument(source);
		TextMasterDocumentNameStrategy nameStrategy = getTextMasterTypeNameStrategies().get(rootType);
		if (nameStrategy != null)
		{
			title = nameStrategy.translate(source);
		}

		target.setTitle(title);
		target.setType(TextmastercoreConstants.Parameters.DocumentType.KEY_VALUE);
		target.setDeliverWorkAsFile(false);
		target.setPerformWordCount(true);
		target.setMarkupInContent(true);

		// Get locale source to send correct content
		Locale locale = getCommonI18NService().getLocaleForLanguage(source.getProject().getLanguageSource().getLanguage());

		// Load attributes into MAP
		Map<Object, Object> fields = new HashMap<>();
		source.getProject().getAttributes().stream()
				.filter(a -> filter(source, a, locale))
				.forEach(a -> fields
						.put(a.getQualifier(), "" + getModelService().getAttributeValue(source.getItem(), a.getQualifier(), locale)));
		target.setOriginalContent(fields);

		// Add PK of item
		TextMasterCustomDataDto customData = new TextMasterCustomDataDto();
		customData.setExternalClientId(source.getPk().getLongValueAsString());
		target.setCustomData(customData);
	}

	/**
	 * Filter attribute checking if a value exists.
	 *
	 * @param source
	 * @param attribute
	 * @param locale
	 * @return
	 */
	protected boolean filter(TextMasterDocumentModel source, AttributeDescriptorModel attribute, Locale locale)
	{
		Object value = getModelService().getAttributeValue(source.getItem(), attribute.getQualifier(), locale);

		return StringUtils.isNotBlank((String) value);

		//        if (value == null) {
		//            return false;
		//        }
		//
		//        if (value instanceof Map && MapUtils.isEmpty((Map) value)) {
		//            return false;
		//        }
		//
		//        if (value instanceof Collection && CollectionUtils.isEmpty((Collection) value)) {
		//            return false;
		//        }
		//
		//        if (value instanceof String && StringUtils.isBlank((String)value)) {
		//            return false;
		//        }
		//		return true;
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

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	protected Map<Class, TextMasterDocumentNameStrategy> getTextMasterTypeNameStrategies()
	{
		return textMasterTypeNameStrategies;
	}

	@Required
	public void setTextMasterTypeNameStrategies(Map<Class, TextMasterDocumentNameStrategy> textMasterTypeNameStrategies)
	{
		this.textMasterTypeNameStrategies = textMasterTypeNameStrategies;
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
}
