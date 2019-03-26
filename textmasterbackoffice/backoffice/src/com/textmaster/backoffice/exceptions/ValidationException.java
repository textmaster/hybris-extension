package com.textmaster.backoffice.exceptions;

import java.util.List;


public class ValidationException extends Exception
{
	List<String> errors;

	public ValidationException()
	{
		super();
	}

	public ValidationException(String message)
	{
		super(message);
	}

	public ValidationException(String message, List<String> errors)
	{
		super(message);
		setErrors(errors);
	}

	public ValidationException(List<String> errors)
	{
		super();
		setErrors(errors);
	}

	public List<String> getErrors()
	{
		return errors;
	}

	public void setErrors(List<String> errors)
	{
		this.errors = errors;
	}
}
