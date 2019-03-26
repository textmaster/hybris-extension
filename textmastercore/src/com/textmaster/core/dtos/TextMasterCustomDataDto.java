package com.textmaster.core.dtos;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "tags",
        "external_client_id"
})
public class TextMasterCustomDataDto {

    @JsonProperty("tags")
    private List<String> tags = null;
    @JsonProperty("external_client_id")
    private String externalClientId;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getExternalClientId() {
        return externalClientId;
    }

    public void setExternalClientId(String externalClientId) {
        this.externalClientId = externalClientId;
    }
}
