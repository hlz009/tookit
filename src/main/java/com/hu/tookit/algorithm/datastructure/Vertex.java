package com.hu.tookit.algorithm.datastructure;

/**
 * 顶点
 * @author xiaozhi009
 *
 */
public class Vertex<V> {
	final static int INFINITY = (int) (1.0/0);

	V topNum;//存放拓扑编号
	int indegeree;
	// array 和 cvw 可以合并
	V[] array;// 存放邻接表的其他数据关系
	int[] cvw;// 权值关系，与array关系对应。
	
	int dist;// 距离， 初始默认为无穷大。
	boolean known = false;
	Vertex<V> path;

	Vertex(V topNum) {
		this(topNum, null, INFINITY, null);
	}

	Vertex(V topNum, V[] array, int dist) {
		this(topNum, array, dist, null);
	}

	Vertex(V topNum, V[] array, int[] cvw) {
		this(topNum, array, INFINITY, cvw);
	}

	Vertex(V topNum, V[] array, int dist, int[] cvw) {
		this.topNum = topNum;
		this.array = array;
		this.dist = dist;
		this.cvw = cvw;
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
		Vertex other = (Vertex) obj;
		if (topNum == null) {
			if (other.topNum != null)
				return false;
		} else if (!topNum.equals(other.topNum))
			return false;
		return true;
	}
}
