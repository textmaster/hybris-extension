package com.textmaster.core.tasks;

import com.textmaster.core.model.TextMasterProjectModel;
import com.textmaster.core.model.TextMasterProjectTaskModel;
import com.textmaster.core.services.TextMasterProjectService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;


/**
 * Push content of project on TextMaster platform.
 * <p>
 * Send documents through many batches and finalize project at the end. If an error occured, retry later,
 * so this task could be called many times until the project has been completely created on remote platform.
 */
public class TextMasterProjectPushContentTaskRunner implements TaskRunner<TaskModel>
{
	private static final Logger LOG = LoggerFactory.getLogger(TextMasterProjectPushContentTaskRunner.class);

	private ConfigurationService configurationService;
	private TextMasterProjectService textMasterProjectService;
	private ModelService modelService;
	private TaskService taskService;

	@Override
	public void run(TaskService taskService, TaskModel taskModel) throws RetryLaterException
	{
		TextMasterProjectTaskModel currentTask = (TextMasterProjectTaskModel) taskModel;

		TextMasterProjectModel project = currentTask.getTextMasterProject();

		int interval = getConfigurationService().getConfiguration().getInt("textmastercore.project.send.retry.interval");

		try
		{
			// Synchronize documents to be sure that all documents already pushed have their corresponding remote ID
			getTextMasterProjectService().recoverDocuments(project);

			// Send batches of documents
			getTextMasterProjectService().sendAndReceiveDocuments(project);

			// Schedule the task to finalize project and get all calculated data
			Date nextTime = DateUtils.addMinutes(new Date(), interval);
			final TextMasterProjectTaskModel task = getModelService().create(TextMasterProjectTaskModel.class);
			task.setRunnerBean("textMasterProjectFinalizeTaskRunner");
			task.setExecutionDate(nextTime);
			task.setTextMasterProject(project);

			getModelService().save(task);
			getTaskService().scheduleTask(task);
		}
		catch (Exception e)
		{
			LOG.error("An error occured during sending documents: {}", e.getMessage(), e);

			// Retry later
			RetryLaterException retryLaterException = new RetryLaterException("Re-scheduling currentTask of documents sending");
			retryLaterException.setRollBack(false);
			retryLaterException.setDelay(interval * 60 * 1000);
			throw retryLaterException;
		}
	}

	@Override public void handleError(TaskService taskService, TaskModel taskModel, Throwable throwable)
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

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(ModelService modelService)
	{
		this.modelService = modelService;
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
}
