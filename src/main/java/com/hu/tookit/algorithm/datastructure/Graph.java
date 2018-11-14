package com.hu.tookit.algorithm.datastructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.hu.tookit.algorithm.SortCase;
import com.hu.tookit.algorithm.Exception.CycleFoundException;

/**
 * 图
 * @author xiaozhi009
 *
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class Graph {

	private HashMap<Object, Vertex> table;
	private volatile boolean init = false;
	
	public Graph() {
		table = new HashMap<>();
	}

	public void put(Object k, Object[] v) {
		put(k, v, 1);
	}

	public void put(Object k, Object[] v, int dist) {
		put(k, v, dist, null);
	}

	public void put(Object k, Object[] v, int[] cvw) {
		table.put(k, new Vertex(k, v, cvw));
	}

	public void put(Object k, Object[] v, int dist, int[] cvw) {
		table.put(k, new Vertex(k, v, dist, cvw));
	}

	public Vertex get(Object key) {
		return table.get(key);
	}

	/**
	 * 有向无圈图排序
	 */
	public void topSort() {
		initTop();
		BlockingQueue<Vertex> q = new LinkedBlockingQueue<Vertex>();
		for (Object key : table.keySet()) {
			Vertex v = table.get(key);
			if (v.indegeree == 0) {
				q.add(v);
				break;
			}
		}
		int counter = 0;
		while(!q.isEmpty()) {
			Vertex v = q.poll();
			System.out.println(v.topNum);
			v.topNum = ++counter;
			if (v.array != null)
				for (Object value: v.array) {
					Vertex w = table.get(value);
					if (--w.indegeree == 0) {
						q.add(w);
					}
				}
		}
		if (counter != table.size()) {
			throw new CycleFoundException();
		}
	}

	private void initTop() {
		if (!init) {
			for (Object key : table.keySet()) {
				Vertex v = table.get(key);
				setTopNum(v.topNum, v.array);
			}
			init = true;
		}
	}

	private void setTopNum(Object topNum, Object[] array) {
		if (array != null)
			for (Object obj: array) {
				Vertex w = table.get(obj);
				w.indegeree++;
			}
	}

	/**
	 * 无权最短路径
	 * @param s
	 */
	public void unWeighted(Vertex s) {
		initVertexPath();
		BlockingQueue<Vertex> q = new LinkedBlockingQueue<Vertex>();
		s.dist = 0;
		q.add(s);
		while (!q.isEmpty()) {
			Vertex v = q.poll();
			System.out.println("关键字：" + v.topNum + 
					"，路径长度" + v.dist);
			if (v.array != null)
				for (Object key: v.array) {
					Vertex w = table.get(key);
					if (w.dist == Vertex.INFINITY) {
						w.dist = v.dist + 1;
						w.path = v;
						q.add(w);
					}
				}
		}
	}

	private void initVertexPath() {
		for (Object key : table.keySet()) {
			Vertex v = table.get(key);
			v.dist = Vertex.INFINITY;
			v.known = false;
		} 
	}

	private List<Vertex> toList() {
		List<Vertex> result = new ArrayList<>();
		for (Object key : table.keySet()) {
			Vertex v = table.get(key);
			v.dist = Vertex.INFINITY;
			v.known = false;
			result.add(v);
		}
		return result;
	}

	private Vertex minList(List<Vertex> vertexs) {
		Vertex minV = null;
		for (Vertex v : vertexs) {
			v.dist = Vertex.INFINITY;
			if (!v.known && (minV == null || minV.dist > v.dist)){
				minV = v;
			}
		}
		return minV;
	}

	/**
	 * 赋权最短路径（Dijkstra算法不适合负值）
	 */
	public void weightedByDijkdtra(Vertex s) {
		weightedByDijkdtra(s, null);
	}


	public void weightedByDijkdtra(Vertex start, Vertex end) {
		initVertexPath();
		start.dist = 0;
		Vertex last = start;
		while (true) {
			Vertex v = minDist(table);
			if (v == null || (null != end && v.topNum.equals(end.topNum))) {
				if (v != null) {
					last = v;
				}
				break;
			}
			v.known = true;
			if (v.array != null) {
				for (int i = 0, len = v.array.length; i < len; i++) {
					Vertex w = table.get(v.array[i]);
					if (!w.known) {
						int minCvw = v.cvw[i];
						if (v.dist + minCvw < w.dist) {
							// 初始的dist 都是INFINITY
							w.dist = v.dist + minCvw;
							w.path = v;
						}
					}
				}				
			}
		}
		printVertex(last);
	}
	
	private Vertex minDist(HashMap<Object, Vertex> table) {
		// 进一步优化，可以采用堆排序，不过不能使用HashMap这个数据结构
		Vertex minV = null;
		for (Object key : table.keySet()) {
			Vertex v = table.get(key);
			if (!v.known && (minV == null || minV.dist > v.dist)){
				minV = v;
			}
		}
		return minV;
	}

	public void weightedByNegative(Vertex start) {
		weightedByNegative(start, null);
	}
	
	public void weightedByNegative(Vertex start, Vertex end) {
		initVertexPath();
		start.dist = 0;
		BlockingQueue<Vertex> q = new LinkedBlockingQueue<Vertex>();
		q.add(start);
		Vertex v = start;
		while (!q.isEmpty()) {
			v = q.poll();
			if (null != end && v.equals(end)) {
				break;
			}
			if (v.array != null) {
				for (int i = 0, len = v.array.length; i < len; i++) {
					Vertex w = table.get(v.array[i]);
					int minCvw = v.cvw[i];
					if (v.dist + minCvw < w.dist) {
						// 初始的dist 都是INFINITY
						w.dist = v.dist + minCvw;
						w.path = v;
						if (!q.contains(w)) {
							q.add(w);
						}
					}
				}
			}
		}
		printVertex(v);
	}

	
	private void printVertex(Vertex s) {
		if (s.path != null) {
			printVertex(s.path);
			System.out.println(" ---> ");
		}
		System.out.println(s.topNum);
	}

	/**
	 * 赋权有向无圈图排序，用于寻找关键路径，
	 * 常用在管理上的甘特图
	 */
	public void topSortWithWeight() {
		initTop();
		BlockingQueue<Vertex> q = new LinkedBlockingQueue<Vertex>();
		for (Object key : table.keySet()) {
			Vertex v = table.get(key);
			v.dist = 0;
			if (v.indegeree == 0) {
				q.add(v);
			}
		}
		int counter = 0;
		Vertex last = q.peek();
		while(!q.isEmpty()) {
			Vertex v = q.poll();
			System.out.print(v.topNum + "--->");
			counter++;
			if (v.array != null) {
				for (int i = 0, len = v.array.length; i < len; i++) {
					Vertex w = table.get(v.array[i]);
					if (v.dist + v.cvw[i] > w.dist) {
						w.dist = v.dist + v.cvw[i];
						w.path = v;
						last = w;
					}
					if (--w.indegeree == 0) {
						q.add(w);
					}
				}
			}
		}
		if (counter != table.size()) {
			throw new CycleFoundException();
		}
		System.out.print("\n , 关键路径为：\n");
		printVertex(last);
	}

	/**
	 * 最小生成树（用于无向图） 与Djikstra算法一样，
	 * 每一条边的关系 都要放在两个相邻表中（可以看做两个方向都合适）
	 * @param start
	 */
	public void generateMinSpanTree(Vertex start) {
		initVertexPath();
		start.dist = 0;
		Vertex last = start;
		while (true) {
			Vertex v = minDist(table);
			if (v == null) {
				break;
			}
			v.known = true;
			if (v.array != null) {
				for (int i = 0, len = v.array.length; i < len; i++) {
					Vertex w = table.get(v.array[i]);
					if (!w.known) {
						int minCvw = v.cvw[i];
						if (v.dist + minCvw < w.dist) {
							// 初始的dist 都是INFINITY
							w.dist = v.dist + minCvw;
							w.path = v;
						}
					}
				}
			}
			last = v;
		}
		printVertex(last);
	}

	/**
	 * 最小生成树（用于无向图，Kruskal算法，也是贪婪策略的一种），
	 * 每一条边的关系 都要放在两个相邻表中（可以看做两个方向都合适）
	 * 类似union/find
	 * @param start
	 */
	public void generateMinSpanTree2() {
		initVertexPath();
		int len = table.size(); // 转成边，N-1
		DisjSets ds = new DisjSets(len+1);// 第一个元素 0 没有使用
		List<Edge> mst = new ArrayList<>();
		List<Edge> q = toOrderQueue(table);
		int index = 0;
		while (mst.size() != len-1) {
			Edge e = q.get(index++);
			int u = ds.find((int)e.getU());
			int v = ds.find((int)e.getV());
			if (u != v) {
				mst.add(e);
				ds.union(u, v);
			}
		}
		System.out.println("连接的边数" + mst.size());
		System.out.println(mst);
	}

	private List<Edge> toOrderQueue(HashMap<Object, Vertex> table) {
		ArrayList<Edge> list = toQueue(table);
		Edge[] array = new Edge[list.size()];
		for (int i = 0; i < list.size(); i++) {
			array[i] = list.get(i);
		}
		return sort(array);
	}

	private ArrayList<Edge> toQueue(HashMap<Object, Vertex> table) {
		ArrayList<Edge> list = new ArrayList<>();
		for (Object key : table.keySet()) {
			Vertex v = table.get(key);
			for (int i = 0, len = v.array.length; i < len; i++) {
				Vertex w = table.get(v.array[i]);
				Edge e = new Edge(v, w, v.cvw[i]);
				list.add(e);
			}
		}
		return list;
	}

	private List<Edge> sort(Edge[] array) {
		Edge[] orderEdges = SortCase.heapSort(array);
		return Arrays.asList(orderEdges);
	}

	public static void main(String[] args) {
		Graph graph = new Graph();
//		graph.put(1, new Integer[]{2, 4}, new int[]{1, 2});
//		graph.put(2, new Integer[]{4, 5}, new int[] {3, 10});
//		graph.put(3, new Integer[]{7}, new int[] {1});
//		graph.put(4, new Integer[]{3}, new int[] {2});
//		graph.put(5, new Integer[]{7}, new int[] {6});
//		graph.put(6, null);
//		graph.put(7, new Integer[]{6}, new int[] {1});
//		graph.topSort();
//		graph.unWeighted(graph.get(3));
//		graph.weightedByDijkdtra(graph.get(1), graph.get(4));
//		graph.weightedByNegative(graph.get(1));
//		graph.put(1, new Integer[] {2, 3}, new int[] {3, 2});
//		graph.put(2, new Integer[] {4, 5}, new int[] {3, 2});
//		graph.put(3, new Integer[] {5, 6}, new int[] {2, 1});
//		graph.put(4, new Integer[] {7}, new int[] {3});
//		graph.put(5, new Integer[] {7}, new int[] {3});
//		graph.put(6, new Integer[] {8, 10}, new int[] {2, 4});
//		graph.put(7, new Integer[]{9}, new int[] {1});
//		graph.put(8, new Integer[]{9}, new int[] {1});
//		graph.put(9, null);
//		graph.put(10, new Integer[]{9}, new int[] {1});
//		graph.topSortWithWeight();
		graph.put(1, new Integer[] {2, 3, 4}, new int[] {2, 4, 1});
		graph.put(2, new Integer[] {1, 4, 5}, new int[] {2, 3, 10});
		graph.put(3, new Integer[] {1, 4, 6}, new int[] {4, 2, 5});
		graph.put(4, new Integer[] {1, 2, 3, 5, 6, 7}, new int[] {1, 3, 3, 7, 8, 4});
		graph.put(5, new Integer[] {2, 4, 7}, new int[] {10, 7, 6});
		graph.put(6, new Integer[] {3, 4, 7}, new int[] {5, 8, 1});
		graph.put(7, new Integer[] {4, 5, 6}, new int[] {4, 6, 1});
		graph.generateMinSpanTree2();
	}
}

