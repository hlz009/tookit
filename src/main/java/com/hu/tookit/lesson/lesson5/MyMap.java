package com.hu.tookit.lesson.lesson5;

import com.hu.tookit.algorithm.datastructure.QuadraticProbingHashTable;

/**
 * 实现简单的一个平方探测的Map
 * 5.20
 * @author xiaozhi009
 *
 */
public class MyMap<T, V> {
	private QuadraticProbingHashTable<Entry<T, V>> items;

	private static class Entry<T, V> {
		T key;
		V value;

		public Entry(T key, V value) {
			super();
			this.key = key;
			this.value = value;
		}

		public Entry(T key) {
			this(key, null);
		}

		@Override
		public int hashCode() {
			return  ((key == null) ? 0 : key.hashCode());
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Entry other = (Entry) obj;
			if (key == null) {
				if (other.key != null)
					return false;
			} else if (!key.equals(other.key))
				return false;
			return true;
		}
	}

	public MyMap() {
		items = new QuadraticProbingHashTable<>();
	}

	public void put(T key, V value) {
		items.insert(new Entry<>(key, value));
	}

	public V get(T key) {
		Entry<T, V> entry = items.get(new Entry<>(key));
		if (entry == null) {
			return null;
		}
		return entry.value;
	}

	public static void main(String[] args) {
		MyMap<String, Integer> mm = new MyMap<>();
		mm.put("first", 1);
		mm.put("second", 2);
		System.out.println(mm.get("first"));
	}
}
