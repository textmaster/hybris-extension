package com.textmaster.core.utils;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Create a batch of data to be used in stream.
 *
 * @param <T>
 */
public class TextMasterBatchIterator<T> implements Iterator<List<T>>
{
	private int size;
	private List<T> currentBatch;
	private Iterator<T> sourceIterator;

	public TextMasterBatchIterator(Iterator<T> sourceIterator, int size)
	{
		this.size = size;
		this.sourceIterator = sourceIterator;
	}

	@Override
	public boolean hasNext()
	{
		currentBatch = new ArrayList<>(size);
		while (sourceIterator.hasNext() && currentBatch.size() < size)
		{
			currentBatch.add(sourceIterator.next());
		}

		return CollectionUtils.isNotEmpty(currentBatch);
	}

	@Override
	public List<T> next()
	{
		return currentBatch;
	}
}
