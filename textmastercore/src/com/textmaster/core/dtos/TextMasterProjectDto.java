package com.textmaster.core.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"id",
		"name",
		"language_from",
		"language_to",
		"language_from_code",
		"language_to_code",
		"category",
		"vocabulary_type",
		"target_reader_groups",
		"grammatical_person",
		"custom_data",
		"project_briefing",
		"project_briefing_is_rich",
		"priority",
		"status",
		"total_word_count",
		"documents_statuses",
		"progress",
		"same_author_must_do_entire_project",
		"callback",
		"external_id",
		"multi_project_id",
		"auto_launch",
		"project_template_id",
		"finalized",
		"word_count_error",
		"cost_in_credits",
		"ctype",
		"creation_channel",
		"reference",
		"cost_per_word",
		"options",
		"option_values",
		"textmasters",
		"total_costs",
		"api_template_id",
		"created_at",
		"updated_at",
		"launched_at",
		"completed_at",
		"work_template"
})
public class TextMasterProjectDto
{
	@JsonProperty("id")
	private String id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("language_from")
	private String languageFrom;
	@JsonProperty("language_to")
	private String languageTo;
	@JsonProperty("language_from_code")
	private String languageFromCode;
	@JsonProperty("language_to_code")
	private String languageToCode;
	@JsonProperty("category")
	private String category;
	@JsonProperty("vocabulary_type")
	private String vocabularyType;
	@JsonProperty("target_reader_groups")
	private String targetReaderGroups;
	@JsonProperty("grammatical_person")
	private String grammaticalPerson;
	@JsonProperty("custom_data")
	private TextMasterCustomDataDto customData;
	@JsonProperty("project_briefing")
	private String projectBriefing;
	@JsonProperty("project_briefing_is_rich")
	private Boolean projectBriefingIsRich;
	@JsonProperty("priority")
	private Float priority;
	@JsonProperty("status")
	private String status;
	@JsonProperty("total_word_count")
	private Integer totalWordCount;
	@JsonProperty("documents_statuses")
	private TextMasterDocumentsStatusesDto documentsStatuses;
	@JsonProperty("progress")
	private Float progress;
	@JsonProperty("same_author_must_do_entire_project")
	private Boolean sameAuthorMustDoEntireProject;
	@JsonProperty("callback")
	private TextMasterCallbackDto callback;
	@JsonProperty("external_id")
	private Object externalId;
	@JsonProperty("multi_project_id")
	private Object multiProjectId;
	@JsonProperty("auto_launch")
	private Boolean autoLaunch;
	@JsonProperty("project_template_id")
	private Object projectTemplateId;
	@JsonProperty("finalized")
	private Boolean finalized;
	@JsonProperty("word_count_error")
	private Object wordCountError;
	@JsonProperty("cost_in_credits")
	private Integer costInCredits;
	@JsonProperty("ctype")
	private String ctype;
	@JsonProperty("creation_channel")
	private String creationChannel;
	@JsonProperty("reference")
	private String reference;
	@JsonProperty("cost_per_word")
	private TextMasterCostPerWordDto costPerWord;
	@JsonProperty("options")
	private TextMasterOptionsDto options;
	@JsonProperty("option_values")
	private TextMasterOptionValuesDto optionValues;
	@JsonProperty("textmasters")
	private List<Object> textmasters = null;
	@JsonProperty("total_costs")
	private List<TextMasterTotalCostDto> totalCosts = null;
	@JsonProperty("api_template_id")
	private Object apiTemplateId;
	@JsonProperty("created_at")
	private TextMasterDateDto createdAt;
	@JsonProperty("updated_at")
	private TextMasterDateDto updatedAt;
	@JsonProperty("launched_at")
	private Object launchedAt;
	@JsonProperty("completed_at")
	private Object completedAt;
	@JsonProperty("work_template")
	private TextMasterWorkTemplateDto workTemplate;
	@JsonProperty("cost_in_currency")
	private TextMasterCostInCurrencyDto costInCurrency;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getLanguageFrom()
	{
		return languageFrom;
	}

	public void setLanguageFrom(String languageFrom)
	{
		this.languageFrom = languageFrom;
	}

	public String getLanguageTo()
	{
		return languageTo;
	}

	public void setLanguageTo(String languageTo)
	{
		this.languageTo = languageTo;
	}

	public String getLanguageFromCode()
	{
		return languageFromCode;
	}

	public void setLanguageFromCode(String languageFromCode)
	{
		this.languageFromCode = languageFromCode;
	}

	public String getLanguageToCode()
	{
		return languageToCode;
	}

	public void setLanguageToCode(String languageToCode)
	{
		this.languageToCode = languageToCode;
	}

	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}

	public String getVocabularyType()
	{
		return vocabularyType;
	}

	public void setVocabularyType(String vocabularyType)
	{
		this.vocabularyType = vocabularyType;
	}

	public String getTargetReaderGroups()
	{
		return targetReaderGroups;
	}

	public void setTargetReaderGroups(String targetReaderGroups)
	{
		this.targetReaderGroups = targetReaderGroups;
	}

	public String getGrammaticalPerson()
	{
		return grammaticalPerson;
	}

	public void setGrammaticalPerson(String grammaticalPerson)
	{
		this.grammaticalPerson = grammaticalPerson;
	}

	public TextMasterCustomDataDto getCustomData()
	{
		return customData;
	}

	public void setCustomData(TextMasterCustomDataDto customData)
	{
		this.customData = customData;
	}

	public String getProjectBriefing()
	{
		return projectBriefing;
	}

	public void setProjectBriefing(String projectBriefing)
	{
		this.projectBriefing = projectBriefing;
	}

	public Boolean getProjectBriefingIsRich()
	{
		return projectBriefingIsRich;
	}

	public void setProjectBriefingIsRich(Boolean projectBriefingIsRich)
	{
		this.projectBriefingIsRich = projectBriefingIsRich;
	}

	public Float getPriority()
	{
		return priority;
	}

	public void setPriority(Float priority)
	{
		this.priority = priority;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public Integer getTotalWordCount()
	{
		return totalWordCount;
	}

	public void setTotalWordCount(Integer totalWordCount)
	{
		this.totalWordCount = totalWordCount;
	}

	public TextMasterDocumentsStatusesDto getDocumentsStatuses()
	{
		return documentsStatuses;
	}

	public void setDocumentsStatuses(TextMasterDocumentsStatusesDto documentsStatuses)
	{
		this.documentsStatuses = documentsStatuses;
	}

	public Float getProgress()
	{
		return progress;
	}

	public void setProgress(Float progress)
	{
		this.progress = progress;
	}

	public Boolean getSameAuthorMustDoEntireProject()
	{
		return sameAuthorMustDoEntireProject;
	}

	public void setSameAuthorMustDoEntireProject(Boolean sameAuthorMustDoEntireProject)
	{
		this.sameAuthorMustDoEntireProject = sameAuthorMustDoEntireProject;
	}

	public TextMasterCallbackDto getCallback()
	{
		return callback;
	}

	public void setCallback(TextMasterCallbackDto callback)
	{
		this.callback = callback;
	}

	public Object getExternalId()
	{
		return externalId;
	}

	public void setExternalId(Object externalId)
	{
		this.externalId = externalId;
	}

	public Object getMultiProjectId()
	{
		return multiProjectId;
	}

	public void setMultiProjectId(Object multiProjectId)
	{
		this.multiProjectId = multiProjectId;
	}

	public Boolean getAutoLaunch()
	{
		return autoLaunch;
	}

	public void setAutoLaunch(Boolean autoLaunch)
	{
		this.autoLaunch = autoLaunch;
	}

	public Object getProjectTemplateId()
	{
		return projectTemplateId;
	}

	public void setProjectTemplateId(Object projectTemplateId)
	{
		this.projectTemplateId = projectTemplateId;
	}

	public Boolean getFinalized()
	{
		return finalized;
	}

	public void setFinalized(Boolean finalized)
	{
		this.finalized = finalized;
	}

	public Object getWordCountError()
	{
		return wordCountError;
	}

	public void setWordCountError(Object wordCountError)
	{
		this.wordCountError = wordCountError;
	}

	public Integer getCostInCredits()
	{
		return costInCredits;
	}

	public void setCostInCredits(Integer costInCredits)
	{
		this.costInCredits = costInCredits;
	}

	public String getCtype()
	{
		return ctype;
	}

	public void setCtype(String ctype)
	{
		this.ctype = ctype;
	}

	public String getCreationChannel()
	{
		return creationChannel;
	}

	public void setCreationChannel(String creationChannel)
	{
		this.creationChannel = creationChannel;
	}

	public String getReference()
	{
		return reference;
	}

	public void setReference(String reference)
	{
		this.reference = reference;
	}

	public TextMasterCostPerWordDto getCostPerWord()
	{
		return costPerWord;
	}

	public void setCostPerWord(TextMasterCostPerWordDto costPerWord)
	{
		this.costPerWord = costPerWord;
	}

	public TextMasterOptionsDto getOptions()
	{
		return options;
	}

	public void setOptions(TextMasterOptionsDto options)
	{
		this.options = options;
	}

	public TextMasterOptionValuesDto getOptionValues()
	{
		return optionValues;
	}

	public void setOptionValues(TextMasterOptionValuesDto optionValues)
	{
		this.optionValues = optionValues;
	}

	public List<Object> getTextmasters()
	{
		return textmasters;
	}

	public void setTextmasters(List<Object> textmasters)
	{
		this.textmasters = textmasters;
	}

	public List<TextMasterTotalCostDto> getTotalCosts()
	{
		return totalCosts;
	}

	public void setTotalCosts(List<TextMasterTotalCostDto> totalCosts)
	{
		this.totalCosts = totalCosts;
	}

	public Object getApiTemplateId()
	{
		return apiTemplateId;
	}

	public void setApiTemplateId(Object apiTemplateId)
	{
		this.apiTemplateId = apiTemplateId;
	}

	public TextMasterDateDto getCreatedAt()
	{
		return createdAt;
	}

	public void setCreatedAt(TextMasterDateDto createdAt)
	{
		this.createdAt = createdAt;
	}

	public TextMasterDateDto getUpdatedAt()
	{
		return updatedAt;
	}

	public void setUpdatedAt(TextMasterDateDto updatedAt)
	{
		this.updatedAt = updatedAt;
	}

	public Object getLaunchedAt()
	{
		return launchedAt;
	}

	public void setLaunchedAt(Object launchedAt)
	{
		this.launchedAt = launchedAt;
	}

	public Object getCompletedAt()
	{
		return completedAt;
	}

	public void setCompletedAt(Object completedAt)
	{
		this.completedAt = completedAt;
	}

	public TextMasterWorkTemplateDto getWorkTemplate()
	{
		return workTemplate;
	}

	public void setWorkTemplate(TextMasterWorkTemplateDto workTemplate)
	{
		this.workTemplate = workTemplate;
	}

	public TextMasterCostInCurrencyDto getCostInCurrency()
	{
		return costInCurrency;
	}

	public void setCostInCurrency(TextMasterCostInCurrencyDto costInCurrency)
	{
		this.costInCurrency = costInCurrency;
	}
}
