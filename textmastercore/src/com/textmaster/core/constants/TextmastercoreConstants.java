/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.textmaster.core.constants;

/**
 * Global class for all Textmastercore constants. You can add global constants for your extension into this class.
 */
public final class TextmastercoreConstants extends GeneratedTextmastercoreConstants
{
	public static final String EXTENSIONNAME = "textmastercore";

	private TextmastercoreConstants()
	{
		//empty to avoid instantiating this constant class
	}

	// implement here constants used by this extension
	public static final String URL_PROPERTY_KEY = "textmastercore.baseurl";
	public static final String MUST_UPDATE_CATALOG_PROPERTY_KEY = "textmastercore.catalog.staged.update";

	public interface Documents
	{
		public static final String BATCH_SIZE = "textmastercore.documents.batch.size";
		public static final String SELECTION_MAX = "textmastercore.documents.selection.max";
	}

	public interface Services
	{
		public static final String AUTHENTICATION_TEST = "/test";
		public static final String TEMPLATES = "/v1/clients/api_templates?page={page}";
		public static final String LOCALES = "/v1/locales";
		public static final String PROJECT_CREATE = "/v1/clients/projects";
		public static final String PROJECT_GET = "/v1/clients/projects/{project}";
		public static final String PROJECT_FINALIZE = "/v1/clients/projects/{project}/finalize";
		public static final String PROJECT_LAUNCH = "/v1/clients/projects/{project}/launch";
		public static final String DOCUMENT_MULTIPLE_CREATE = "/v1/clients/projects/{project}/batch/documents";
		public static final String DOCUMENT_MULTIPLE_COMPLETE = "/v1/clients/projects/{project}/batch/documents/complete";
		public static final String DOCUMENTS_GET_FILTER = "/v1/clients/projects/{project}/documents/filter?where={where}&order={order}&page={page}";
		public static final String SUPPORT_MESSAGES = "/v1/clients/projects/{project}/documents/{document}/support_messages";

		public interface Constants
		{
			public static final String COMPLETE_RETURN_OK = "ok";
		}
	}

	public interface Parameters
	{
		public interface DocumentType
		{
			public static final String KEY_VALUE = "key_value";
		}
	}

	public interface EnvironmentMode
	{
		public static final String DEV = "dev";
		public static final String PROD = "prod";
	}
}
