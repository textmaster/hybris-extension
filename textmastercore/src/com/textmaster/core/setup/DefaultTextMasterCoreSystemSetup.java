
package com.textmaster.core.setup;

import com.textmaster.core.constants.TextmastercoreConstants;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;

import java.util.ArrayList;
import java.util.List;


@SystemSetup(extension = TextmastercoreConstants.EXTENSIONNAME)
public class DefaultTextMasterCoreSystemSetup extends AbstractSystemSetup
{
	private static final String IMPORT_SAMPLE_DATA = "importSampleData";

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SystemSetupParameterMethod
	public List<SystemSetupParameter> getInitializationOptions() {
		final List<SystemSetupParameter> params = new ArrayList<>();

		params.add(createBooleanSystemSetupParameter(IMPORT_SAMPLE_DATA, "Import Sample Data", false));

		return params;
	}

	/**
	 * This method will be called by system creator during initialization and system update. Be sure that this method can
	 * be called repeatedly.
	 *
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = Type.ESSENTIAL, process = Process.ALL)
	public void createEssentialData(final SystemSetupContext context)
	{
		importImpexFile(context, "/textmastercore/import/core/essential-data.impex");
	}


	/**
	 * This method will be called during the system initialization.
	 *
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = Type.PROJECT, process = Process.ALL)
	public void createProjectData(final SystemSetupContext context)
	{
		final boolean importSampleData = getBooleanSystemSetupParameter(context, IMPORT_SAMPLE_DATA);

		if (importSampleData)
		{
			importImpexFile(context, "/textmastercore/import/sample/sample-data.impex");
		}
	}
}
