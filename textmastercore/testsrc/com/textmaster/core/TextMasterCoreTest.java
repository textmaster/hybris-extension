package com.textmaster.core;

import com.textmaster.core.dtos.*;
import com.textmaster.core.enums.TextMasterProjectStatusEnum;
import com.textmaster.core.model.TextMasterProjectModel;
import com.textmaster.core.services.TextMasterApiService;
import com.textmaster.core.services.TextMasterProjectService;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class TextMasterCoreTest extends ServicelayerTransactionalTest
{
	private static final Logger LOG = LoggerFactory.getLogger(TextMasterCoreTest.class);
	@Resource
	private TextMasterApiService textMasterApiService;
	@Resource
	private TextMasterProjectService textMasterProjectService;
	@Resource
	private ModelService modelService;
	@Resource
	private CommonI18NService commonI18NService;

	@Test
	public void testNothing()
	{
		assertTrue(true);
		assertFalse(false);
	}

	@Test
	public void testAuthentication()
	{
		// Assets
		String apiKey = "I6b0PTSJRzM";
		String apiSecret = "Hb5zfJdpntI";

		assertTrue(textMasterApiService.checkAuthentication(apiKey, apiSecret));
		assertFalse(textMasterApiService.checkAuthentication("hello", "world"));
	}

	@Test
	public void testGetTemplates()
	{
		// Assets
		String apiKey = "I6b0PTSJRzM";
		String apiSecret = "Hb5zfJdpntI";

		List<TextMasterApiTemplateDto> list = textMasterProjectService.getTemplates(apiKey, apiSecret);

		assertTrue(CollectionUtils.isNotEmpty(list));
	}

	@Test
	public void testPushProject()
	{
		// Assets
		String apiKey = "I6b0PTSJRzM";
		String apiSecret = "Hb5zfJdpntI";

		TextMasterProjectDto dto = new TextMasterProjectDto();
		dto.setName("Test1");
		dto.setApiTemplateId("586f9162-5e0c-4e6b-b25e-45d42c95386f");


		TextMasterProjectRequestDto request = new TextMasterProjectRequestDto();
		request.setProject(dto);

		TextMasterProjectResponseDto response = textMasterApiService.createProject(apiKey, apiSecret, request);

		assertTrue(response.getId() != null);
	}

	@Test
	public void testPushDocuments()
	{
		// Assets
		String apiKey = "I6b0PTSJRzM";
		String apiSecret = "Hb5zfJdpntI";

		TextMasterProjectDto dto = new TextMasterProjectDto();
		dto.setName("Test1");
		dto.setApiTemplateId("586f9162-5e0c-4e6b-b25e-45d42c95386f");

		Map<Object, Object> content = new HashMap<>();
		content.put("attr1", "value1");
		content.put("attr2", "value2");

		List<TextMasterDocumentDto> docs = new ArrayList<>();

		TextMasterDocumentDto doc1 = new TextMasterDocumentDto();
		doc1.setTitle("Doc1");
		doc1.setType("key_value");
		doc1.setDeliverWorkAsFile(false);
		doc1.setPerformWordCount(true);
		doc1.setMarkupInContent(true);
		doc1.setOriginalContent(content);
		docs.add(doc1);

		TextMasterDocumentDto doc2 = new TextMasterDocumentDto();
		doc2.setTitle("Doc1");
		doc2.setType("key_value");
		doc2.setDeliverWorkAsFile(false);
		doc2.setPerformWordCount(true);
		doc2.setMarkupInContent(true);
		doc2.setOriginalContent(content);
		docs.add(doc2);

		TextMasterDocumentsRequestDto request = new TextMasterDocumentsRequestDto();
		request.setDocuments(docs);

		String projectId = "5bd332963e0f6700102e8e04";

		List<TextMasterDocumentDto> response = textMasterApiService.createDocuments(apiKey, apiSecret, projectId, request);

		LOG.info("Number of document created: " + response.size());

		assertTrue(response.size() > 0);
	}

	@Test
	public void testGetLanguages()
	{
		// Assets
		String apiKey = "I6b0PTSJRzM";
		String apiSecret = "Hb5zfJdpntI";
		String projectId = "5bd332963e0f6700102e8e04";
		String status = "in_review";

		// Prepare filters
		Map<String, Object> statusFilter = new HashMap<>();
		statusFilter.put("status", status);

		TextMasterDocumentsResponseDto response = textMasterApiService.filterDocuments(apiKey, apiSecret, projectId, statusFilter);

		LOG.info("Number of translations in review: " + response.getDocuments().size());

		assertTrue(response.getDocuments().size() > 0);
	}

	@Test
	public void testUpdateAttributeValueForLocale()
	{
		LanguageModel languageFR = commonI18NService.getLanguage("fr");
		Locale localeFR = commonI18NService.getLocaleForLanguage(languageFR);

		LanguageModel languageEN = commonI18NService.getLanguage("en");
		Locale localeEN = commonI18NService.getLocaleForLanguage(languageEN);

		List<Locale> availableLocales = Arrays.asList(localeFR, localeEN);

		// Initial text
		ProductModel product = modelService.create(ProductModel.class);
		product.setName("Coucou", localeFR);
		product.setCode("coucoulemonde");
		modelService.save(product);
		modelService.refresh(product);

		Map<Locale, Object> values = modelService
				.getAttributeValues(product, ProductModel.NAME, (Locale[]) availableLocales.toArray());
		assertTrue(MapUtils.isNotEmpty(values));

		values.forEach((locale, translation) -> {
			LOG.info("For language {}: {}", locale.getISO3Language(), translation);
		});

		// Try to change name values for different locales using modelservice
		Map<Locale, Object> newTranslation = new HashMap<>();

		// Add older translations
		newTranslation.putAll(modelService.getAttributeValues(product, ProductModel.NAME, (Locale[]) availableLocales.toArray()));
		newTranslation.put(localeEN, "Hello");

		// Add new translation
		modelService.setAttributeValue(product, ProductModel.NAME, newTranslation);
		modelService.save(product);

		values = modelService.getAttributeValues(product, ProductModel.NAME, (Locale[]) availableLocales.toArray());
		assertTrue(values.size() == 2);

		values.forEach((locale, translation) -> {
			LOG.info("For language {}: {}", locale.getISO3Language(), translation);
		});
	}

	@Test
	public void testGetProjectsByStatuses()
	{
		// Prepare project statuses
		List<TextMasterProjectStatusEnum> projectStatuses = textMasterProjectService.getProjectAvailableStatuses();

		// Prepare available locales
		List<Locale> availableLocales = commonI18NService.getAllLanguages()
				.stream()
				.map(l -> commonI18NService.getLocaleForLanguage(l))
				.collect(Collectors.toList());

		// Take in account all projects open
		List<TextMasterProjectModel> projects = textMasterProjectService.getProjects(projectStatuses);

		assertTrue(CollectionUtils.isEmpty(projects));
	}

	@Test
	public void testFinalizeProject()
	{
		// Assets
		String apiKey = "I6b0PTSJRzM";
		String apiSecret = "Hb5zfJdpntI";

		TextMasterProjectDto dto = new TextMasterProjectDto();
		dto.setName("Integration Test");
		dto.setApiTemplateId("586f9162-5e0c-4e6b-b25e-45d42c95386f");

		// Create project
		TextMasterProjectRequestDto requestProject = new TextMasterProjectRequestDto();
		requestProject.setProject(dto);
		TextMasterProjectResponseDto responseProject = textMasterApiService.createProject(apiKey, apiSecret, requestProject);

		assertTrue(responseProject.getId() != null);

		// Create document
		Map<Object, Object> content = new HashMap<>();
		content.put("attr1", "value1");
		content.put("attr2", "value2");

		List<TextMasterDocumentDto> docs = new ArrayList<>();
		TextMasterDocumentDto doc1 = new TextMasterDocumentDto();
		doc1.setTitle("Doc1");
		doc1.setType("key_value");
		doc1.setDeliverWorkAsFile(false);
		doc1.setPerformWordCount(true);
		doc1.setMarkupInContent(true);
		doc1.setOriginalContent(content);
		docs.add(doc1);

		TextMasterDocumentsRequestDto requestDocuments = new TextMasterDocumentsRequestDto();
		requestDocuments.setDocuments(docs);

		List<TextMasterDocumentDto> responseDocuments = textMasterApiService
				.createDocuments(apiKey, apiSecret, responseProject.getId(), requestDocuments);

		LOG.info("Number of document created: " + responseDocuments.size());

		assertTrue(responseDocuments.size() > 0);

		// Finalize project
		TextMasterProjectResponseDto responseFinalize = textMasterApiService.finalize(apiKey, apiSecret, responseProject.getId());

		LOG.info("Total word count: " + responseFinalize.getTotalWordCount());
		LOG.info("Total costs: {} {}", responseFinalize.getCostInCurrency().getAmount(),
				responseFinalize.getCostInCurrency().getCurrency());

		assertTrue(responseFinalize.getId() != null);
	}

	@Test
	public void testSupportMessages()
	{
		// Assets
		String apiKey = "I6b0PTSJRzM";
		String apiSecret = "Hb5zfJdpntI";

		String projectId = "5be1e28e70317a0010ccd1b8";
		String documentId = "5be1e28f70317a0010ccd1ea";

		TextMasterSupportMessageDto supportMessage = new TextMasterSupportMessageDto();
		supportMessage.setMessage("Coucou :-)");
		TextMasterSupportMessageRequestDto requestSupportMessage = new TextMasterSupportMessageRequestDto();
		requestSupportMessage.setSupportMessage(supportMessage);

		// Create message
		TextMasterSupportMessageResponseDto responseSupportMessage = textMasterApiService
				.createSupportMessage(apiKey, apiSecret, projectId, documentId, requestSupportMessage);

		// Create message
		TextMasterSupportMessagesResponseDto responseSupportMessages = textMasterApiService
				.getSupportMessages(apiKey, apiSecret, projectId, documentId);
		assertTrue(responseSupportMessages.getSupportMessages().size() == 1);
	}
}
