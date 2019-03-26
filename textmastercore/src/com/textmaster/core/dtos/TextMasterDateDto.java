package com.textmaster.core.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.textmaster.core.dtos.deserializer.DateDeserializer;

import java.util.Date;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"day",
		"month",
		"year",
		"full"
})
public class TextMasterDateDto
{

	@JsonProperty("day")
	private Integer day;
	@JsonProperty("month")
	private Integer month;
	@JsonProperty("year")
	private Integer year;
	@JsonProperty("full")
	@JsonDeserialize(using = DateDeserializer.class)
	private Date full;

	public Integer getDay()
	{
		return day;
	}

	public void setDay(Integer day)
	{
		this.day = day;
	}

	public Integer getMonth()
	{
		return month;
	}

	public void setMonth(Integer month)
	{
		this.month = month;
	}

	public Integer getYear()
	{
		return year;
	}

	public void setYear(Integer year)
	{
		this.year = year;
	}

	public Date getFull()
	{
		return full;
	}

	public void setFull(Date full)
	{
		this.full = full;
	}
}
