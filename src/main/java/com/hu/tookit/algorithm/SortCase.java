package com.hu.tookit.algorithm;

import java.util.ArrayList;

/**
 * 排序案例
 * 插入，希尔，堆排序
 * @author xiaozhi009
 *
 */
public class SortCase {
	public static void main(String[] args) {
		Integer[] a = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5};
//		Integer[] a = {8, 1, 4, 9, 0, 3, 5, 2, 7, 6};
//		quickSelect(a, 7);
		quickSort4(a, 0, a.length-1);
//		String[] a = {"cvf", "abc", "xxx", "ser", "edc", "awr", "acde", "bfv"};
//		radixSort2(a, 4);
		print(a);
	}
	
	private static <T> void print(T[] array) {
		for (T a: array) {
			System.out.println(a);
		}
	}

	//-------------------------------------------------------------//
	// 插入排序 -- 
	public static <T extends Comparable<? super T>> 
	void insertSort(T[] a) {
		insertSort(a, 1, a.length);
	}

	public static <T extends Comparable<? super T>> 
	void insertSort(T[] a, int startPos, int n) {
		int j;
		for (int i = startPos; i < n; i++) {
			T tmp = a[i];
			for (j = i; j > 0; j--) {
				if (tmp.compareTo(a[j-1]) < 0) {
					a[j] = a[j-1];
				} else {
					break;
				}
			}
			a[j] = tmp;
		}
//		// or to be
//		for (int i = startPos; i < n; i++) {
//			for (int j = i; j > 0; j--) {
//				if (a[j].compareTo(a[j-1]) < 0) {
//					T tmp = a[j];
//					a[j] = a[j-1];
//					a[j-1] = tmp;
//				} else {
//					break;
//				}
//			}
//		}
	}
	
	//-------------------------------------------------------------//
	// 希尔排序 -- 
	// 通过比较一定间隔的元素来工作，各趟比较所用的距离随着算法的进行而减小，
	// 直到只比较相邻元素的最后一趟排序止。													
	public static <T extends Comparable<? super T>> 
	void shellSort(T[] a) {
		int j;
		for (int gap = a.length/2; gap > 0; gap/=2) {
			for (int i = gap; i < a.length; i++) {
				T tmp = a[i];
				for (j = i; j >= gap; j -= gap) {
					if (tmp.compareTo(a[j-gap]) < 0) {
						a[j] = a[j-gap];
					} else {
						break;
					}
				}
				a[j] = tmp;
			}
		}
	}

	//-------------------------------------------------------------//
	// 堆排序 -- 
	public static <T extends Comparable<? super T>> 
	T[] heapSort(T[] a) {
		/** buildHeap */
		for (int i = a.length/2 - 1; i >= 0; i--) {
			percolateDown(a, i, a.length);
		}
		for (int i = a.length - 1; i > 0; i--) {
			/** 类似于deleteMax 操作 
			 * 将堆中最后一个元素与首元素替换，注意i一直在--*/
			T tmp = a[0];
			a[0] = a[i];
			a[i] = tmp;
			percolateDown(a, 0, i); /** i 堆的大小一直在减少 */
		}
		return a;
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

	/**
	 * 节点从下标0开始，左儿子节点需要+1
	 * @param i
	 * @return
	 */
	private static int leftChild(int i) {
		return 2*i + 1;
	}

	//-------------------------------------------------------------//
	// 归并排序 -- 
	@SuppressWarnings("unchecked")
	public static <T extends Comparable<? super T>> 
	void mergeSort(T[] a) {
		T[] tmpArray = (T[]) new Comparable[a.length];
		mergeSort(a, tmpArray, 0, a.length-1);
	}

	private static <T extends Comparable<? super T>> 
	void mergeSort(T[] a, T[] tmpArray, int left, int right) {
		if (left >= right) {
			return;
		}
		int center = (left + right)/2;
		mergeSort(a, tmpArray, left, center);
		mergeSort(a, tmpArray, center+1, right);
		merge(a, tmpArray, left, center+1, right);
	} 

	private static <T extends Comparable<? super T>> 
	void merge(T[] a, T[] tmpArray, int leftPos, int rightPos, int rightEnd) {
		int leftEnd = rightPos - 1;
		int tmpPos = leftPos;
		int numElements = rightEnd - leftPos + 1;
		while (leftPos <= leftEnd && rightPos <= rightEnd) {
			if (a[leftPos].compareTo(a[rightPos]) < 0) {
				tmpArray[tmpPos++] = a[leftPos++];
			} else {
				tmpArray[tmpPos++] = a[rightPos++];
			}
		}

		// 合并剩下数组左右部分未比较的数据
		while (leftPos <= leftEnd) {
			tmpArray[tmpPos++] = a[leftPos++];
		}
		while (rightPos <= rightEnd) {
			tmpArray[tmpPos++] = a[rightPos++];
		}
		// copy
		for (int i = 0; i < numElements; i++, rightEnd--) {
			a[rightEnd] = tmpArray[rightEnd];
		}
	}

	//-------------------------------------------------------------//
	// 快速排序 -- 
	private static final int CUTOFF = 3;// 分割的数组大小的截止范围
	
	@SuppressWarnings("unchecked")
	public static <T extends Comparable<? super T>> 
	void quickSort(T[] a) {
		quickSort(a, 0, a.length-1);
	}

	private static <T extends Comparable<? super T>> 
	void quickSort(T[] a, int left, int right) {
		if (left + CUTOFF > right) {
			// 小数组，可以不采用快速排序
			insertSort(a, left, right+1);
			return;
		}
		T pivot = getPivot(a, left, right);
		int i = left+1, j = right-2;
		while (true) {
			// 以下判断要包括等于的情况，
			// 当a[i]=a[j]=pivot时，while一直循环，但是i++或者j--执行不到
			while (a[i].compareTo(pivot) <= 0) {
				i++;
			}
			while (a[j].compareTo(pivot) >= 0) {
				j--;
			}
			if (i < j) {
				swapReference(a, i, j);
			} else {
				break;
			}
		}
		swapReference(a, i, right-1);
		quickSort(a, left, i-1);// sort small subArray
		quickSort(a, i+1, right);// sort big subArray
	}

	
	/**
	 * 与quickSort方法效果一样
	 * 代码仅变动了一点点
	 * @param a
	 * @param left
	 * @param right
	 */
	private static <T extends Comparable<? super T>> 
	void quickSort2(T[] a, int left, int right) {
		if (left + CUTOFF > right) {
			// 小数组，可以不采用快速排序
			insertSort(a, left, right);
			return;
		}
		T pivot = getPivot(a, left, right);
		int i = left, j = right-1;
		while (true) {
			// 不用包括等于的情况，因为是先执行在判断。
			while (a[++i].compareTo(pivot) < 0) {}
			while (a[--j].compareTo(pivot) > 0) {}
			if (i < j) {
				swapReference(a, i, j);
			} else {
				break;
			}
		}
		swapReference(a, i, right-1);
		quickSort2(a, left, i-1);// sort small subArray
		quickSort2(a, i+1, right);// sort big subArray
	}

	/**
	 * 与quickSort方法效果一样，去掉最后一次递归调用
	 * @param a
	 * @param left
	 * @param right
	 */
	private static <T extends Comparable<? super T>> 
	void quickSort3(T[] a, int left, int right) {
//		int start = left;
//		int end = right;
		while (true) {
			if (left + CUTOFF > right) {
				// 小数组，可以不采用快速排序
				insertSort(a, left, right);
				return;
			}
			T pivot = getPivot(a, left, right);
			int i = left, j = right-1;
			while (true) {
				// 不用包括等于的情况，因为是先执行在判断。
				while (a[++i].compareTo(pivot) < 0) {}
				while (a[--j].compareTo(pivot) > 0) {}
				if (i < j) {
					swapReference(a, i, j);
				} else {
					break;
				}
			}
			swapReference(a, i, right-1);
			quickSort3(a, left, i-1);// sort small subArray
//			quickSort(a, i+1, right);// sort big subArray 
			// 改写成不用递归调用的形式
			left = i + 1;
			continue;
		}

	}

	private static <T extends Comparable<? super T>> 
	void quickInnerSort(T[] a, int left, int right) {
		if (left + CUTOFF > right) {
			// 小数组，可以不采用快速排序
			insertSort(a, left, right);
			return;
		}
		T pivot = getPivot(a, left, right);
		int i = left, j = right-1;
		while (true) {
			// 不用包括等于的情况，因为是先执行在判断。
			while (a[++i].compareTo(pivot) < 0) {}
			while (a[--j].compareTo(pivot) > 0) {}
			if (i < j) {
				swapReference(a, i, j);
			} else {
				break;
			}
		}
		swapReference(a, i, right-1);
	}

	/**
	 * 与quickSort方法效果一样
	 * 三路快排
	 * @param a
	 * @param left
	 * @param right
	 */
	private static <T extends Comparable<? super T>> 
	void quickSort4(T[] a, int left, int right) {
		if (left + CUTOFF > right) {
			// 小数组，可以不采用快速排序
			insertSort(a, left, right);
			return;
		}
		T pivot = getPivot(a, left, right);
		int i = left, j = right-1;
		int leftEqualCount = 0;
		int rightEqualCount = 0;
		while (true) {
			// 等于的情况,单独作为一路
			while (a[i].compareTo(pivot) <= 0) {
				if (a[i].compareTo(pivot) == 0) {
					leftEqualCount++;
				} else {
					if (leftEqualCount > 0) {
						int tmpIndex = i;
						while (leftEqualCount > 0) {
							a[tmpIndex - 1] = a[tmpIndex];
							a[tmpIndex] = pivot;
							leftEqualCount--;
							tmpIndex--;
						}
					}
				}
				i++;
			}
			// 得到的pivot放入了右半部分，在这里往中间放置
			while (a[j].compareTo(pivot) >= 0) {
				if (a[j].compareTo(pivot) == 0) {
					rightEqualCount++;
				} else {
					int tmpIndex = j;
					while (rightEqualCount > 0) {
						a[tmpIndex + 1] = a[tmpIndex];
						a[tmpIndex] = pivot;
						rightEqualCount--;
						tmpIndex++;
					}
				}
				j--;
			}
			// 交换操作已经在上面做了。
			if (i < j) {
//				swapReference(a, i, j);
			} else {
				break;
			}
		}
//		swapReference(a, i, right-1);
		quickSort2(a, left, i-1);// sort small subArray
		quickSort2(a, i+1, right);// sort big subArray
	}

	/**
	 * 获取枢纽元（三数中值分割法）
	 * 左中右三个元素排序找出中间大小的元素
	 * @param a
	 * @param left
	 * @param right
	 * @return
	 */
	private static <T extends Comparable<? super T>> 
	T getPivot(T[] a, int left, int right) {
		int center = (left + right)/2;
		if (a[center].compareTo(a[left]) < 0) {
			swapReference(a, left, center);
		}
		if (a[right].compareTo(a[left]) < 0) {
			swapReference(a, left, right);
		}
		if (a[right].compareTo(a[center]) < 0) {
			swapReference(a, center, right);
		}

		/**
		 * 将取得的枢纽元a[center]与倒数第二个元素互换,
		 * 最后一个元素比枢纽元大，与倒数第二个替换更好
		 */
		swapReference(a, center, right-1);
		return a[right-1];
	}

	private static <T extends Comparable<? super T>> 
	void swapReference(T[] a, int left, int right) {
		T tmp = a[left];
		a[left] = a[right];
		a[right] = tmp;
	}

	//-------------------------------------------------------------//
	// 快速选择 -- 在一个数组中，找到第k个最大（小）值
	// 原理与快速排序一样，quickSort，quickSort2，quickSelect可以进行合并
	@SuppressWarnings("unchecked")
	public static <T extends Comparable<? super T>> 
	void quickSelect(T[] a, int k) {
		if (k >= a.length) {
			throw new RuntimeException("不存在该值");
		}
		quickSelect(a, 0, a.length-1, k-1);
		// k默认第一个是下标0
		System.out.println("第" + k + "个值大小为：" + a[k-1]);
	}

	private static <T extends Comparable<? super T>> 
	void quickSelect(T[] a, int left, int right, int k) {
		if (left + CUTOFF > right) {
			// 小数组，可以不采用快速排序
			insertSort(a, left, right+1);
			return;
		}
		T pivot = getPivot(a, left, right);
		int i = left+1, j = right-2;
		while (true) {
			// 以下判断要包括等于的情况，
			// 当a[i]=a[j]=pivot时，while一直循环，但是i++或者j--执行不到
			while (a[i].compareTo(pivot) <= 0) {
				i++;
			}
			while (a[j].compareTo(pivot) >= 0) {
				j--;
			}
			if (i < j) {
				swapReference(a, i, j);
			} else {
				break;
			}
		}
		swapReference(a, i, right-1);
		if (k <= i) {
			quickSort(a, left, i-1);// sort small subArray
		} else if (k > i+1) {
			quickSort(a, i+1, right);// sort big subArray
		}
	}

	//-------------------------------------------------------------//
	// 基数排序（桶排序的扩展）
	// 桶排序不是基于比较的排序，适用于整数，字符(定长)间的排序
	@SuppressWarnings("unchecked")
	public static void radixSort(String[] arr, int stringLen) {
		final int BUCKETS = 256;
		ArrayList<String>[] buckets = new ArrayList[BUCKETS];
		for (int i = 0; i < BUCKETS; i++) {
			buckets[i] = new ArrayList<>();
		}

		for (int pos = stringLen-1; pos >= 0; pos--) {
			for (String s: arr) {
				// 从低位开始，也就是从最后一位开始
				buckets[s.charAt(pos)].add(s);
			}
			int idx = 0;
			for (ArrayList<String> thisBucket: buckets) {
				for (String s: thisBucket) {
					arr[idx++] = s;
				}
				// 必须清空桶排序的数值，否则bucket的数据会进行累加
				thisBucket.clear();
			}
		}
	}

	//-------------------------------------------------------------//
	// 基数排序（桶排序的扩展）
	/**
	 * 用于变长字符串排序
	 * @param arr
	 * @param maxLen 字符的最大长度
	 */
	@SuppressWarnings("unchecked")
	public static void radixSort2(String[] arr, int maxLen) {
		final int BUCKETS = 256;
		ArrayList<String>[] buckets = new ArrayList[BUCKETS];
		ArrayList<String>[] wordsByLength = new ArrayList[maxLen + 1];
		for (int i = 0; i < wordsByLength.length; i++) {
			wordsByLength[i] = new ArrayList<>();
		}
		for (int i = 0; i < BUCKETS; i++) {
			buckets[i] = new ArrayList<>();
		}
		for (String str: arr) {// 根据长度划分（下标为0不存在）
			wordsByLength[str.length()].add(str);
		}
		int idx = 0;
		// 根据长度排了序
		for (ArrayList<String> wordList: wordsByLength) {
			for (String s: wordList) {
				arr[idx++] = s;
			}
		}
		int startingIndex = arr.length;
		for (int pos = maxLen-1; pos >= 0; pos--) {
			// 桶单元中存放字符的数量(从存放最长的字符的桶开始)
			startingIndex -= wordsByLength[pos+1].size();
			for (int i = startingIndex; i < arr.length; i++) {
				buckets[arr[i].charAt(pos)].add(arr[i]);
			}
			idx = startingIndex;
			for (ArrayList<String> thisBucket: buckets) {
				for (String s: thisBucket) {
					arr[idx++] = s;
				}
				thisBucket.clear();
			}
		}
	}

	//-------------------------------------------------------------//
	// 计数基数排序（基数排序的另一种实现,不用ArrayList）
	// 实用范围同基数排序
	@SuppressWarnings("unchecked")
	public static void countRadixSort(String[] in, int stringLen) {
		final int BUCKETS = 256;
		int n = in.length;
//		String[] buffer = new String[n];
//		String[] in = arr;
		String[] out = new String[n];
		for (int pos = stringLen - 1; pos >= 0; pos--) {
			int[] count = new int[BUCKETS + 1];
			for (int i = 0; i < n; i++) {
				count[in[i].charAt(pos) + 1]++;
			}
			for (int i = 1; i <= BUCKETS; i++) {
				count[i] += count[i-1];
			}
			for (int i = 0; i < n; i++) {
				out[count[in[i].charAt(pos)]++] = in[i];
			}
//			String[] tmp = in;
			in = out;
//			out = tmp;
		}
//		if (stringLen % 2 == 1) {
//			for (int i = 0; i < arr.length; i++) {
//				out[i] = in[i];
//			}
//		}
	}
}
