package com.hu.tookit.algorithm.datastructure;

/**
 * 二叉堆 --- 一种优先队列
 * 用数组实现
 * 对任意i节点， i/2 父节点，2i 左子节点，2i+1 右子节点
 * @author xiaozhi009
 *
 */
public class BinaryHeap<T extends Comparable<? super T>> {
	private transient int size;
	private transient T[] table;
	private static final int DEFAUT_SIZE = 16;

	public BinaryHeap() {
		this(DEFAUT_SIZE);
	}

	public BinaryHeap(int size) {
		table = (T[]) new Comparable[size];
	}

	public BinaryHeap(T[] items) {
		size = items.length;
		table = (T[]) new Comparable[(size+2)*11/10];
		int i = 1;
		for(T item: items) {
			table[i++] = item;
		}
		buildHeap();
	}
	
	private void buildHeap() {
		for (int i = size/2; i > 0; i--) {
			percolateDown(i, size);
		}
	}

	public void insert(T data) {
		// 下标0 不保存元素
		int hole = ++size;
		expand();
		for (table[0] = data; data.compareTo(table[hole/2]) < 0; hole/=2) {
			table[hole] = table[hole/2];
		}
		table[hole] = data;
	}

	private void expand() {
		if (size >= table.length-1) {
			T[] oldTable = table;
			table = (T[]) new Comparable[2*table.length];
			for (int i = 1, len = oldTable.length; i <len; i++) {
				table[i] = oldTable[i];
			}
		}
	}
	public void remove(T data) {
		decreaseKey(data, size);
		removeMin();
	}

	/**
	 * 将第一个元素（最小的）删除，
	 * 最后一个元素放到第一个元素的位置，
	 * 下滤操作
	 */
	public void removeMin() {
		table[1] = table[size--];
		percolateDown(1, size);
	}

	public int size() {
		return size;
	}

	public void decreaseKey(T data, int change) {
		int index = getIndex(data);
		if (index == -1 || change <= 0) {
			return;
		}
		int hole = Math.max(index/(1 << change), 1);// 实际达到的位置
		T tmp = table[index];
		table[index] = table[hole];
		table[hole] = tmp;
		/**
		 * 到达hole的位置，就不在替换，hole达不到的边界，
		 * 要选定其子节点。可以定位2*hole
		 */
		percolateUp(index, 2*hole);
	}
	
	private int getIndex(T data) {
		for (int i = 1, len = table.length; i < len; i++) {
			if (data.equals(table[i])) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 上滤
	 * @param hole
	 * @param size 上滤到指定位置
	 */
	private void percolateUp(int hole, int size) {
		int parent;
		T temp = table[hole];
		while (hole/2 >= size) {
			parent = hole/2;
//			if (parent != size &&
//					table[parent+1].compareTo(table[parent]) < 0) {
//				parent++;
//			}
			if (table[parent].compareTo(temp) > 0) {
				table[hole] = table[parent];
				hole = parent;
			} else {
				break;
			}
		}
		table[hole] = temp;
	}

	/**
	 * 下滤
	 * 对于一个完全树，如果缺少一个子节点，
	 * 应该是在最后的位置
	 * @param hole
	 * @param size 下滤到指定位置
	 */
	private void percolateDown(int hole, int size) {
		int child;
		T temp = table[hole];
		while (hole*2 <= size) {
			child = hole*2;
			if (child != size &&
					table[child+1].compareTo(table[child]) < 0) {
				child++;
			}
			if (table[child].compareTo(temp) < 0) {
				table[hole] = table[child];
				hole = child;
			} else {
				break;
			}
		}
		table[hole] = temp;
	}

	@Override
	public String toString() {
		String s = "";
		for (int i = 1, len = table.length; i < len; i++) {
			if (null != table[i]) {
				s += table[i].toString() + ",";
			}
		}
		return s;
	}

	public static void main(String[] args) {
//		BinaryHeap<Integer> bh = new BinaryHeap<>();
//		bh.insert(150);
//		bh.insert(80);
//		bh.insert(40);
//		bh.insert(30);
//		bh.insert(10);
//		bh.insert(70);
//		bh.insert(110);
//		bh.insert(100);
//		bh.insert(20);
//		bh.insert(90);
//		bh.insert(60);
//		bh.insert(50);
//		bh.insert(120);
//		bh.insert(140);
//		bh.insert(130);
		
		Integer[] s = new Integer[15];
		s[0] = 150;
		s[1] = 80;
		s[2] = 40;
		s[3] = 30;
		s[4] = 10;
		s[5] = 70;
		s[6] = 110;
		s[7] = 100;
		s[8] = 20;
		s[9] = 90;
		s[10] = 60;
		s[11] = 50;
		s[12] = 120;
		s[13] = 140;
		s[14] = 130;
//		bh.decreaseKey(130, 2);
		BinaryHeap<Integer> bh = new BinaryHeap<>(s);
		System.out.println(bh);
	}
}
