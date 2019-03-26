package com.textmaster.backoffice.components;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.TreeNode;

import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ComposedTypeTreeNode extends DefaultTreeNode<ComposedTypeModel> {

    public ComposedTypeTreeNode(ComposedTypeModel composedType) {
        super(composedType, new LinkedList<>());
    }

    public ComposedTypeTreeNode(ComposedTypeModel composedType, ComposedTypeTreeNodeCollection<ComposedTypeModel> subTypes) {
        super(composedType, subTypes);
    }

    public String getName() {
        return getData().getName();
    }

    public boolean isLeaf() {
        return getData() != null && getData().getSubtypes().isEmpty();
    }
}
