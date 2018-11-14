package com.hu.tookit.algorithm;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GraphCase {
	public static void main(String[] args) {
		Map<String, List<String>> adjacentWords = new HashMap<>();
		adjacentWords.put("zero", Arrays.asList(new String[] {"hero", "aero", "cero", "kero"}));
		adjacentWords.put("hero", Arrays.asList(new String[] {"here", "hera", "herxx"}));
		adjacentWords.put("here", Arrays.asList(new String[] {"hire"}));
		adjacentWords.put("hire", Arrays.asList(new String[] {"fire"}));
		adjacentWords.put("fire", Arrays.asList(new String[] {"five"}));
		print(findChain(adjacentWords, "hero", "five"));
	}
	
	public static <E>void print(Collection<E> c) {
		Iterator<E> ite = c.iterator();
		while (ite.hasNext()) {
			System.out.println(ite.next());
		}
	}

	public static List<String> findChain(Map<String, List<String>> adjacentWords, 
			String first, String second) {
		Map<String, String> previousWord = new HashMap<String, String>();
		LinkedList<String> q = new LinkedList<>();
		q.add(first);
		while (!q.isEmpty()) {
			String current = q.removeFirst();
			List<String> adj = adjacentWords.get(current);
			if (adj != null) {
				for (String adjWord: adj) {
					if (previousWord.get(adjWord) == null) {
						previousWord.put(adjWord, current);
						q.addLast(adjWord);
					}
				}
			}
		}
		previousWord.put(first, null);
		return getChainFromPreviousMap(previousWord, first, second);
	}

	public static List<String> getChainFromPreviousMap(Map<String, String> prev, String first, 
			String second) {
		LinkedList<String> result = null;
		if (prev.get(second) != null) {
			result = new LinkedList<String>();
			for (String str = second; str != null; str = prev.get(str)) {
				result.addFirst(str);
			}
		}
		return result;
	}
}
