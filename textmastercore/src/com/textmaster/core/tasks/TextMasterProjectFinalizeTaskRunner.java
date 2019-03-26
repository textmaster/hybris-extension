package com.textmaster.core.tasks;

import com.textmaster.core.dtos.TextMasterDocumentDto;
import com.textmaster.core.dtos.TextMasterProjectResponseDto;
import com.textmaster.core.model.TextMasterAccountModel;
import com.textmaster.core.model.TextMasterDocumentModel;
import com.textmaster.core.model.TextMasterProjectModel;
import com.textmaster.core.model.TextMasterProjectTaskModel;
import com.textmaster.core.services.TextMasterDocumentService;
import com.textmaster.core.services.TextMasterProjectService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Finalize project content of project on TextMaster platform.
 * <p>
 * Get all word count of documents and projet, and finalize project at the end. If not all documents have been calculated, retry
 * later. If an error occured, retry later.
 * So this task could be called many times until the project has been completely created on remote platform.
 */
public class TextMasterProjectFinalizeTaskRunner implements TaskRunner<TaskModel>
{
	private static final Logger LOG = LoggerFactory.getLogger(TextMasterProjectFinalizeTaskRunner.class);

	private ConfigurationService configurationService;
	private TextMasterProjectService textMasterProjectService;
	private TextMasterDocumentService textMasterDocumentService;
	private TaskService taskService;
	private ModelService modelService;

	@Override

	public void run(TaskService taskService, TaskModel taskModel) throws RetryLaterException
	{
		int interval = getConfigurationService().getConfiguration().getInt("textmastercore.project.finalize.schedule");

		TextMasterProjectTaskModel task = (TextMasterProjectTaskModel) taskModel;
		TextMasterProjectModel project = task.getTextMasterProject();

		try
		{
			TextMasterAccountModel account = project.getAccount();

			Map<String, Object> params = Collections.singletonMap("word_count", Collections.singletonMap("$gt", 0));

			List<TextMasterDocumentDto> remoteDocuments = getTextMasterDocumentService()
					.filterDocuments(project, params);

			// Update documents without word count
			Collection<TextMasterDocumentModel> documentsToSave = project.getDocuments()
					.stream()
					.filter(d -> d.getWordCount() == 0)
					.map(d -> searchAndSetWordCount(remoteDocuments, d))
					.filter(d -> d != null)
					.collect(Collectors.toList());

			if (CollectionUtils.isNotEmpty(documentsToSave))
			{
				getModelService().saveAll(documentsToSave);
			}

			// If at least one document has not been calculated on TextMaster platform, retry later
			if (project.getDocuments()
					.stream()
					.anyMatch(d -> d.getWordCount() == 0))
			{
				LOG.error("At least one document has not been calculated yet on TextMaster platform, retry later");
				throw retry(task);
			}

			// Update project
			TextMasterProjectResponseDto remoteProject = getTextMasterProjectService().getProject(project);
			project.setTotalWordCount(remoteProject.getTotalWordCount());
			project.setPrice(BigDecimal.valueOf(remoteProject.getTotalCosts().stream().findFirst().get().getAmount()));
			project.setCurrencyIsocode(remoteProject.getTotalCosts().stream().findFirst().get().getCurrency());
			getModelService().save(project);

			// Finalize project
			getTextMasterProjectService().finalize(project);

			// Add flag to project to indicates that the finalize process has been executed
			project.setFinalized(true);
			getModelService().save(project);

			// If the project is configured with autolaunch, start a new task to update project status
			// Schedule the task to manage status for autolaunch project
			if (project.getAutolaunch())
			{
				startAutolaunchTask(project);
			}
		}
		catch (Exception e)
		{
			LOG.error("An error occured during finalizing project: {}", e.getMessage(), e);
			throw retry(task);
		}
	}

	/**
	 * Try to get project status later, following the autolaunch process
	 *
	 * @param project
	 */
	protected void startAutolaunchTask(TextMasterProjectModel project)
	{
		int autolaunchInterval = getConfigurationService().getConfiguration().getInt("textmastercore.project.autolaunch.schedule");
		Date nextTime = DateUtils.addMinutes(new Date(), autolaunchInterval);
		final TextMasterProjectTaskModel autolaunchTask = getModelService().create(TextMasterProjectTaskModel.class);
		autolaunchTask.setRunnerBean("textMasterProjectAutolaunchTaskRunner");
		autolaunchTask.setExecutionDate(nextTime);
		autolaunchTask.setTextMasterProject(project);
		getModelService().save(autolaunchTask);
		getTaskService().scheduleTask(autolaunchTask);
	}

	/**
	 * Retry task execution.
	 *
	 * @param task
	 */
	protected RetryLaterException retry(final TaskModel task)
	{
		LOG.error("An error occured during finalize task: [{}]", task.getPk());

		int interval = getConfigurationService().getConfiguration().getInt("textmastercore.project.finalize.schedule");

		// Retry later
		RetryLaterException retryLaterException = new RetryLaterException("Re-scheduling task of project finalize");
		retryLaterException.setRollBack(false);
		retryLaterException.setDelay(interval * 60 * 1000);
		return retryLaterException;
	}

	/**
	 * Search, define word count and return document.
	 *
	 * @param remoteDocuments
	 * @param projectDocument
	 * @return
	 */
	protected TextMasterDocumentModel searchAndSetWordCount(List<TextMasterDocumentDto> remoteDocuments,
			TextMasterDocumentModel projectDocument)
	{
		Optional<TextMasterDocumentDto> remoteDocument = remoteDocuments
				.stream()
				.filter(rd -> rd.getId().equalsIgnoreCase(projectDocument.getRemoteId()))
				.findFirst();

		if (!remoteDocument.isPresent())
		{
			return null;
		}

		projectDocument.setWordCount(remoteDocument.get().getWordCount());
		return projectDocument;
	}

	@Override
	public void handleError(TaskService taskService, TaskModel taskModel, Throwable throwable)
	{
		LOG.error("Failed to run task '{}' (context: {}).", taskModel, taskModel.getContext(), throwable);
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setConfigurationService(ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
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

	protected TaskService getTaskService()
	{
		return taskService;
	}

	@Required
	public void setTaskService(TaskService taskService)
	{
		this.taskService = taskService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected TextMasterDocumentService getTextMasterDocumentService()
	{
		return textMasterDocumentService;
	}

	@Required
	public void setTextMasterDocumentService(TextMasterDocumentService textMasterDocumentService)
	{
		this.textMasterDocumentService = textMasterDocumentService;
	}
}
