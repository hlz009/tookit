package com.hu.tookit.algorithm.datastructure;

/**
 * 最小，最大值堆
 * 结构与二叉堆相同(是一个完全二叉树)，支持deleteMin和deleteMax两种操作
 * 偶数深度上：任意节点X，存储在X上的元素小于它的父亲，但是大于它的祖父
 * 奇数深度上：任意节点X，存储在X上的元素大于它的父亲，但是小于它的祖父
 * 最小值在偶数深度上，最大值在奇数深度上。
 * 以数组结构实现
 * @author xiaozhi009
 *
 */
public class MinAndMaxArrayHeap<T extends Comparable<? super T>> {
	private int size;
	private final static int DEFAULT_SIZE = 16;
	private T[] table;// 下标从1开始。

	public MinAndMaxArrayHeap() {
		this(DEFAULT_SIZE);
	}
	public MinAndMaxArrayHeap(int size) {
		table =  (T[]) new Comparable[size];
		size = 0;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public int size() {
		return size;
	}

	public T findMin() {
		if (isEmpty()) {
			return null;
		}
		return table[1];
	}

	public T findMax() {
		if (size > 1) {
			if (null == table[3]) {
				return table[2];
			} else {
				return table[2].compareTo(table[3]) > 0? table[2]: table[3];
			}
		}
		return findMin();
	}
	
	public void insert(T element) {
		size++;
		expand();
		int i = size;
		table[0] = element;
		while(i > 0) {
			int compareResult = element.compareTo(table[i/2]);
			if ((level(i))%2 == 0) {//偶数
				if (compareResult > 0) {
					table[i] = table[i/2];
					i/=2;
				} else {
					table[i] = element;
					return;
				}
			} else { // 奇数
				if (compareResult < 0) {
					table[i] = table[i/2];
					i/=2;
				} else {
					table[i] = element;
					return;
				}
			}
		}
	}

	public void merge(MinAndMaxArrayHeap<T> hs) {
		if (this == hs) {
			return;
		}
		// TODO 合并两个子二叉堆
	}

	/**
	 * 偶数项值随着层数增加值变大，
	 * 奇数项值随着层数增加值减少
	 */
	public T deleteMin() {
		if (isEmpty()) {
			return null;
		}
		T minVal = table[1];
		int len = size;
		if (level(len) % 2 != 0) {//奇数
			//只有偶数，会存入小值
			len = len/2;
		}
		table[1] = table[size--];
		int i = 1;
		percolateDown(len, i, true);
		return minVal;
	}

	public T deleteMax() {
		if (isEmpty()) {
			return null;
		}
		T maxVal = table[1];
		if (size > 1 && size <= 3) {
			size--;
			return table[3] == null ? table[2]: table[2].compareTo(table[3]) > 0? 
					table[2]:table[3];
		} else if (size > 3){
			int deletedIndex = 2;
			if (table[2].compareTo(table[3]) < 0) {
				deletedIndex = 3;
			}
			maxVal = table[deletedIndex];
			int len = size;
			if (level(len) % 2 == 0) {//偶数
				//只有奇数，会存入最大值
				len = len/2;
			}
			table[deletedIndex] = table[size--];
			percolateDown(len, deletedIndex, false);
			return maxVal;
		}
		size = 0;
		return maxVal;
	}
	/**
	 * 下滤操作
	 * @param len 边界条件
	 * @param i 下标
	 */
	private void percolateDown(int len, int i, boolean isMin) {
		int child;
		T tmp = table[i];
		while(4*i <= len) {
			child = 4*i;
			if (child == size) {// 只有一个子节点
			} else if (child == size-2) {// 有三个子节点
				T selectedChild = null;
				if (isMin) {
					selectedChild = getMin(getMin(table[child], table[child+1]), table[child+2]);
				} else {
					selectedChild = getMax(getMax(table[child], table[child+1]), table[child+2]);
				}
				child = getCompareIndex(child, child+1, child+2, selectedChild);
			}
			int compareResult = tmp.compareTo(table[child]);
			if (compareResult > 0) {
				table[i] = table[child];
				i = child;
			} else {
				break;
			}
		}
		table[i] = tmp;
	}

	private T getMin(T a, T b) {
		return a.compareTo(b) < 0? a: b;
	}

	private T getMax(T a, T b) {
		return a.compareTo(b) > 0? a: b;
	}
	
	private int getCompareIndex(int a, int b, int c, T val) {
		if (table[a].compareTo(val) == 0) {
			return a;
		}
		if (table[b].compareTo(val) == 0) {
			return b;
		}
		if (table[c].compareTo(val) == 0) {
			return c;
		}
		return -1;
	}

	private void expand() {
		if (size >= table.length) {
			T[] oldTable = table;
			table = (T[]) new Comparable[2*oldTable.length + 1];
			for (int i = 1; i < size; i++) {
				table[i] = oldTable[i];
			}
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i <= size; i++) {
			sb.append("下标：" + i + "--->" +table[i] + "\n");
		}
		return sb.toString();
	}

	private int level(int length) {
		int i = 0;
		while(length > 1) {
			length >>= 1;
			i++;
		}
		return i;
	}

	public static void main(String[] args) {
		MinAndMaxArrayHeap<Integer> mma = new MinAndMaxArrayHeap<>();
		mma.insert(6);
		mma.insert(81);
		mma.insert(87);
		mma.insert(14);
		mma.insert(17);
		mma.insert(12);
		mma.insert(28);
//		System.out.println(mma);
		mma.deleteMax();
		System.out.println(mma);
	}
}
