package com.textmaster.core.interceptors;

import com.textmaster.core.model.TextMasterAccountModel;
import com.textmaster.core.model.TextMasterProjectModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import org.springframework.beans.factory.annotation.Required;

public class TextMasterProjectPrepareInterceptor implements PrepareInterceptor<TextMasterProjectModel> {

    private PersistentKeyGenerator codeGenerator;

    @Override
    public void onPrepare(TextMasterProjectModel model, InterceptorContext context) throws InterceptorException {

        // Define auto generated code for new item
        if (context.isNew(model)) {
            model.setCode((String) getCodeGenerator().generate());
        }
    }

    protected PersistentKeyGenerator getCodeGenerator() {
        return codeGenerator;
    }

    @Required
    public void setCodeGenerator(PersistentKeyGenerator codeGenerator) {
        this.codeGenerator = codeGenerator;
    }
}
