package com.hu.tookit.algorithm;

/**
 * 表、栈、队
 * @author xiaozhi009
 *
 */
public class Exercise {
	
	public static void main(String[] args) {
		DoubleLinkedList<Integer> ss = new DoubleLinkedList<Integer>();
		ss.add(1);
		ss.add(2);
		ss.add(3);
		System.out.println(ss.toString());
		ss.swap(2, 3);
		System.out.println(ss.toString());
	}
}
