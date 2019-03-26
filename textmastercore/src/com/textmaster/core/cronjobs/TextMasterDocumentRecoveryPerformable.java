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
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Recover documents from TextMaster remote platform for local documents which are not a remote ID defined.
 *
 * It is useful in case of lost of network connection, when a document has been pushed into TextMaster but not
 * referenced locally.
 */
public class TextMasterDocumentRecoveryPerformable extends AbstractJobPerformable<CronJobModel> {

    private final static Logger LOG = LoggerFactory.getLogger(TextMasterDocumentRecoveryPerformable.class);

    private TextMasterProjectService textMasterProjectService;

    @Override
    public PerformResult perform(CronJobModel cronJobModel) {

        // Prepare project statuses
        List<TextMasterProjectModel> projects = getProjectWithDocumentsToRecover();

        // Find all documents filter by their status
        for (TextMasterProjectModel project : projects) {
            getTextMasterProjectService().recoverDocuments(project);
        }

        // TODO: Performance problem ?

        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }

    /**
     * Get projects which contains document without remote id
     *
     * @return
     */
    protected List<TextMasterProjectModel> getProjectWithDocumentsToRecover() {
        List<TextMasterProjectStatusEnum> projectStatuses = getTextMasterProjectService().getProjectAvailableStatuses();

        // Take in account all projects open
        List<TextMasterProjectModel> projects = getTextMasterProjectService().getProjects(projectStatuses);

        return projects.stream()
                .filter(p -> p.getDocuments().stream()
                                .anyMatch(d -> StringUtils.isBlank(d.getRemoteId())))
                .collect(Collectors.toList());
    }


    protected TextMasterProjectService getTextMasterProjectService() {
        return textMasterProjectService;
    }

    @Required
    public void setTextMasterProjectService(TextMasterProjectService textMasterProjectService) {
        this.textMasterProjectService = textMasterProjectService;
    }

    protected ModelService getModelService() {
        return modelService;
    }
}
