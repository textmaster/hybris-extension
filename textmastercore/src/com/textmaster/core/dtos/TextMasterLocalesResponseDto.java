package com.textmaster.core.dtos;

import java.util.List;
        import com.fasterxml.jackson.annotation.JsonInclude;
        import com.fasterxml.jackson.annotation.JsonProperty;
        import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "locales"
})
public class TextMasterLocalesResponseDto {

    @JsonProperty("locales")
    private List<TextMasterLocaleDto> locales = null;

    public List<TextMasterLocaleDto> getLocales() {
        return locales;
    }

    public void setLocales(List<TextMasterLocaleDto> locales) {
        this.locales = locales;
    }
}
