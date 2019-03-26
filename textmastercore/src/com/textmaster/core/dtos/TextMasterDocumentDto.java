package com.textmaster.core.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "status",
        "skip_copyscape",
        "title",
        "instructions",
        "word_count",
        "word_count_rule",
        "keywords_repeat_count",
        "deliver_work_as_file",
        "custom_data",
        "plagiarism_analysis",
        "written_words",
        "type",
        "id",
        "project_id",
        "callback",
        "reference",
        "ctype",
        "keyword_list",
        "satisfaction",
        "completion",
        "can_post_message_to_author",
        "author_work",
        "author_id",
        "author_rating",
        "original_content",
        "created_at",
        "updated_at",
        "completed_at",
        "language_from",
        "language_to",
        "perform_word_count",
        "markup_in_content"
})
public class TextMasterDocumentDto {

    @JsonProperty("status")
    private String status;
    @JsonProperty("skip_copyscape")
    private Boolean skipCopyscape;
    @JsonProperty("title")
    private String title;
    @JsonProperty("instructions")
    private String instructions;
    @JsonProperty("word_count")
    private Integer wordCount;
    @JsonProperty("word_count_rule")
    private Integer wordCountRule;
    @JsonProperty("keywords_repeat_count")
    private Integer keywordsRepeatCount;
    @JsonProperty("deliver_work_as_file")
    private Boolean deliverWorkAsFile;
    @JsonProperty("custom_data")
    private TextMasterCustomDataDto customData;
    @JsonProperty("plagiarism_analysis")
    private TextMasterPlagiarismAnalysisDto plagiarismAnalysis;
    @JsonProperty("written_words")
    private Integer writtenWords;
    @JsonProperty("type")
    private String type;
    @JsonProperty("id")
    private String id;
    @JsonProperty("project_id")
    private String projectId;
    @JsonProperty("callback")
    private TextMasterCallbackDto callback;
    @JsonProperty("reference")
    private String reference;
    @JsonProperty("ctype")
    private String ctype;
    @JsonProperty("keyword_list")
    private String keywordList;
    @JsonProperty("satisfaction")
    private Object satisfaction;
    @JsonProperty("completion")
    private Integer completion;
    @JsonProperty("can_post_message_to_author")
    private Boolean canPostMessageToAuthor;
    @JsonProperty("author_work")
    private Map<String, String> authorWork;
    @JsonProperty("author_id")
    private Object authorId;
    @JsonProperty("author_rating")
    private Integer authorRating;
    @JsonProperty("original_content")
    private Map<Object, Object> originalContent;
    @JsonProperty("created_at")
    private TextMasterDateDto createdAt;
    @JsonProperty("updated_at")
    private TextMasterDateDto updatedAt;
    @JsonProperty("completed_at")
    private TextMasterDateDto completedAt;
    @JsonProperty("language_from")
    private String languageFrom;
    @JsonProperty("language_to")
    private String languageTo;
    @JsonProperty("perform_word_count")
    private Boolean performWordCount;
    @JsonProperty("markup_in_content")
    private Boolean markupInContent;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getSkipCopyscape() {
        return skipCopyscape;
    }

    public void setSkipCopyscape(Boolean skipCopyscape) {
        this.skipCopyscape = skipCopyscape;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Integer getWordCount() {
        return wordCount;
    }

    public void setWordCount(Integer wordCount) {
        this.wordCount = wordCount;
    }

    public Integer getWordCountRule() {
        return wordCountRule;
    }

    public void setWordCountRule(Integer wordCountRule) {
        this.wordCountRule = wordCountRule;
    }

    public Integer getKeywordsRepeatCount() {
        return keywordsRepeatCount;
    }

    public void setKeywordsRepeatCount(Integer keywordsRepeatCount) {
        this.keywordsRepeatCount = keywordsRepeatCount;
    }

    public Boolean getDeliverWorkAsFile() {
        return deliverWorkAsFile;
    }

    public void setDeliverWorkAsFile(Boolean deliverWorkAsFile) {
        this.deliverWorkAsFile = deliverWorkAsFile;
    }

    public TextMasterCustomDataDto getCustomData() {
        return customData;
    }

    public void setCustomData(TextMasterCustomDataDto customData) {
        this.customData = customData;
    }

    public TextMasterPlagiarismAnalysisDto getPlagiarismAnalysis() {
        return plagiarismAnalysis;
    }

    public void setPlagiarismAnalysis(TextMasterPlagiarismAnalysisDto plagiarismAnalysis) {
        this.plagiarismAnalysis = plagiarismAnalysis;
    }

    public Integer getWrittenWords() {
        return writtenWords;
    }

    public void setWrittenWords(Integer writtenWords) {
        this.writtenWords = writtenWords;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public TextMasterCallbackDto getCallback() {
        return callback;
    }

    public void setCallback(TextMasterCallbackDto callback) {
        this.callback = callback;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getCtype() {
        return ctype;
    }

    public void setCtype(String ctype) {
        this.ctype = ctype;
    }

    public String getKeywordList() {
        return keywordList;
    }

    public void setKeywordList(String keywordList) {
        this.keywordList = keywordList;
    }

    public Object getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(Object satisfaction) {
        this.satisfaction = satisfaction;
    }

    public Integer getCompletion() {
        return completion;
    }

    public void setCompletion(Integer completion) {
        this.completion = completion;
    }

    public Boolean getCanPostMessageToAuthor() {
        return canPostMessageToAuthor;
    }

    public void setCanPostMessageToAuthor(Boolean canPostMessageToAuthor) {
        this.canPostMessageToAuthor = canPostMessageToAuthor;
    }

    public Map<String, String> getAuthorWork() {
        return authorWork;
    }

    public void setAuthorWork(Map<String, String> authorWork) {
        this.authorWork = authorWork;
    }

    public Object getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Object authorId) {
        this.authorId = authorId;
    }

    public Integer getAuthorRating() {
        return authorRating;
    }

    public void setAuthorRating(Integer authorRating) {
        this.authorRating = authorRating;
    }

    public Map<Object, Object> getOriginalContent() {
        return originalContent;
    }

    public void setOriginalContent(Map<Object, Object> originalContent) {
        this.originalContent = originalContent;
    }

    public TextMasterDateDto getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(TextMasterDateDto createdAt) {
        this.createdAt = createdAt;
    }

    public TextMasterDateDto getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(TextMasterDateDto updatedAt) {
        this.updatedAt = updatedAt;
    }

    public TextMasterDateDto getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(TextMasterDateDto completedAt) {
        this.completedAt = completedAt;
    }

    public String getLanguageFrom() {
        return languageFrom;
    }

    public void setLanguageFrom(String languageFrom) {
        this.languageFrom = languageFrom;
    }

    public String getLanguageTo() {
        return languageTo;
    }

    public void setLanguageTo(String languageTo) {
        this.languageTo = languageTo;
    }

    public Boolean getPerformWordCount() {
        return performWordCount;
    }

    public void setPerformWordCount(Boolean performWordCount) {
        this.performWordCount = performWordCount;
    }

    public Boolean getMarkupInContent() {
        return markupInContent;
    }

    public void setMarkupInContent(Boolean markupInContent) {
        this.markupInContent = markupInContent;
    }
}
