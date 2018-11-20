package com.hu.tookit.algorithm.datastructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 顶点抽象类
 * @author xiaozhi009
 *
 */
public abstract class AbstractVertex<V> {
	final static int INFINITY = (int) (1.0/0);

	V topNum;//存放拓扑编号
	int indegeree;
	// array 和 cvw 可以合并
	List<V> adjacList;// 存放邻接表的其他数据关系	
	int dist;// 距离， 初始默认为无穷大。

	AbstractVertex(V topNum) {
		this(topNum, null, INFINITY);
	}

	AbstractVertex(V topNum, V[] array) {
		this(topNum, array, INFINITY);
	}

	AbstractVertex(V topNum, V[] array, int dist) {
		this.topNum = topNum;
		if (array != null) {
			List<V> tmpList = Arrays.asList(array);
			this.adjacList = new ArrayList<>(tmpList);
		} else {
			this.adjacList = new ArrayList<>(); 
		}
		this.dist = dist;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((topNum == null) ? 0 : topNum.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractVertex other = (AbstractVertex) obj;
		if (topNum == null) {
			if (other.topNum != null)
				return false;
		} else if (!topNum.equals(other.topNum))
			return false;
		return true;
	}
}
