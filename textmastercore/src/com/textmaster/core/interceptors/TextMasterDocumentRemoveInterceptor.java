package com.textmaster.core.interceptors;

import com.textmaster.core.enums.TextMasterProjectStatusEnum;
import com.textmaster.core.model.TextMasterDocumentModel;
import com.textmaster.core.model.TextMasterProjectModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;


public class TextMasterDocumentRemoveInterceptor implements RemoveInterceptor<TextMasterDocumentModel>
{
	@Override
	public void onRemove(TextMasterDocumentModel document, InterceptorContext interceptorContext) throws InterceptorException
	{
		// Prevent the project to be removed according to project status
		if (!TextMasterProjectStatusEnum.IN_CREATION.equals(document.getProject().getStatus())) {
			throw new InterceptorException("Impossible to remove document whose project status is not IN_CREATION");
		}
	}
}
