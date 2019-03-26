package com.textmaster.core.dtos.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Allows to create a date from string with specific format
 */
public class DateDeserializer extends StdDeserializer<Date>
{
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss' UTC'");

	public DateDeserializer()
	{
		this(null);
	}

	public DateDeserializer(Class<?> c)
	{
		super(c);
	}

	@Override
	public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
			throws IOException, JsonProcessingException
	{
		String date = jsonParser.getText();
		try
		{
			return dateFormat.parse(date);
		}
		catch (ParseException e)
		{
			throw new RuntimeException(e);
		}
	}
}
