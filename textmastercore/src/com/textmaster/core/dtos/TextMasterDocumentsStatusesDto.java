package com.textmaster.core.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "in_creation",
        "waiting_assignment",
        "in_progress",
        "in_review",
        "incomplete",
        "completed",
        "paused",
        "canceled",
        "quality_control",
        "copyscape",
        "counting_words",
        "in_extra_review"
})
public class TextMasterDocumentsStatusesDto {

    @JsonProperty("in_creation")
    private Integer inCreation;
    @JsonProperty("waiting_assignment")
    private Integer waitingAssignment;
    @JsonProperty("in_progress")
    private Integer inProgress;
    @JsonProperty("in_review")
    private Integer inReview;
    @JsonProperty("incomplete")
    private Integer incomplete;
    @JsonProperty("completed")
    private Integer completed;
    @JsonProperty("paused")
    private Integer paused;
    @JsonProperty("canceled")
    private Integer canceled;
    @JsonProperty("quality_control")
    private Integer qualityControl;
    @JsonProperty("copyscape")
    private Integer copyscape;
    @JsonProperty("counting_words")
    private Integer countingWords;
    @JsonProperty("in_extra_review")
    private Integer inExtraReview;

    public Integer getInCreation() {
        return inCreation;
    }

    public void setInCreation(Integer inCreation) {
        this.inCreation = inCreation;
    }

    public Integer getWaitingAssignment() {
        return waitingAssignment;
    }

    public void setWaitingAssignment(Integer waitingAssignment) {
        this.waitingAssignment = waitingAssignment;
    }

    public Integer getInProgress() {
        return inProgress;
    }

    public void setInProgress(Integer inProgress) {
        this.inProgress = inProgress;
    }

    public Integer getInReview() {
        return inReview;
    }

    public void setInReview(Integer inReview) {
        this.inReview = inReview;
    }

    public Integer getIncomplete() {
        return incomplete;
    }

    public void setIncomplete(Integer incomplete) {
        this.incomplete = incomplete;
    }

    public Integer getCompleted() {
        return completed;
    }

    public void setCompleted(Integer completed) {
        this.completed = completed;
    }

    public Integer getPaused() {
        return paused;
    }

    public void setPaused(Integer paused) {
        this.paused = paused;
    }

    public Integer getCanceled() {
        return canceled;
    }

    public void setCanceled(Integer canceled) {
        this.canceled = canceled;
    }

    public Integer getQualityControl() {
        return qualityControl;
    }

    public void setQualityControl(Integer qualityControl) {
        this.qualityControl = qualityControl;
    }

    public Integer getCopyscape() {
        return copyscape;
    }

    public void setCopyscape(Integer copyscape) {
        this.copyscape = copyscape;
    }

    public Integer getCountingWords() {
        return countingWords;
    }

    public void setCountingWords(Integer countingWords) {
        this.countingWords = countingWords;
    }

    public Integer getInExtraReview() {
        return inExtraReview;
    }

    public void setInExtraReview(Integer inExtraReview) {
        this.inExtraReview = inExtraReview;
    }
}

