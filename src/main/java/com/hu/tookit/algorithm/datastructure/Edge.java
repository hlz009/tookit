package com.hu.tookit.algorithm.datastructure;

/**
 * 边
 * @author xiaozhi009
 *
 */
public class Edge<T> implements Comparable<T> {
	AbstractVertex<T> u;
	AbstractVertex<T> v;
	
	int dist;

	public Edge(AbstractVertex<T> u, AbstractVertex<T> v, int dist) {
		this.u = u;
		this.v = v;
		this.dist = dist;
	}

	public T getU() {
		return u.topNum;
	}

	public T getV() {
		return v.topNum;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dist;
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
		Edge other = (Edge) obj;
		if (dist != other.dist)
			return false;
		return true;
	}

	@Override
	public int compareTo(Object o) {
		if (getClass() != o.getClass())
			return -1;
		Edge other = (Edge) o;
		return dist - other.dist;
	}

	@Override
	public String toString() {
		return "Edge [u=" + u + ", v=" + v + ", dist=" + dist + "]";
	}
}
