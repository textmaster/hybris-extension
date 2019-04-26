package com.textmaster.core.interceptors;

import com.textmaster.core.enums.TextMasterProjectStatusEnum;
import com.textmaster.core.model.TextMasterProjectModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import org.apache.solr.common.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


public class TextMasterProjectRemoveInterceptor implements RemoveInterceptor<TextMasterProjectModel>
{
	@Override
	public void onRemove(TextMasterProjectModel project, InterceptorContext interceptorContext) throws InterceptorException
	{
		// Prevent the project to be removed according to project status
		if (!TextMasterProjectStatusEnum.IN_CREATION.equals(project.getStatus())) {
			throw new InterceptorException("Impossible to remove project whose status is not IN_CREATION");
		}
	}
}
