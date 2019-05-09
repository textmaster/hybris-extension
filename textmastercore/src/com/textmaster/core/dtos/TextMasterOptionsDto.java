package com.textmaster.core.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "expertise",
        "language_level",
        "translation_memory"
})
public class TextMasterOptionsDto {

    @JsonProperty("expertise")
    private Boolean expertise;

    @JsonProperty("language_level")
    private String languageLevel;

    @JsonProperty("translation_memory")
    private Boolean translationMemory;

    public String getLanguageLevel() {
        return languageLevel;
    }

    public void setLanguageLevel(String languageLevel) {
        this.languageLevel = languageLevel;
    }

    public Boolean getExpertise() {
        return expertise;
    }

    public void setExpertise(Boolean expertise) {
        this.expertise = expertise;
    }

    public Boolean getTranslationMemory()
    {
        return translationMemory;
    }

    public void setTranslationMemory(Boolean translationMemory)
    {
        this.translationMemory = translationMemory;
    }
}
