package com.textmaster.core.tasks;

import com.textmaster.core.dtos.TextMasterProjectResponseDto;
import com.textmaster.core.enums.TextMasterDocumentStatusEnum;
import com.textmaster.core.enums.TextMasterProjectStatusEnum;
import com.textmaster.core.model.TextMasterAccountModel;
import com.textmaster.core.model.TextMasterDocumentModel;
import com.textmaster.core.model.TextMasterProjectModel;
import com.textmaster.core.model.TextMasterProjectTaskModel;
import com.textmaster.core.services.TextMasterProjectService;
import com.textmaster.core.strategies.TextMasterProjectStatusReverseStrategy;
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

import java.util.*;
import java.util.stream.Collectors;


/**
 * Check the project status.
 * <br/>
 * In case of the project is configured with autolaunch, try regularly to check status to update local project.
 */
public class TextMasterProjectAutolaunchTaskRunner implements TaskRunner<TaskModel>
{
	private static final Logger LOG = LoggerFactory.getLogger(TextMasterProjectAutolaunchTaskRunner.class);

	private ConfigurationService configurationService;
	private TextMasterProjectService textMasterProjectService;
	private TaskService taskService;
	private ModelService modelService;
	private TextMasterProjectStatusReverseStrategy textMasterProjectStatusReverseStrategy;

	@Override
	public void run(TaskService taskService, TaskModel taskModel) throws RetryLaterException
	{
		int max = getConfigurationService().getConfiguration().getInt("textmastercore.project.autolaunch.retry.max");

		TextMasterProjectTaskModel task = (TextMasterProjectTaskModel) taskModel;
		TextMasterProjectModel project = task.getTextMasterProject();

		try
		{
			TextMasterAccountModel account = project.getAccount();

			if (project.getAutolaunch() && TextMasterProjectStatusEnum.IN_PROGRESS != project.getStatus())
			{
				// Get project
				TextMasterProjectResponseDto projectDto = getTextMasterProjectService().getProject(project);

				// If project status on TextMaster platform is not IN_PROGRESS, retry later.
				TextMasterProjectStatusEnum status = getTextMasterProjectStatusReverseStrategy().translate(projectDto.getStatus());
				if (TextMasterProjectStatusEnum.IN_PROGRESS != status && task.getRetry() <= max)
				{
					throw retry(task);
				}

				// Otherwise, update project status
				project.setStatus(status);
				getModelService().save(project);
				
				// Update all documents to IN_PROGRESS status. No cronjob manage all document statuses to avoid bad performance
				List<TextMasterDocumentModel> documents = project.getDocuments()
						.stream()
						.map(d -> {
							d.setStatus(TextMasterDocumentStatusEnum.IN_PROGRESS);
							return d;
						})
						.collect(Collectors.toList());
				getModelService().saveAll(documents);
			}
		}
		catch (Exception e)
		{
			LOG.error("An error occured during getting status of project: {}", e.getMessage(), e);
			throw retry(task);
		}
	}

	/**
	 * Retry task execution.
	 *
	 * @param task
	 */
	protected RetryLaterException retry(final TaskModel task)
	{
		LOG.error("An error occured during autolaunch task [{}]", task.getPk());

		int interval = getConfigurationService().getConfiguration().getInt("textmastercore.project.autolaunch.schedule");

		// Retry later
		RetryLaterException retryLaterException = new RetryLaterException("Re-scheduling task of project autolaunch");
		retryLaterException.setRollBack(false);
		retryLaterException.setDelay(interval * 60 * 1000);
		return retryLaterException;
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

	protected TextMasterProjectStatusReverseStrategy getTextMasterProjectStatusReverseStrategy()
	{
		return textMasterProjectStatusReverseStrategy;
	}

	@Required
	public void setTextMasterProjectStatusReverseStrategy(
			TextMasterProjectStatusReverseStrategy textMasterProjectStatusReverseStrategy)
	{
		this.textMasterProjectStatusReverseStrategy = textMasterProjectStatusReverseStrategy;
	}
}
