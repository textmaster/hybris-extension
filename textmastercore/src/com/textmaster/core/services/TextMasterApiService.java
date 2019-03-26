package com.textmaster.core.services;

import com.textmaster.core.dtos.*;

import java.util.List;
import java.util.Map;


public interface TextMasterApiService
{
	/**
	 * Verify if api key and secret are consistent on TextMaster platform.
	 *
	 * @param apiKey
	 * @param apiSecret
	 * @return
	 */
	public boolean checkAuthentication(String apiKey, String apiSecret);

	/**
	 * Get templates from remote platform.
	 *
	 * @param apiKey
	 * @param apiSecret
	 * @return
	 */
	public TextMasterApiTemplateResponseDto getTemplates(String apiKey, String apiSecret);

	/**
	 * Get templates from remote platform.
	 *
	 * @param apiKey
	 * @param apiSecret
	 *
	 * @return
	 */
	public TextMasterApiTemplateResponseDto getTemplates(String apiKey, String apiSecret, int page);

	/**
	 * Get locales from remote platform.
	 *
	 * @param apiKey
	 * @param apiSecret
	 * @return
	 */
	public TextMasterLocalesResponseDto getLanguages(String apiKey, String apiSecret);

	/**
	 * Push project on remote platform.
	 *
	 * @param apiKey
	 * @param apiSecret
	 * @param request
	 * @return
	 */
	public TextMasterProjectResponseDto createProject(String apiKey, String apiSecret, TextMasterProjectRequestDto request);

	/**
	 * Push project on remote platform.
	 *
	 * @param apiKey
	 * @param apiSecret
	 * @param projectId
	 * @param request
	 * @return
	 */
	public List<TextMasterDocumentDto> createDocuments(String apiKey, String apiSecret, String projectId,
			TextMasterDocumentsRequestDto request);

	/**
	 * Filter documents according to project and criterias.
	 *
	 * @param apiKey
	 * @param apiSecret
	 * @param projectId
	 * @param filters
	 * @return
	 */
	public TextMasterDocumentsResponseDto filterDocuments(String apiKey, String apiSecret, String projectId,
			Map<String, Object> filters);

	/**
	 * Filter documents for a specific page according to project and criterias.
	 *
	 * @param apiKey
	 * @param apiSecret
	 * @param projectId
	 * @param filters
	 * @param page
	 * @return
	 */
	public TextMasterDocumentsResponseDto filterDocuments(String apiKey, String apiSecret, String projectId,
			Map<String, Object> filters, int page);

	/**
	 * Finalize project.
	 *
	 * @param apiKey
	 * @param apiSecret
	 * @param projectId
	 * @return
	 */
	public TextMasterProjectResponseDto finalize(String apiKey, String apiSecret, String projectId);

	/**
	 * Launch project.
	 *
	 * @param apiKey
	 * @param apiSecret
	 * @param projectId
	 * @return
	 */
	public TextMasterProjectResponseDto launch(String apiKey, String apiSecret, String projectId);

	/**
	 * Create support a message.
	 *
	 * @param apiKey
	 * @param apiSecret
	 * @param projectId
	 * @param documentId
	 * @param request
	 * @return
	 */
	public TextMasterSupportMessageResponseDto createSupportMessage(String apiKey, String apiSecret, String projectId,
			String documentId, TextMasterSupportMessageRequestDto request);

	/**
	 * Get support a messages.
	 *
	 * @param apiKey
	 * @param apiSecret
	 * @param projectId
	 * @param documentId
	 * @return
	 */
	public TextMasterSupportMessagesResponseDto getSupportMessages(String apiKey, String apiSecret, String projectId,
			String documentId);

	/**
	 * Get a project.
	 *
	 * @param apiKey
	 * @param apiSecret
	 * @param projectId
	 * @return
	 */
	public TextMasterProjectResponseDto getProject(String apiKey, String apiSecret, String projectId);

	/**
	 * Complete documents.
	 *
	 * @param apiKey
	 * @param apiSecret
	 * @param projectId
	 * @param request
	 * @return
	 */
	public boolean completeDocuments(String apiKey, String apiSecret, String projectId,
			TextMasterDocumentsCompleteRequestDto request);
}
