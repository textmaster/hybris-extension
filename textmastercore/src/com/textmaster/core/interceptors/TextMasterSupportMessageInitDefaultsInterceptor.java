package com.textmaster.core.interceptors;

import com.textmaster.core.model.TextMasterSupportMessageModel;
import de.hybris.platform.servicelayer.interceptor.InitDefaultsInterceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import org.springframework.beans.factory.annotation.Required;


public class TextMasterSupportMessageInitDefaultsInterceptor implements InitDefaultsInterceptor<TextMasterSupportMessageModel>
{
	private PersistentKeyGenerator codeGenerator;

	@Override
	public void onInitDefaults(TextMasterSupportMessageModel model, InterceptorContext context) throws InterceptorException
	{
		model.setCode((String) getCodeGenerator().generate());
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
