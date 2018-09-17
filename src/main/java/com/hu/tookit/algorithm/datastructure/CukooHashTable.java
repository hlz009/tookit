package com.hu.tookit.algorithm.datastructure;

import static com.hu.tookit.Math.Prime.nextPrime;

import java.util.Random;

import com.hu.tookit.algorithm.util.HashFactory;
import com.hu.tookit.algorithm.util.StringHashFactory;

/**
 * 布谷鸟散列
 * @author xiaozhi009
 * 比较成熟
 * @param <T>
 */
public class CukooHashTable<T> implements MyHashTable<T> {
	private static final double MAX_LOAD = 0.4;
	private static final int ALLOWED_REHASHS = 1;
	private static final int DEFAULT_TABLE_SIZE = 101;
	
	private final HashFactory<T> hasFunction;
	private final int numHashFunctions;
	private T[] table;
	private int size;
	
	public CukooHashTable() {
		this(DEFAULT_TABLE_SIZE);
	}

	@SuppressWarnings("unchecked")
	public CukooHashTable(int tableSize) {
		table = (T[]) new Object[nextPrime(tableSize)];
		hasFunction = (HashFactory<T>) new StringHashFactory();
		numHashFunctions = hasFunction.getNumOfFunctions();
	}

	public int size() {
		return size;
	}

	public boolean contains(T data) {
		return findPos(data) != -1;
	}

	@Override
	public void insert(T data) {
		if (contains(data)) {
			return;
		}
		insertVal(data);
		expand();
	}

	private int rehashTimes = 0;
	private Random r = new Random();

	private void insertVal(T data) {
		final int COUNT_LIMIT = 100;// 设定循环替换元素的次数上限
		while (true) {
			int lastPos = -1;
			int pos;
			for (int count = 0; count < COUNT_LIMIT; count++) {
				for (int i = 0; i < numHashFunctions; i++) {
					pos = hash(data, i);
					if (table[pos] == null) {
						table[pos] = data;
						size++;
						return;
					}
				}
				/**
				 * 没有可插入的位置，进行替换，
				 * 随机替换最后一项， 被替换的值存入data中
				 * 进行下一次插入
				 */
				int i = 0;
				do {
					pos = hash(data, r.nextInt(numHashFunctions));
				} while (pos == lastPos && i++ < 5);
				T temp = table[lastPos = pos];
				table[pos] = data;
				data = temp;
			}
			if (++rehashTimes > ALLOWED_REHASHS) {
				// 再散列次数太多，把表扩大，在rehash
				rehash((int)(table.length/MAX_LOAD));
				rehashTimes = 0;
			} else {
				rehash();
			}
		}
	}

	private void expand() {
		if (size / table.length < MAX_LOAD) {
			return;
		}
		rehash((int)(table.length/MAX_LOAD));
	}

	@SuppressWarnings("unchecked")
	private void rehash(int tableSize) {
		T[] oldTable = table;
		table = (T[]) new Object[nextPrime(tableSize)];
		size = 0;
		for (T oldData: oldTable) {
			insertVal(oldData);
		}
	}

	private void rehash() {
		rehash(table.length);
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void clear() {
		table = (T[]) new Object[table.length];
		size = 0;
	}

	@Override
	public void remove(T data) {
		int index = findPos(data);
		if (index != -1) {
			table[index] = null;
			size--;
		}
	}

	private int findPos(T data) {
		for (int i = 0; i < numHashFunctions; i++) {
			int pos = hash(data, i);
			if (table[pos] != null && 
					table[pos].equals(data)) {
				return pos;
			}
		}
		return -1;
	}

	private int hash(T data, int index) {
		int hashVal = hasFunction.hash(data, index) % table.length;
		if (hashVal < 0) {
			hashVal += table.length;
		}
		return hashVal;
	}

	public static void main(String[] args) {
		CukooHashTable<String> ss = new CukooHashTable<>();
		Random r = new Random();
		for (int i = 0; i < 100; i++) {
			int k = r.nextInt(100);
			System.out.println(k);
			ss.insert(String.valueOf(k)); 
		}
	}
}
