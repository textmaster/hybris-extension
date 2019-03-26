/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package com.textmaster.backoffice.constants;

/**
 * Global class for all Ybackoffice constants. You can add global constants for your extension into this class.
 */
public final class TextmasterbackofficeConstants extends GeneratedTextmasterbackofficeConstants
{
	public static final String EXTENSIONNAME = "textmasterbackoffice";

	private TextmasterbackofficeConstants()
	{
		//empty to avoid instantiating this constant class
	}

	/**
	 * Widget parameters available to pass data between widgets
	 */
	public static interface ProjectParameters {
		public static final String ACCOUNT = "account";
		public static final String NAME = "name";
		public static final String TEMPLATES = "templates";
		public static final String COMPOSED_TYPE = "type";
		public static final String ATTRIBUTES = "attributes";
	}

	public static interface UI {
		public static final String NONE_VALUE = "none";
	}
}
