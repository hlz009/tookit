package com.hu.tookit.lesson.lesson7;

public class AnwserSort {
	
	public static void main(String[] args) {
		Integer[] a = {1, 7, 9, 10, 13};
		Integer[] b = {2, 5, 6, 11, 14};
//		System.out.println(getMiddleIndexBetween(a, b));
//		System.out.println(false < true);
		// 猜测次数的高低，与用户第一次初始值也有关系。
		guessPrize(12, 10000, 999);
	}

	public static <T extends Comparable<? super T>> 
	T getMiddleIndexBetween(T[] a, T[] b) {
		// 非递归写法
		int startA = 0, endA = a.length, startB = 0, endB = b.length;
		while (startA < endA && startB < endB) {
			int middle1 = (startA + endA)/2;
			int middle2 = (startB + endB)/2;
			if (a[middle1].compareTo(b[middle2]) >= 0) {
				endA = middle1;
				startB = middle2;
			} else {
				startA = middle1;
				endB = middle2;
			}
		}
		if (a[startA].compareTo(b[startB]) >= 0) {
			return a[startA];
		}
		return b[startB];
		// 调用递归写法
//		return getMiddleNumBetween(a, 0, a.length, b, 0, b.length);
	}

	
	// 递归写法
	public static <T extends Comparable<? super T>> 
	T getMiddleNumBetween(T[] a, int startA, int endA, T[] b, 
			int startB, int endB) {
		if (startA >= endA || startB >= endB) {
			if (a[startA].compareTo(b[startB]) >= 0) {
				return a[startA];
			}
			return b[startB];
		}
		int middle1 = (startA + endA)/2;
		int middle2 = (startB + endB)/2;
		if (a[middle1].compareTo(b[middle2]) >= 0) {
			endA = middle1;
			startB = middle2;
		} else {
			startA = middle1;
			endB = middle2;
		}
		return getMiddleNumBetween(a, startA, endA, b, startB, endB);
	}

	/**
	 * 猜物品价值 1-N
	 * 给定N值，最多允许猜高g次，大于g则猜测失败
	 * 如果g=[logN]，则采用折半查找的策略去竞猜
	 * 过程：第一步，先随意猜测一个数字，比较数字与该价值的大小。
	 * 第二步，采用折半找到该元素的值。
	 * @param g 给定的g值
	 * @param N 给定猜测的值范围
	 * @param guessValue 物品实际的值，--后台给定
	 * @return
	 */
	public static int guessPrize(int g, int N, int guessValue) {
		int[] a = initArray(N);
		int initValue = 500;//初始给定的猜测值。实际猜测的时候，有用户输入
		int initValueIndex = initValue-1;// 初始猜测值的下标
		int value = initValue;
		int valueIndex = initValueIndex;
		int actualGCount = 0;//实际记录的g记录值
		int guessCount = 0; // 猜测次数
		int start = 0, end = N;
		while (actualGCount <= g) {
			if (value == guessValue) {
				System.out.println("猜次数：" + (++guessCount));
				return initValue;
			}
			if (value > guessValue) {
				end = valueIndex;
				actualGCount++;
			} else {
				start = valueIndex;
			}
			valueIndex = (start + end)/2;
			value = a[valueIndex];
			guessCount++;
		}
		if (actualGCount > g) {
			throw new RuntimeException("猜测失败");
		} else {
			System.out.println("猜测成功，实际价值为" + guessValue +
					"，猜测次数为："+ guessCount);
			System.out.println("猜高了的次数为" + actualGCount);
		}
		return -1;
	}

	private static int[] initArray(int len) {
		int[] a = new int[len];
		for (int i = 0; i < len; i++) {
			a[i] = i+1;
		}
		return a;
	}
}
