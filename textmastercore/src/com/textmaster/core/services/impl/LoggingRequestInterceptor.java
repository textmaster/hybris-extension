package com.textmaster.core.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;


public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor
{

	final static Logger LOG = LoggerFactory.getLogger(LoggingRequestInterceptor.class);

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException
	{
		if (LOG.isDebugEnabled())
		{
			traceRequest(request, body);
		}
		ClientHttpResponse response = execution.execute(request, body);

		if (LOG.isDebugEnabled())
		{
			traceResponse(response);
		}
		return response;
	}

	private void traceRequest(HttpRequest request, byte[] body) throws IOException
	{
		LOG.debug("===========================request begin================================================");
		LOG.debug("URI         : {}", request.getURI());
		LOG.debug("Method      : {}", request.getMethod());
		LOG.debug("Headers     : {}", request.getHeaders());
		LOG.debug("Request body: {}", new String(body, "UTF-8"));
		LOG.debug("==========================request end================================================");
	}

	private void traceResponse(ClientHttpResponse response) throws IOException
	{
		BufferedReader buffer = new BufferedReader(new InputStreamReader(response.getBody(), "UTF-8"));
		String responseBody = buffer.lines().collect(Collectors.joining("\n"));

		LOG.debug("============================response begin==========================================");
		LOG.debug("Status code  : {}", response.getStatusCode());
		LOG.debug("Status text  : {}", response.getStatusText());
		LOG.debug("Headers      : {}", response.getHeaders());
		LOG.debug("Response body: {}", responseBody);
		LOG.debug("=======================response end=================================================");
	}

}
