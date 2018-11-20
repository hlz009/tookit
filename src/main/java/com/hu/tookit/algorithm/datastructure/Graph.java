package com.hu.tookit.algorithm.datastructure;

/**
 * 图
 * @author xiaozhi009
 *
 */
public interface Graph {

	public void put(Object k, Object[] v);

	public void put(Object k, Object[] v, int dist);

	public AbstractVertex get(Object key);
}

