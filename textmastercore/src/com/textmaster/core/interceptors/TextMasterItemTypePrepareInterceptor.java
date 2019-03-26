package com.textmaster.core.interceptors;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;

import org.springframework.beans.factory.annotation.Required;

import com.textmaster.core.model.TextMasterItemTypeModel;


public class TextMasterItemTypePrepareInterceptor implements PrepareInterceptor<TextMasterItemTypeModel>
{

	private PersistentKeyGenerator codeGenerator;

	@Override
	public void onPrepare(final TextMasterItemTypeModel model, final InterceptorContext context) throws InterceptorException
	{

		// Define auto generated code for new item
		if (context.isNew(model))
		{
			model.setCode((String) getCodeGenerator().generate());
		}
	}

	protected PersistentKeyGenerator getCodeGenerator()
	{
		return codeGenerator;
	}

	@Required
	public void setCodeGenerator(final PersistentKeyGenerator codeGenerator)
	{
		this.codeGenerator = codeGenerator;
	}
}