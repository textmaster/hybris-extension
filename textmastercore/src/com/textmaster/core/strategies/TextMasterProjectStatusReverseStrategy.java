package com.textmaster.core.strategies;

import com.textmaster.core.enums.TextMasterProjectStatusEnum;


/**
 * Apply status conversion between datamodel statuses and TextMaster statuses
 */
public interface TextMasterProjectStatusReverseStrategy
{

    /**
     * Convert status.
     *
     * @param status
     * @return
     */
    public TextMasterProjectStatusEnum translate(String status);
}
