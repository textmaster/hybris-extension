package com.textmaster.core.interceptors;

import com.textmaster.core.model.TextMasterSupportMessageModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import org.apache.solr.common.StringUtils;
import org.springframework.beans.factory.annotation.Required;


public class TextMasterSupportMessagePrepareInterceptor implements PrepareInterceptor<TextMasterSupportMessageModel>
{
	private PersistentKeyGenerator codeGenerator;

	@Override
	public void onPrepare(TextMasterSupportMessageModel model, InterceptorContext context) throws InterceptorException
	{
		if (StringUtils.isEmpty(model.getCode()))
		{
			model.setCode((String) getCodeGenerator().generate());
		}
	}

	protected PersistentKeyGenerator getCodeGenerator()
	{
		return codeGenerator;
	}

	@Required
	public void setCodeGenerator(PersistentKeyGenerator codeGenerator)
	{
		this.codeGenerator = codeGenerator;
	}
}
