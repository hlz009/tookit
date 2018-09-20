package com.hu.tookit.algorithm.datastructure;

import static com.hu.tookit.Math.Prime.*;

/**
 * 二次探测散列，
 * 比如：(hash(x) + f(i)) mod tableSize
 * f(i)= i^2
 * @author xiaozhi009
 *
 * @param <T>
 */
public class QuadraticProbingHashTable<T> implements MyHashTable<T> {

	private static final int DEFAULT_TABLE_SIZE= 31;
	private Entity<T>[] table;
	private transient int size;

	private class Entity<T> {
		T element;
		boolean isActve;
	
		public Entity(T element, boolean isActive) {
			this.element = element;
			this.isActve = isActive;
		}
	}

	public QuadraticProbingHashTable() {
		this(DEFAULT_TABLE_SIZE);
	}

	@SuppressWarnings("unchecked")
	public QuadraticProbingHashTable(int size) {
		table = new Entity[nextPrime(size)];
		size = 0;
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public void insert(T data) {
		int index = findPos(data);
		if (isActive(index)) {// 包含了值相等的情况
			return;
		}
		table[index] = new Entity<T>(data, true);
		size++;
		if (size > table.length/2) {// 扩容
			reHash();
		}
	}
	
	public boolean contains(T data) {
		int index = findPos(data);
		return isActive(index);
	}

	/**
	 * 此方法用于对象有多个成员变量时，
	 * 以某一个成员变量判断标准时进行查找
	 * @param data
	 * @return
	 */
	public T get(T data) {
		int index = findPos(data);
		if (isActive(index)) {
			return table[index].element;
		}
		return null;
	}

	public void remove(T data) {
		int index = findPos(data);
		if (isActive(index)) {
			table[index].isActve = false;
			size--;
		}
	}

	@SuppressWarnings("unused")
	public void clear() {
		for (Entity<T> entity: table) {
			entity = null;
		}
		// or
//		table = new Entity[table.length];
		size = 0;
	}

	private int hash(T data) {
		int hashVal = data.hashCode() % table.length;
		if (hashVal < 0) {
			hashVal += table.length;
		}
		return hashVal;
	}
	
	/**
	 * 直接找到搜索路径上，isActive=false的位置。
	 * @param data
	 * @return
	 */
	private int findInsertPos(T data) {
		int index = hash(data);
		int offset = 1;
		while (table[index] != null && 
				!table[index].element.equals(data)) {
			index += offset*offset; // 二次（平方）探测 f(i)=i^2
			offset += 1;
			if (index >= table.length) {
				index -= table.length;
			}
		}
		return index;
	}

	/**
	 * 查找下标
	 * 探测方法：线性，平方，双散列，在散列
	 * 双散列：f(i)=i*hash2(x) hash2(x)= R - (data mod R)
	 * 其中R为小于tableSize的素数
	 * @param data
	 * @return
	 */
	private int findPos(T data) {
		int index = hash(data);
		int offset = 1;
		while (table[index] != null && 
				!table[index].element.equals(data)) {
			index += offset*offset; // 二次（平方）探测 f(i)=i^2
			offset += 1;
			if (index >= table.length) {
				index -= table.length;
			}
		}
		return index;
	}

	private boolean isActive(int index) {
		return table[index]!=null && table[index].isActve;
	}

	@SuppressWarnings("unchecked")
	private void reHash() {
		Entity<T>[] oldTable = table;
		table = new Entity[nextPrime(2*oldTable.length)];
		for (int i = 0, len = oldTable.length; i < len; i++) {
			if (oldTable[i] != null && oldTable[i].isActve) {
				insert(oldTable[i].element);
			}
		}
	}
}
