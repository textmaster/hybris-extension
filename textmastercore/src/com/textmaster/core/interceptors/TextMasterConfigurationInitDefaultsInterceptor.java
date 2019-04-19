package com.textmaster.core.interceptors;

import com.textmaster.core.model.TextMasterConfigurationModel;
import de.hybris.platform.servicelayer.interceptor.InitDefaultsInterceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import org.springframework.beans.factory.annotation.Required;


public class TextMasterConfigurationInitDefaultsInterceptor implements InitDefaultsInterceptor<TextMasterConfigurationModel>
{

    private PersistentKeyGenerator codeGenerator;

    @Override
    public void onInitDefaults(final TextMasterConfigurationModel model, final InterceptorContext context) throws InterceptorException {
        model.setCode((String) getCodeGenerator().generate());
    }

    protected PersistentKeyGenerator getCodeGenerator() {
        return codeGenerator;
    }

    @Required
    public void setCodeGenerator(final PersistentKeyGenerator codeGenerator) {
        this.codeGenerator = codeGenerator;
    }
}
