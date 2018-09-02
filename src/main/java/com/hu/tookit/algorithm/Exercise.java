package com.hu.tookit.algorithm;

import java.nio.file.FileSystem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * 表、栈、队
 * @author xiaozhi009
 *
 */
public class Exercise {
	
	public static void main(String[] args) {
		List<Integer> s1 = new ArrayList<Integer>();
		s1.add(1);
		s1.add(2);
		s1.add(3);
		s1.add(4);
		s1.add(5);
		System.out.println(Josephus(3, s1));
	}

	/**
	 * 进行交集操作
	 * 已排好序的两个列表
	 * @param <T>
	 * @param first
	 * @param second
	 */
	public static <T> List<T> intersectionSortBetween(List<T> first, List<T> second) {
		int matchLength = -1;
		int secondLength = second.size();
		Iterator<T> lite = first.iterator();
		List<T> resultList = new ArrayList<T>();
		while(lite.hasNext() && matchLength < secondLength) {
			T data = lite.next();
			int lastSameIndex = second.indexOf(data);
			if (lastSameIndex == -1) {
				continue;
			}
			resultList.add(data);
			matchLength = lastSameIndex;
			matchLength++;
		}
		if (matchLength == -1) {
			return null;
		}
		return resultList;
	}

	/**
	 * 进行交集操作（两表未排序）
	 * 注意 T 要自己实现equals和hashcode方法
	 * @param first
	 * @param second
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> List<T> intersectionBetween(List first, List second) {
		Collections.sort(first);
		Collections.sort(second);
		return intersectionSortBetween(first, second);
	}

	/**
	 * 进行并集操作
	 * @param first
	 * @param second
	 */
	public static <T> List<T> unionBetween(List<T> first, List<T> second) {
		first.addAll(second);
		HashSet<T> set = new HashSet<T>(first);
		return new ArrayList<T>(set);
	}

	/**
	 * Josephus问题
	 * @param <T>
	 * @param <T>
	 * @param m m次
	 * @param array 长度n的列表
	 * @return 返回消除元素的列表
	 */
	public static <T> List<T> Josephus(int m, List<T> array) {
		int currenLength = array.size();
		List<T> result = new ArrayList<T>();
		int index = 0;
		while (currenLength > 1) {
			index = (m+index)%currenLength;
			T data = array.remove(index);
			result.add(data);
			currenLength = array.size();
			index %= currenLength;
		}
		return result;
	}

}
