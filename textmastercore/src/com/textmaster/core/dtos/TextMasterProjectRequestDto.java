package com.textmaster.core.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
        import com.fasterxml.jackson.annotation.JsonProperty;
        import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "project"
})
public class TextMasterProjectRequestDto {

    @JsonProperty("project")
    private TextMasterProjectDto project;

    public TextMasterProjectDto getProject() {
        return project;
    }

    public void setProject(TextMasterProjectDto project) {
        this.project = project;
    }
}