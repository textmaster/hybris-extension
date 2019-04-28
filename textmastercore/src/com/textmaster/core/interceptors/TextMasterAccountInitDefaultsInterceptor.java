package com.textmaster.core.interceptors;

import com.textmaster.core.model.TextMasterAccountModel;
import de.hybris.platform.servicelayer.interceptor.InitDefaultsInterceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import org.springframework.beans.factory.annotation.Required;

public class TextMasterAccountInitDefaultsInterceptor implements InitDefaultsInterceptor<TextMasterAccountModel> {

    private PersistentKeyGenerator codeGenerator;

    @Override
    public void onInitDefaults(TextMasterAccountModel model, InterceptorContext interceptorContext) {
        model.setCode((String) getCodeGenerator().generate());
    }

    protected PersistentKeyGenerator getCodeGenerator() {
        return codeGenerator;
    }

    @Required
    public void setCodeGenerator(PersistentKeyGenerator codeGenerator) {
        this.codeGenerator = codeGenerator;
    }
}
