package com.hu.tookit.algorithm;

/**
 * 表、栈、队列练习题
 * @author xiaozhi009
 *
 */
public class PractiseDataStructure {
	
	public static void main(String[] args) {
		SingleLinkedList<Integer> ss = new SingleLinkedList<Integer>();
		ss.add(1);
		ss.add(2);
		ss.add(3);
		System.out.println(ss.toString());
		ss.swap(1, 2);
		System.out.println(ss.toString());
	}
}
