package com.textmaster.backoffice.widgets.actions.validate;

import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import com.textmaster.core.enums.TextMasterDocumentStatusEnum;
import com.textmaster.core.enums.TextMasterProjectStatusEnum;
import com.textmaster.core.exceptions.TextMasterException;
import com.textmaster.core.model.TextMasterDocumentModel;
import com.textmaster.core.model.TextMasterProjectModel;
import com.textmaster.core.services.TextMasterDocumentService;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class ValidateDocumentsAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<Object, Object>
{
	private static final Logger LOG = LoggerFactory.getLogger(ValidateDocumentsAction.class);

	@Resource
	private TextMasterDocumentService textMasterDocumentService;

	@Resource
	private ModelService modelService;

	@Resource
	private NotificationService notificationService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ActionResult<Object> perform(final ActionContext<Object> ctx)
	{
		ActionResult result = new ActionResult("success");

		List<TextMasterDocumentModel> documents = new ArrayList();
		if (ctx.getData() instanceof Collection)
		{
			documents.addAll((Collection) ctx.getData());
		}
		else
		{
			documents.add((TextMasterDocumentModel) ctx.getData());
		}

		// Filter to keep only document in review
		documents = documents
				.stream()
				.filter(d -> d.getStatus() == TextMasterDocumentStatusEnum.IN_REVIEW)
				.collect(Collectors.toList());

		if (CollectionUtils.isNotEmpty(documents))
		{
			// Use project info of first element found (infos are the same for all documents)
			TextMasterProjectModel project = documents.stream().findFirst().get().getProject();

			try
			{
				getTextMasterDocumentService().completeDocuments(project, documents);

				getNotificationService().notifyUser("ValidateDocumentsAction", "TextMasterGeneral", NotificationEvent.Level.SUCCESS, ctx.getLabel("documents.validated"));

				// Change all documents statuses to COMPLETED
				documents = documents
						.stream()
						.map(d -> {
							d.setStatus(TextMasterDocumentStatusEnum.COMPLETED);
							return d;
						})
						.collect(Collectors.toList());
				getModelService().saveAll(documents);

				// If all project documents has been completed, complete entire project
				// Refresh projects list if the project has been completed, documents list otherwise
				getModelService().refresh(project);
				if (project.getDocuments().stream().allMatch(d -> d.getStatus() == TextMasterDocumentStatusEnum.COMPLETED))
				{
					project.setStatus(TextMasterProjectStatusEnum.COMPLETED);
					getModelService().save(project);
					sendOutput("account", project.getAccount());
				}
				else
				{
					sendOutput("project", project);
				}
			}
			catch (TextMasterException e)
			{
				LOG.error("Impossible to complete documents: {}", e.getMessage());

				getNotificationService().notifyUser("ValidateDocumentsAction", "TextMasterGeneral", NotificationEvent.Level.FAILURE, ctx.getLabel("documents.notvalidated"));

				result = new ActionResult("error");
			}
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canPerform(final ActionContext<Object> var1)
	{
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getConfirmationMessage(final ActionContext<Object> var1)
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean needsConfirmation(final ActionContext<Object> var1)
	{
		return false;
	}

	protected TextMasterDocumentService getTextMasterDocumentService()
	{
		return textMasterDocumentService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected NotificationService getNotificationService()
	{
		return notificationService;
	}
}
