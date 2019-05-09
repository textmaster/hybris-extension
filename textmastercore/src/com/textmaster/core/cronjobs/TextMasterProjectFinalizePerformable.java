package com.textmaster.core.cronjobs;

import com.textmaster.core.enums.TextMasterProjectStatusEnum;
import com.textmaster.core.model.TextMasterProjectModel;
import com.textmaster.core.services.TextMasterProjectService;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Take in account all projects not finalized yet on hybris side and check if there are remote finalized.
 * If it is the case, update hybris data.
 */
public class TextMasterProjectFinalizePerformable extends AbstractJobPerformable<CronJobModel>
{

	private final static Logger LOG = LoggerFactory.getLogger(TextMasterProjectFinalizePerformable.class);

	private TextMasterProjectService textMasterProjectService;

	@Override
	public PerformResult perform(CronJobModel cronJobModel)
	{
		// Get projects not finalized
		List<TextMasterProjectModel> projects = getProjectNotFinalized();

		for (TextMasterProjectModel project : projects)
		{
			// Update project
			getTextMasterProjectService().updateProject(project);
		}

		// TODO: Performance problem ?

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	/**
	 * Get projects which contains document without remote id
	 *
	 * @return
	 */
	protected List<TextMasterProjectModel> getProjectNotFinalized()
	{
		List<TextMasterProjectStatusEnum> projectStatuses = getTextMasterProjectService().getProjectAvailableStatuses();

		// Take in account all projects open
		List<TextMasterProjectModel> projects = getTextMasterProjectService().getProjects(projectStatuses);

		// If not finalized OR (finalized AND translation memory not done)
		return projects.stream()
				.filter(p -> !p.getFinalized() || (p.getFinalized() && p.getTranslationMemoryActivated() && !p.getTranslationMemoryFinished()))
				.collect(Collectors.toList());
	}

	protected TextMasterProjectService getTextMasterProjectService()
	{
		return textMasterProjectService;
	}

	@Required
	public void setTextMasterProjectService(TextMasterProjectService textMasterProjectService)
	{
		this.textMasterProjectService = textMasterProjectService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}
}
