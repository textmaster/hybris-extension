package com.textmaster.core.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.textmaster.core.constants.TextmastercoreConstants;
import com.textmaster.core.dtos.*;
import com.textmaster.core.services.TextMasterApiService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;


public class DefaultTextMasterApiService implements TextMasterApiService
{
	private static final Logger LOG = Logger.getLogger(DefaultTextMasterApiService.class);
	private ConfigurationService configurationService;

	/**
	 * Create request template.
	 *
	 * @return
	 */
	protected RestTemplate getTemplate()
	{
		String environmentMode = getConfigurationService().getConfiguration().getString("textmastercore.envionment.mode");

		if (TextmastercoreConstants.EnvironmentMode.DEV.equals(environmentMode))
		{
			// TODO: Check if a different strategy should not be applied here for production environment
			final HttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
			final HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
			requestFactory.setHttpClient(httpClient);

			final RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(requestFactory));

			// TODO: Use interceptor in debug mode
			final List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
			interceptors.add(new LoggingRequestInterceptor());
			restTemplate.setInterceptors(interceptors);

			return restTemplate;
		}

		return new RestTemplate();
	}

	/**
	 * Create headers.
	 *
	 * @param apiKey
	 * @param apiSecret
	 * @return
	 */
	protected HttpHeaders getHeaders(final String apiKey, final String apiSecret)
	{
		// Date
		final String pattern = "yyy-MM-dd HH:mm:ss";
		final SimpleDateFormat format = new SimpleDateFormat(pattern);
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
		final String date = format.format(new Date());

		// Signature calculation
		final String signature = DigestUtils.sha1Hex(apiSecret + date);

		// Headers preparation
		final HttpHeaders headers = new HttpHeaders();
		headers.set("Apikey", apiKey);
		headers.set("Date", date);
		headers.set("Signature", signature);
		headers.set("X-Partner-Id", getConfigurationService().getConfiguration().getString("textmastercore.partnerid"));

		return headers;
	}

	/**
	 * Build full path from base url.
	 *
	 * @param path
	 * @return
	 */
	protected String getUrl(final String path)
	{
		return this.getConfigurationService().getConfiguration().getString(TextmastercoreConstants.URL_PROPERTY_KEY) + path;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean checkAuthentication(final String apiKey, final String apiSecret)
	{
		final String url = this.getUrl(TextmastercoreConstants.Services.AUTHENTICATION_TEST);

		final RestTemplate template = this.getTemplate();

		// Prepare request
		final HttpHeaders headers = getHeaders(apiKey, apiSecret);
		final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

		// Send request
		final ResponseEntity<TextMasterAuthenticationDto> responseEntity = template.exchange(url, HttpMethod.GET, requestEntity,
				TextMasterAuthenticationDto.class);

		final TextMasterAuthenticationDto body = responseEntity.getBody();

		final String returnMessage = body.getMessage();

		if (returnMessage.matches("^.*Your signature is valid.$"))
		{
			return true;
		}

		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public TextMasterApiTemplateResponseDto getTemplates(final String apiKey, final String apiSecret)
	{
		// Get the first page by default
		return getTemplates(apiKey, apiSecret, 0);
	}

	/**
	 * {@inheritDoc}
	 */
	public TextMasterApiTemplateResponseDto getTemplates(final String apiKey, final String apiSecret, final int page)
	{
		final String url = this.getUrl(TextmastercoreConstants.Services.TEMPLATES);

		final RestTemplate template = this.getTemplate();

		// Prepare request
		final HttpHeaders headers = getHeaders(apiKey, apiSecret);
		final HttpEntity<String> requestEntity = new HttpEntity<>(headers);


		// Prepare parameters
		final Map<String, String> params = new HashMap<>();
		params.put("page", String.valueOf(page));

		// Send request
		final ResponseEntity<TextMasterApiTemplateResponseDto> responseEntity = template.exchange(url, HttpMethod.GET,
				requestEntity, TextMasterApiTemplateResponseDto.class, params);

		return responseEntity.getBody();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TextMasterLocalesResponseDto getLanguages(final String apiKey, final String apiSecret)
	{
		final String url = this.getUrl(TextmastercoreConstants.Services.LOCALES);

		final RestTemplate template = this.getTemplate();

		// Prepare request
		final HttpHeaders headers = getHeaders(apiKey, apiSecret);
		final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

		// Send request
		final ResponseEntity<TextMasterLocalesResponseDto> responseEntity = template.exchange(url, HttpMethod.GET, requestEntity,
				TextMasterLocalesResponseDto.class);

		return responseEntity.getBody();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TextMasterProjectResponseDto createProject(final String apiKey, final String apiSecret,
			final TextMasterProjectRequestDto request)
	{
		final String url = this.getUrl(TextmastercoreConstants.Services.PROJECT_CREATE);

		final RestTemplate template = this.getTemplate();

		// Prepare request
		final HttpHeaders headers = getHeaders(apiKey, apiSecret);
		final HttpEntity<TextMasterProjectRequestDto> requestEntity = new HttpEntity<>(request, headers);

		// Send request
		final ResponseEntity<TextMasterProjectResponseDto> responseEntity = template.exchange(url, HttpMethod.POST, requestEntity,
				TextMasterProjectResponseDto.class);

		return responseEntity.getBody();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TextMasterDocumentDto> createDocuments(final String apiKey, final String apiSecret, final String projectId,
			final TextMasterDocumentsRequestDto request)
	{
		final String url = this.getUrl(TextmastercoreConstants.Services.DOCUMENT_MULTIPLE_CREATE);

		final RestTemplate template = this.getTemplate();

		// Prepare request
		final HttpHeaders headers = getHeaders(apiKey, apiSecret);
		final HttpEntity<TextMasterDocumentsRequestDto> requestEntity = new HttpEntity<>(request, headers);

		// Prepare parameters
		final Map<String, String> params = new HashMap<>();
		params.put("project", projectId);

		// Send request
		final ResponseEntity<List<TextMasterDocumentDto>> responseEntity = template.exchange(url, HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<List<TextMasterDocumentDto>>()
				{
					// Empty Body
				}, params);

		return responseEntity.getBody();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TextMasterDocumentsResponseDto filterDocuments(final String apiKey, final String apiSecret,
			final String projectId,
			final Map<String, Object> filters)
	{
		return filterDocuments(apiKey, apiSecret, projectId, filters, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TextMasterDocumentsResponseDto filterDocuments(final String apiKey, final String apiSecret, final String projectId,
			final Map<String, Object> filters, int page)
	{
		final String url = this.getUrl(TextmastercoreConstants.Services.DOCUMENTS_GET_FILTER);

		final RestTemplate template = this.getTemplate();

		// Prepare request
		final HttpHeaders headers = getHeaders(apiKey, apiSecret);
		final HttpEntity<TextMasterDocumentsRequestDto> requestEntity = new HttpEntity<>(headers);

		final ObjectMapper objectMapper = new ObjectMapper();

		// Prepare parameters
		final Map<String, String> params = new HashMap<>();
		params.put("project", projectId);
		params.put("order", "");
		params.put("page", String.valueOf(page));

		try
		{
			params.put("where", objectMapper.writeValueAsString(filters));
		}
		catch (final JsonProcessingException e)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Impossible to convert document filters to JSON", e);
			}
			LOG.error("Impossible to convert document filters to JSON");
		}

		// Send request
		final ResponseEntity<TextMasterDocumentsResponseDto> responseEntity = template.exchange(url, HttpMethod.GET, requestEntity,
				TextMasterDocumentsResponseDto.class, params);

		return responseEntity.getBody();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TextMasterProjectResponseDto finalize(String apiKey, String apiSecret, String projectId)
	{
		final String url = this.getUrl(TextmastercoreConstants.Services.PROJECT_FINALIZE);

		final RestTemplate template = this.getTemplate();

		// Prepare request
		final HttpHeaders headers = getHeaders(apiKey, apiSecret);
		final HttpEntity<TextMasterDocumentsRequestDto> requestEntity = new HttpEntity<>(headers);

		final ObjectMapper objectMapper = new ObjectMapper();

		// Prepare parameters
		final Map<String, String> params = new HashMap<>();
		params.put("project", projectId);

		// Send request
		final ResponseEntity<TextMasterProjectResponseDto> responseEntity = template.exchange(url, HttpMethod.PUT, requestEntity,
				TextMasterProjectResponseDto.class, params);

		return responseEntity.getBody();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TextMasterProjectResponseDto launch(String apiKey, String apiSecret, String projectId)
	{
		final String url = this.getUrl(TextmastercoreConstants.Services.PROJECT_LAUNCH);

		final RestTemplate template = this.getTemplate();

		// Prepare request
		final HttpHeaders headers = getHeaders(apiKey, apiSecret);
		final HttpEntity<TextMasterDocumentsRequestDto> requestEntity = new HttpEntity<>(headers);

		final ObjectMapper objectMapper = new ObjectMapper();

		// Prepare parameters
		final Map<String, String> params = new HashMap<>();
		params.put("project", projectId);

		// Send request
		final ResponseEntity<TextMasterProjectResponseDto> responseEntity = template.exchange(url, HttpMethod.PUT, requestEntity,
				TextMasterProjectResponseDto.class, params);

		return responseEntity.getBody();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TextMasterSupportMessageResponseDto createSupportMessage(String apiKey, String apiSecret, String projectId,
			String documentId, TextMasterSupportMessageRequestDto request)
	{
		final String url = this.getUrl(TextmastercoreConstants.Services.SUPPORT_MESSAGES);

		final RestTemplate template = this.getTemplate();

		// Prepare request
		final HttpHeaders headers = getHeaders(apiKey, apiSecret);
		final HttpEntity<TextMasterSupportMessageRequestDto> requestEntity = new HttpEntity<>(request, headers);

		// Prepare parameters
		final Map<String, String> params = new HashMap<>();
		params.put("project", projectId);
		params.put("document", documentId);

		// Send request
		final ResponseEntity<TextMasterSupportMessageResponseDto> responseEntity = template
				.exchange(url, HttpMethod.POST, requestEntity,
						new ParameterizedTypeReference<TextMasterSupportMessageResponseDto>()
						{
							// Empty Body
						}, params);

		return responseEntity.getBody();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TextMasterSupportMessagesResponseDto getSupportMessages(String apiKey, String apiSecret, String projectId,
			String documentId)
	{
		final String url = this.getUrl(TextmastercoreConstants.Services.SUPPORT_MESSAGES);

		final RestTemplate template = this.getTemplate();

		// Prepare request
		final HttpHeaders headers = getHeaders(apiKey, apiSecret);
		final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

		// Prepare parameters
		final Map<String, String> params = new HashMap<>();
		params.put("project", projectId);
		params.put("document", documentId);

		// Send request
		final ResponseEntity<TextMasterSupportMessagesResponseDto> responseEntity = template.exchange(url, HttpMethod.GET,
				requestEntity,
				new ParameterizedTypeReference<TextMasterSupportMessagesResponseDto>()
				{
					// Empty Body
				}, params);

		return responseEntity.getBody();
	}

	/**
	 * {@inheritDoc}
	 */
	public TextMasterProjectResponseDto getProject(String apiKey, String apiSecret, String projectId)
	{

		final String url = this.getUrl(TextmastercoreConstants.Services.PROJECT_GET);

		final RestTemplate template = this.getTemplate();

		// Prepare request
		final HttpHeaders headers = getHeaders(apiKey, apiSecret);
		final HttpEntity<String> requestEntity = new HttpEntity<>(headers);

		// Prepare parameters
		final Map<String, String> params = new HashMap<>();
		params.put("project", projectId);

		// Send request
		final ResponseEntity<TextMasterProjectResponseDto> responseEntity = template.exchange(url, HttpMethod.GET, requestEntity,
				TextMasterProjectResponseDto.class, params);

		return responseEntity.getBody();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean completeDocuments(String apiKey, String apiSecret, String projectId,
			TextMasterDocumentsCompleteRequestDto request)
	{
		final String url = this.getUrl(TextmastercoreConstants.Services.DOCUMENT_MULTIPLE_COMPLETE);

		final RestTemplate template = this.getTemplate();

		// Prepare request
		final HttpHeaders headers = getHeaders(apiKey, apiSecret);
		final HttpEntity<TextMasterDocumentsCompleteRequestDto> requestEntity = new HttpEntity<>(request, headers);

		// Prepare parameters
		final Map<String, String> params = new HashMap<>();
		params.put("project", projectId);

		// Send request
		final ResponseEntity<TextMasterDocumentsCompleteResponseDto> responseEntity = template.exchange(url, HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<TextMasterDocumentsCompleteResponseDto>()
				{
					// Empty Body
				}, params);

		if (TextmastercoreConstants.Services.Constants.COMPLETE_RETURN_OK.equals(responseEntity.getBody().getStatus())) {
			return true;
		}

		return false;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}
}
