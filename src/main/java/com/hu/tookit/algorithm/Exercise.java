package com.hu.tookit.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 表、栈、队
 * @author xiaozhi009
 *
 */
public class Exercise {

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

	/**
	 * 返回单个字母替换成多个单词的单词集合
	 * @param theWords
	 * @return
	 */
	public static Map<String, List<String>> computeAdjacentWords(List<String> theWords) {
		Map<String, List<String>> adjWords = new HashMap<>();
		Map<Integer, List<String>> wordsByLength = new HashMap<>();

		for (String word: theWords) {
			update(wordsByLength, word.length(), word);
		}
		
		for (Entry<Integer, List<String>> wordEntry: wordsByLength.entrySet()) {
			List<String> groupWords = wordEntry.getValue();
			int groupNum = wordEntry.getKey();
			
			for (int i = 0; i < groupNum; i++) {
				Map<String, List<String>> repToWord = new HashMap<>();
				for (String groupWord: groupWords) {
					String rep = groupWord.substring(0, i) + groupWord.substring(i+1);
					update(repToWord, rep, groupWord);
				}
				for (List<String> wordClique: repToWord.values()) {
					for (String wordI: wordClique) {
						for (String wordJ: wordClique) {
							if (wordI != wordJ) {
								update(adjWords, wordI, wordJ);
							}
						}
					}
//					for (int first = 0, lenX = wordClique.size(); first < lenX; first++) {
//						for (int second = 0, lenY = wordClique.size(); second < lenY; second++) {
//							update(adjWords, wordClique.get(first), wordClique.get(second));
//						}
//					}
				}
			}
		}
		return adjWords;
	}

	private static <T> void update(Map<T, List<String>> map, T key, String value) {
		List<String> values = map.get(key);
		if (null == values) {
			values = new ArrayList<>();
			map.put(key, values);
		}
		values.add(value);
	}
}
