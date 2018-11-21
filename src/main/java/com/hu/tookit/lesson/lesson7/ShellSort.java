package com.hu.tookit.lesson.lesson7;

public class ShellSort {
	
	public static void main(String[] args) {
		Integer[] a = {9, 10, 8, 7, 6, 5, 4, 3, 2, 1};
		shellSort(a);
		print(a);
	}
	
	public static <T extends Comparable<? super T>> 
	void shellSort(T[] a) {
		int j;
		int gap = 2;
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

	private static <T> void print(T[] array) {
		for (T a: array) {
			System.out.println(a);
		}
	}
}
