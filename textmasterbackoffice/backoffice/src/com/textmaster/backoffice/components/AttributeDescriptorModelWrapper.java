package com.textmaster.backoffice.components;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;

/**
 * Wrapper used in chosen box to display qualifier of attribute descriptor.
 */
public class AttributeDescriptorModelWrapper {

    private AttributeDescriptorModel attributeDescriptor;

    public AttributeDescriptorModelWrapper(AttributeDescriptorModel attributeDescriptor) {
        this.attributeDescriptor =attributeDescriptor;
    }

    public AttributeDescriptorModel getAttributeDescriptor() {
        return attributeDescriptor;
    }

    public void setAttributeDescriptor(AttributeDescriptorModel attributeDescriptor) {
        this.attributeDescriptor = attributeDescriptor;
    }

    @Override
    public String toString() {
        return this.attributeDescriptor.getQualifier();
    }
}
