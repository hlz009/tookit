package com.hu.tookit.algorithm.datastructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * 搜索图
 * @author xiaozhi009
 *
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class SearchGraph implements Graph {
	private HashMap<Object, Vertex> table;
	private int counter;

	private class Vertex<V> extends AbstractVertex<V> {
		boolean visited = false;
		Vertex<V> parent;// 父路径
		List<V> tmpAdjacList;// 临时存放邻接表的其他数据关系
		int low;
		int num;

		Vertex(V topNum, V[] array, int dist) {
			super(topNum, array, dist);
			if (null != array) {
				// 只进行值的传递，不进行引用传递
				// 用LinkedList获取好些
				this.tmpAdjacList = new ArrayList<>(this.adjacList);
			} else {
				this.tmpAdjacList = new ArrayList<>();
			}
		}

		@Override
		public String toString() {
			return "Vertex: " + topNum;
		}
	}

	private void initAdjacencyArray() {
		for (Object key : table.keySet()) {
			Vertex v = table.get(key);
			v.adjacList = new ArrayList<>(v.tmpAdjacList);
		}
	}

	public SearchGraph() {
		table = new HashMap<>();
		counter = 1;
	}

	@Override
	public void put(Object k, Object[] v) {
		put(k, v, 1);
	}

	@Override
	public void put(Object k, Object[] v, int dist) {
		table.put(k, new Vertex(k, v, dist));
	}

	@Override
	public Vertex get(Object key) {
		return table.get(key);
	}

	public void findArtiPoint(Vertex v) {
		initAdjacencyArray();
		executeFindArtiPoint(v);
	}
	/**
	 * 检测深度搜索下路径的割点
	 * @param v
	 */
	public void executeFindArtiPoint(Vertex v) {
		v.visited = true;
		v.low = v.num = counter++;
		if (isNotNull(v.adjacList))
			for (Object k: v.adjacList) {
				Vertex w = table.get(k);
				if (!w.visited) {// or 条件改为w.num > v.num
					w.parent = v;
					executeFindArtiPoint(w);
					if (w.low >= v.num) {
						System.out.println("vertex " + v.topNum + " is an articulation point"
								+ ", num: " + v.num + ", low:" + v.low);
					}
					v.low = min(v.low, w.low);
				} else {
					if (v.parent != w) {// 背向边
						v.low = min(v.low, w.num);
					}
				}
			}
	}

	/**
	 * 无向图欧拉回路
	 */
	private Stack<Vertex> stack;
	public void findEulerCircuit (Vertex v) {
		initAdjacencyArray();
		stack = new Stack<>();
		executeFindEulerCircuit(v);
		printStack(stack);
	}

	private void executeFindEulerCircuit (Vertex v) {
		while (isNotNull(v.adjacList)) {
			Vertex w = table.get(v.adjacList.get(0));
			v.adjacList.remove(0);
			w.adjacList.remove(v.topNum);
			executeFindEulerCircuit(w);
		}
		stack.add(v);
	}

	private <E>void printCollect(Collection<E> co) {
		StringBuffer sbf = new StringBuffer();
		Iterator<E> ite = co.iterator();
		while(ite.hasNext()) {
			sbf.append(ite.next() + "\t");
		}
		System.out.println(sbf.toString());
	}

	private int min(int first, int second) {
		return Math.min(first, second);
	}

	private boolean isNotNull(List<Object> list) {
		return list != null && !list.isEmpty();
	}

	/**
	 * 有向图欧拉回路0
	 */
	private Stack<Vertex> stackWithPath;
	private List<Stack<Vertex>> list;
	public void findAndPrintEulerCircuit (Vertex v) {
		findEulerCircuitWithPath(v);
		for (Stack<Vertex> s : list) {
			printStack(s);
		}
	}

	public void findEulerCircuitWithPath (Vertex v) {
//		initAdjacencyArray();
		list = new ArrayList<>();
		Vertex nextUnsignedVertex = v;
		while (true) {
			stackWithPath = new Stack<>();
			executeFindEulerCircuitWithPath(nextUnsignedVertex, stackWithPath);
			list.add(stackWithPath);
			nextUnsignedVertex = getUnsignedVertex();
			if (null == nextUnsignedVertex) {
				break;
			}
		}
	}

	public void executeFindEulerCircuitWithPath(Vertex v, Collection<Vertex> cc) {
		v.visited = true;
		while (isNotNull(v.adjacList)) {
			Vertex w = table.get(v.adjacList.get(0));
			v.adjacList.remove(0);
			if (!w.visited) {
				executeFindEulerCircuitWithPath(w, cc);
			}
		}
		cc.add(v);
	}

	private Vertex getUnsignedVertex() {
		for (Object key : table.keySet()) {
			Vertex v = table.get(key);
			if (!v.visited) {
				return v;
			}
		}
		return null;
	}

	/**
	 * 有向图查找强分支列表
	 * @param v
	 */
	public void findStrongBranch(Vertex v) {
		findEulerCircuitWithPath(v);
		reverseAdjacencyArray();
		List<List<Vertex>> sCircuitTree = new ArrayList<>();
		List<Stack<Vertex>> vList = list;
		initVisited();
		// 一定要逆序（后序）遍历,
		for (int i = vList.size() - 1; i >= 0; i--) {
			Stack<Vertex> s = vList.get(i);
			while (!s.isEmpty()) {
				Vertex current = s.pop();
				if (!current.visited) {
					List<Vertex> vertexList = new ArrayList<>();
					executeFindEulerCircuitWithPath(current, vertexList);
					sCircuitTree.add(vertexList);
				}
			}
		}
		
		printCollect(sCircuitTree);
	}

	private void initVisited() {
		for (Object key : table.keySet()) {
			Vertex v = table.get(key);
			v.visited = false;
		}
	}

	private void reverseAdjacencyArray() {
		for (Object key : table.keySet()) {
			Vertex v = table.get(key);
			if (isNotNull(v.tmpAdjacList)) {
				for (Object k: v.tmpAdjacList) {
					Vertex w = table.get(k);
					w.adjacList.add(v.topNum);
				}
			}
		}
	}

	private <T>void printStack(Stack<T> stack) {
		StringBuffer sbf = new StringBuffer();
		while(!stack.isEmpty()) {
			sbf.append(stack.pop() + "\t");
		}
		System.out.println(sbf.toString());
	}
	
	public static void main(String[] args) {
		SearchGraph searchGraph = new SearchGraph();
//		searchGraph.put(1, new Integer[] {2, 7});
//		searchGraph.put(2, new Integer[] {5, 3});
//		searchGraph.put(3, new Integer[] {4});
//		searchGraph.put(4, null);
//		searchGraph.put(5, new Integer[] {6});
//		searchGraph.put(6, null);
//		searchGraph.put(7, null);
//		searchGraph.findArtiPoint(searchGraph.get(1));
		
//		searchGraph.put(1, new Integer[] {3, 4});
//		searchGraph.put(2, new Integer[] {3, 8});
//		searchGraph.put(3, new Integer[] {1, 2, 6, 9, 7, 4});
//		searchGraph.put(4, new Integer[] {1, 3, 5, 7, 10, 11});
//		searchGraph.put(5, new Integer[] {4, 10});
//		searchGraph.put(6, new Integer[] {3, 9});
//		searchGraph.put(7, new Integer[] {3, 4, 9, 10});
//		searchGraph.put(8, new Integer[] {2, 9});
//		searchGraph.put(9, new Integer[] {3, 6, 7, 8, 10, 12});
//		searchGraph.put(10, new Integer[] {4, 5, 7, 9, 11, 12});
//		searchGraph.put(11, new Integer[] {4, 10});
//		searchGraph.put(12, new Integer[] {9, 10});
//		searchGraph.findEulerCircuit(searchGraph.get(5));
		
		searchGraph.put(1, new Integer[] {2, 3, 4});
		searchGraph.put(2, new Integer[] {3, 6});
		searchGraph.put(3, new Integer[] {1, 4, 5});
		searchGraph.put(4, new Integer[] {5});
		searchGraph.put(5, null);
		searchGraph.put(6, new Integer[] {3});
		searchGraph.put(7, new Integer[] {6, 8});
		searchGraph.put(8, new Integer[] {10});
		searchGraph.put(9, new Integer[] {8});
		searchGraph.put(10, new Integer[] {9});
//		searchGraph.findAndPrintEulerCircuit(searchGraph.get(2));
		searchGraph.findStrongBranch(searchGraph.get(2));
	}
}
