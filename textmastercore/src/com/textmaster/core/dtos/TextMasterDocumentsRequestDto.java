package com.textmaster.core.dtos;

import java.util.List;
        import com.fasterxml.jackson.annotation.JsonInclude;
        import com.fasterxml.jackson.annotation.JsonProperty;
        import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "documents"
})
public class TextMasterDocumentsRequestDto {

    @JsonProperty("documents")
    private List<TextMasterDocumentDto> documents = null;

    public List<TextMasterDocumentDto> getDocuments() {
        return documents;
    }

    public void setDocuments(List<TextMasterDocumentDto> documents) {
        this.documents = documents;
    }
}