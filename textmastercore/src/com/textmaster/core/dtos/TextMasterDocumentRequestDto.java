package com.textmaster.core.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
        import com.fasterxml.jackson.annotation.JsonProperty;
        import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "document"
})
public class TextMasterDocumentRequestDto {

    @JsonProperty("document")
    private TextMasterDocumentDto document;

    public TextMasterDocumentDto getDocument() {
        return document;
    }

    public void setDocument(TextMasterDocumentDto document) {
        this.document = document;
    }
}
