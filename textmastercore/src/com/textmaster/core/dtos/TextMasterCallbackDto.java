package com.textmaster.core.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "support_message_created"
})
public class TextMasterCallbackDto {

    @JsonProperty("support_message_created")
    private TextMasterSupportMessageCreatedDto supportMessageCreated;

    public TextMasterSupportMessageCreatedDto getSupportMessageCreated() {
        return supportMessageCreated;
    }

    public void setSupportMessageCreated(TextMasterSupportMessageCreatedDto supportMessageCreated) {
        this.supportMessageCreated = supportMessageCreated;
    }
}
