package com.textmaster.core.daos;

import com.textmaster.core.enums.TextMasterProjectStatusEnum;
import com.textmaster.core.model.TextMasterProjectModel;

import java.util.List;

/**
 * Access to project data.
 */
public interface TextMasterProjectDao {

    /**
     * Find a all projects for statuses.
     *
     * @param statuses
     * @return
     */
    public List<TextMasterProjectModel> findProjects(List<TextMasterProjectStatusEnum> statuses);
}
