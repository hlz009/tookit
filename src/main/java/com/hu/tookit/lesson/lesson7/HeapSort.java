package com.hu.tookit.lesson.lesson7;

public class HeapSort {
	
	public static void main(String[] args) {
		Integer[] a = {9, 10, 8, 7, 6, 5, 4, 3, 2, 1};
		heapSort(a, 1, 5);
		print(a);
	}

	// 堆排序 -- 
	public static <T extends Comparable<? super T>> 
	void heapSort(T[] a, int low, int high) {
		if (low >= high) {
			return;
		}
		if (low < 0) {
			low = 0;
		}
		if (high >= a.length) {
			high = a.length - 1;
		}

//		executeHeapSort1(a, low, high); // 新建临时数组
		executeHeapSort2(a, low, high); // 修改左子节点，把low看作堆序的第一个节点
	}

	private static <T extends Comparable<? super T>> void executeHeapSort1(T[] a, int low, int high) {
		int len = high - low + 1;
		T[] tmp = (T[]) new Comparable[len];
		System.arraycopy(a, low, tmp, 0, len);
		/** buildHeap */
		for (int i = len/2 - 1; i >= 0; i--) {
			percolateDown(tmp, i, len);
		}
		for (int i = len - 1; i > 0; i--) {
			/** 类似于deleteMax 操作 
			 * 将堆中最后一个元素与首元素替换，注意i一直在--*/
			T temp = tmp[0];
			tmp[0] = tmp[i];
			tmp[i] = temp;
			percolateDown(tmp, 0, i); /** i 堆的大小一直在减少 */
		}
		System.arraycopy(tmp, 0, a, low, len);
	}

	/**
	 * 堆序性：max
	 * 下滤操作
	 */
	private static <T extends Comparable<? super T>> 
	void percolateDown(T[] a, int i, int n) {
		int child;
		T tmp;
		for (tmp = a[i]; leftChild(i) < n; i = child) {
			child = leftChild(i);
			if (child != n-1 && a[child].compareTo(a[child+1]) < 0) {
				child++;
			}
			if (tmp.compareTo(a[child]) < 0) {
				a[i] = a[child];
			} else {
				break;
			}
		}
		a[i] = tmp;
	}
	
	private static <T extends Comparable<? super T>> void executeHeapSort2(T[] a, int low, int high) {
		int len = high - low + 1;
		/** buildHeap */
		for (int i = len/2 - 1 + low; i >= low; i--) {
			percolateDown2(a, i, len, low);
		}
		for (int i = len - 1 + low; i > low; i--) {
			/** 类似于deleteMax 操作 
			 * 将堆中最后一个元素与首元素替换，注意i一直在--*/
			T temp = a[low];
			a[low] = a[i];
			a[i] = temp;
			percolateDown2(a, low, i, low); /** i 堆的大小一直在减少 */
		}
	}
	
	private static <T extends Comparable<? super T>> 
	void percolateDown2(T[] a, int i, int n, int low) {
		int child;
		T tmp;
		for (tmp = a[i]; leftChild(i, low) < n; i = child) {
			child = leftChild(i, low);
			if (child != n-1 && a[child].compareTo(a[child+1]) < 0) {
				child++;
			}
			if (tmp.compareTo(a[child]) < 0) {
				a[i] = a[child];
			} else {
				break;
			}
		}
		a[i] = tmp;
	}

	/**
	 * 节点从下标0开始，左儿子节点需要+1
	 * @param i
	 * @return
	 */
	private static int leftChild(int i) {
		return leftChild(i, 0);
	}

	private static int leftChild(int i, int low) {
		return 2*i + 1 - low;
	}

	private static <T> void print(T[] array) {
		for (T a: array) {
			System.out.println(a);
		}
	}
}
