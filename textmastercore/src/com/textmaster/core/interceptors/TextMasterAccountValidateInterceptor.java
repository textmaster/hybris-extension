package com.textmaster.core.interceptors;

import com.textmaster.core.model.TextMasterAccountModel;
import com.textmaster.core.services.TextMasterAccountService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import org.springframework.beans.factory.annotation.Required;

public class TextMasterAccountValidateInterceptor implements ValidateInterceptor<TextMasterAccountModel> {

    private TextMasterAccountService textMasterAccountService;

    @Override
    public void onValidate(TextMasterAccountModel model, InterceptorContext context) throws InterceptorException {

        // Check if the account is valid on TextMaster platform
        if (!getTextMasterAccountService().checkAuthentication(model.getApiKey(), model.getApiSecret())) {
            throw new InterceptorException("Account does not exist on TextMaster platform");
        }
    }

    protected TextMasterAccountService getTextMasterAccountService()
    {
        return textMasterAccountService;
    }

    @Required
    public void setTextMasterAccountService(TextMasterAccountService textMasterAccountService)
    {
        this.textMasterAccountService = textMasterAccountService;
    }
}
