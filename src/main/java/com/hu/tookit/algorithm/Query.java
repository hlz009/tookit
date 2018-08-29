package com.hu.tookit.algorithm;

public class Query {
	public static void main(String[] args) {
//		int[] a = {-9, 7, 7, 3, 5, -7, -20, 7, 8, 7, 6, 7, 6, 7};
		int[] a = {1, 2, 1, 3, 1, 4, 1};
		getHalfCount(a);
	}

	/**
	 * 对于给定的元素c，在a和b两个数组(有序集合)找到元素
	 * 满足 a+b=c
	 * @param a
	 * @param b
	 * @param c
	 */
	public static void getRightElement(int[] a, int[] b, int c) {
		int i = 0, j = b.length-1, alen= a.length;
		while(i < alen && j > 0) {
			if (a[i]+b[j]>c) {
				j--;
			} else if (a[i]+b[j]<c) {
				i++;
			} else {
				i++;
				j--;
			}
		}
	}

	public static int getMaxProductArray(int[] a) {
		int sum = 0, temp = 0;
		for (int i = 0, len = a.length; i < len; i++) {
			if (temp > 1 && temp > 0) {
				temp *= a[i];
			} else {
				temp = a[i];
			}
			if (temp > sum) {
				sum = temp;
			}
		}
		return sum;
	}

	/**
	 * 得到数组最大的子数组和
	 * @param a
	 * @return
	 */
	public static int getMaxSumArray(int[] a) {
		int sum = 0, temp = 0;
		for (int i = 0, len = a.length; i < len; i++) {
			if (temp > 0) {
				temp += a[i];
			} else {
				temp = a[i];
			}
			if (temp > sum) {
				sum = temp;
			}
		}
		return sum;
	}

	/**
	 * 数组循环移动K位
	 * 时间复杂度  O(n)
	 * @param a
	 * @param k
	 */
	public static void shift(int[] a, int k) {
		int len = a.length;
		k = k % len;
		reverse(a, 0, len-k-1);
		reverse(a, len-k, len-1);
		reverse(a, 0, len-1);
	}

	private static void reverse(int[] a, int start, int end) {
		int temp = 0;
		while(start < end) {
			temp = a[start];
			a[start++] = a[end];
			a[end--] = temp;
		}
	}

	/**
	 * 链表反转
	 * @param node
	 * @return
	 */
	public static Node reverseList(Node node) {
		Node prev = null;
		Node current = node;
		while (current != null) {
			Node next = current.next;
			current.next = prev;
			prev = current;
			current = next;
		}
		return prev;
	}
	
	class Node {
		Node next;
		
	}

	/**
	 * 求出次数超过一半的元素 
	 * 思考：至少有一次存在该元素连续的情况
	 * 去记录重复元素（不管是否是结果元素）的数量，
	 * 然后碰见一个不同元素就减一（相当于抵消了），碰见结果元素也是一样的。
	 * 此种方法只遍历一次数组
	 */
	public static void getHalfCount(int[] a) {
		int result = a[0];
		int resultCount = 1;
		for (int i = 1, len = a.length; i < len; i++) {
			if (a[i] == result) {
				resultCount++;
			} else {
				resultCount--;
				if (resultCount == 0) {
					result = a[i];
					resultCount = 1;
				}
			}
		}
		if (resultCount > 1) {
			System.out.println(result);
		} else {
			System.out.println("没有超过半数的元素");
		}
	}

	/**
	 * 求得数组的最大值和次最大值
	 * 存储max和secondMax
	 * 依次比较，比max大则将当前的值放入max，max值放入secondMax
	 * 如果只比seconMax大，则替换secondMax即可
	 * @param a
	 */
	public static void getMaxAndSencondMax(int[] a) {
		int max = 0;
		int second_max = 0;
		for (int i = 0, len = a.length; i < len; i++) {
			if (a[i] > max) {
				second_max = max;
				max = a[i];
			} else if (a[i] > second_max){
				second_max = a[i];
			}
		}
		System.out.println("最大的数" + max);
		System.out.println("次最大的数" + second_max);
	}

	/**
	 * 筛选数组中，两个出现1次的元素，其余都是2次
	 * @param a
	 * @param sum
	 */
	public static void getTwoOnly(int[] a) {
		getTwo(a, getSingle(a));
	}
	
	private static void getTwo(int[] a, int sum) {
		int flag = 1;
		while(flag != 0) {
			if ((flag & sum) != 0) {
				System.out.println(flag);
				break;
			}
			flag = flag << 1;
		}
		
		int one = 0, two = 0;
		for(int i = 0, len = a.length; i < len; i++) {
			if ((a[i] & flag) != 0) {
				one ^= a[i];
			} else {
				two ^= a[i];
			}
		}
		System.out.println("第一个数" + one);
		System.out.println("第二个数" + two);
	}

	private static int getSingle(int[] a) {
		if (null == a) {
			return -1;
		}
		int sum = a[0];
		for (int i = 1, len = a.length; i < len; i++) {
			sum ^= a[i];
		}
		System.out.println("sum -->" + sum);
		return sum;
	}
}
