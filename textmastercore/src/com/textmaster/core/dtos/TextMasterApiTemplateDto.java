package com.textmaster.core.dtos;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "name",
        "description",
        "level_name",
        "activity_name",
        "language_from",
        "language_to",
        "project_briefing",
        "options",
        "textmasters",
        "cost_per_word",
        "cost_per_word_in_currency",
        "same_author_must_do_entire_project",
        "auto_launch",
        "created_at",
        "updated_at"
})
public class TextMasterApiTemplateDto {

    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("level_name")
    private String levelName;
    @JsonProperty("activity_name")
    private String activityName;
    @JsonProperty("language_from")
    private String languageFrom;
    @JsonProperty("language_to")
    private String languageTo;
    @JsonProperty("project_briefing")
    private String projectBriefing;
    @JsonProperty("options")
    private TextMasterOptionsDto options;
    @JsonProperty("textmasters")
    private List<Object> textmasters = null;
    @JsonProperty("cost_per_word")
    private TextMasterCostPerWordDto costPerWord;
    @JsonProperty("cost_per_word_in_currency")
    private TextMasterCostPerWordInCurrencyDto costPerWordInCurrency;
    @JsonProperty("same_author_must_do_entire_project")
    private Boolean sameAuthorMustDoEntireProject;
    @JsonProperty("auto_launch")
    private Boolean autoLaunch;
    @JsonProperty("created_at")
    private TextMasterDateDto createdAt;
    @JsonProperty("updated_at")
    private TextMasterDateDto updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
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

    public String getProjectBriefing() {
        return projectBriefing;
    }

    public void setProjectBriefing(String projectBriefing) {
        this.projectBriefing = projectBriefing;
    }

    public TextMasterOptionsDto getOptions() {
        return options;
    }

    public void setOptions(TextMasterOptionsDto options) {
        this.options = options;
    }

    public List<Object> getTextmasters() {
        return textmasters;
    }

    public void setTextmasters(List<Object> textmasters) {
        this.textmasters = textmasters;
    }

    public TextMasterCostPerWordDto getCostPerWord() {
        return costPerWord;
    }

    public void setCostPerWord(TextMasterCostPerWordDto costPerWord) {
        this.costPerWord = costPerWord;
    }

    public TextMasterCostPerWordInCurrencyDto getCostPerWordInCurrency() {
        return costPerWordInCurrency;
    }

    public void setCostPerWordInCurrency(TextMasterCostPerWordInCurrencyDto costPerWordInCurrency) {
        this.costPerWordInCurrency = costPerWordInCurrency;
    }

    public Boolean getSameAuthorMustDoEntireProject() {
        return sameAuthorMustDoEntireProject;
    }

    public void setSameAuthorMustDoEntireProject(Boolean sameAuthorMustDoEntireProject) {
        this.sameAuthorMustDoEntireProject = sameAuthorMustDoEntireProject;
    }

    public Boolean getAutoLaunch() {
        return autoLaunch;
    }

    public void setAutoLaunch(Boolean autoLaunch) {
        this.autoLaunch = autoLaunch;
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
}