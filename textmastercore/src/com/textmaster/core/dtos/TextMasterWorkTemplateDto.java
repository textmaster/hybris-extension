package com.textmaster.core.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "description",
        "image_preview_path",
        "ctype"
})
public class TextMasterWorkTemplateDto {

    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private Object description;
    @JsonProperty("image_preview_path")
    private Object imagePreviewPath;
    @JsonProperty("ctype")
    private String ctype;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public Object getImagePreviewPath() {
        return imagePreviewPath;
    }

    public void setImagePreviewPath(Object imagePreviewPath) {
        this.imagePreviewPath = imagePreviewPath;
    }

    public String getCtype() {
        return ctype;
    }

    public void setCtype(String ctype) {
        this.ctype = ctype;
    }
}
