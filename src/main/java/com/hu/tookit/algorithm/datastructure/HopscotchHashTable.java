package com.hu.tookit.algorithm.datastructure;

import static com.hu.tookit.Math.Prime.nextPrime;

import java.lang.reflect.Array;
import java.util.Random;

import com.hu.tookit.algorithm.util.HashFactory;
import com.hu.tookit.algorithm.util.HashFactoryImpl;

/**
 * 跳房子散列
 * 好的散列函数和表的大小(取素数)，可以提高程序执行性能，降低出错率。
 * 目前测试，感觉不太理想，
 * （也可能时hash函数的问题，选择跟布谷鸟散列同样的hash函数测试，基本上OK）
 * @author xiaozhi009
 *
 * @param <T>
 */
public class HopscotchHashTable<T> implements MyHashTable<T> {
	/**
	 *  装载因子理论上不超过0.9即可。本程序的散列函数不太好，装载率不能超过0.5。
	 *  超过0.5 会增大出错率。
	 */
	private static final double MAX_LOAD = 0.5;
	private static final int DEFAULT_TABLE_SIZE = 11;
	private static final int ALLOWED_REHASHS = 1;
	private static final int SEQUENCE_BOUND = 5;//执行序列的最大值
	
	private transient final HashFactory<T> hashFunction;
	private transient Entity<T>[] table;
	private transient int size;
	private transient int indexHashFunction;//记录执行的散列函数

	private class Entity<T> {
		T value;
		int hop;
		public Entity(T value, int hop) {
			this.value = value;
			this.hop = hop;
		}

		public Entity(T value) {
			this(value, 0);
		}
	}

	public HopscotchHashTable() {
		this(DEFAULT_TABLE_SIZE);
	}

	@SuppressWarnings("unchecked")
	public HopscotchHashTable(int tableSize) {
		/**
		 * 泛型数组的声明方式
		 */
		table = (Entity<T>[]) Array.newInstance(Entity.class, nextPrime(tableSize));
		/**
		 * 这种写法不对。如果是T[] 可以这样写
		 * T[] table = (T[]) new Object[nextPrime(tableSize)];
		 */
//		table = (Entity<T>[]) new Object[nextPrime(tableSize)]; 
		hashFunction = new HashFactoryImpl<T>();
//		indexHashFunction = hasFunction.randomFunction(); 不写，默认为0
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

	private int rehashes = 0;
	private void insertVal(T data) {
		final int COUNT_LIMIT = (int) (SEQUENCE_BOUND/MAX_LOAD); // 查找的最大限制次数
		while(true) {
			int pos = hash(data);
			int moveIndex = 1 << SEQUENCE_BOUND -1;
			int len = Math.min(pos + SEQUENCE_BOUND, table.length);
			for (int i = pos; i < len; i++) {
				if (table[i] == null) {
					table[i] = new Entity<>(data, moveIndex);
					size++;
					return;
				}
			}
			int maxNullIndex = getMaxIndex() + 1;
			int count = 0;
			/**
			 * COUNT_LIMIT 在到达当前pos位置距离的循环，
			 * 或者认为在一个序列的N次循环（该散列基本上都是很聚集的），
			 * 没有找到可移动的元素，说明无法找到合适的元素了。
			 */
			while(count < COUNT_LIMIT) {
				if (maxNullIndex >= table.length) {
					// 达到顶部了，需要跳出进行expand
					break;
				}
				/**
				 * 可以从后往前（或者从前往后）遍历移动元素
				 */
				for (int i = maxNullIndex-1; i > maxNullIndex-SEQUENCE_BOUND + 1; i--) {
					if (table[i] != null && (table[i].hop & moveIndex) == moveIndex) {
						T temp = table[i].value;
						if (table[maxNullIndex] == null) {
							table[maxNullIndex] = new Entity<>(temp);
						} else {
							table[maxNullIndex].value = temp;
						}
						table[i].hop >>= maxNullIndex - i;
						table[i].value = null;
						maxNullIndex = i;
						break;
					}
				}
//				for (int i = maxNullIndex-SEQUENCE_BOUND + 1; i < maxNullIndex; i++) {
//					if ((table[i].hop & moveIndex) == moveIndex) {
//						T temp = table[i].value;
//						if (table[maxNullIndex] == null) {
//							table[maxNullIndex] = new Entity<>(temp);
//						} else {
//							table[maxNullIndex].value = temp;
//						}
//						table[i].hop >>= maxNullIndex - i;
//						table[i].value = null;
//						maxNullIndex = i;
//						break;
//					}
//				}
				if (pos + SEQUENCE_BOUND -1 >= maxNullIndex || 
						pos - SEQUENCE_BOUND + 1 >= maxNullIndex) {
					table[maxNullIndex].value = data;
					table[pos].hop += 1 << SEQUENCE_BOUND - (maxNullIndex - pos);
					size++;
					return;// 结束
				}
				count++;
			}
			if (++rehashes > ALLOWED_REHASHS) {
				// 再散列次数太多，把表扩大，在rehash
				rehash((int)(table.length/MAX_LOAD));
				rehashes = 0;
			} else {
				rehash();
			}
		}
	}

	
	private int getMaxIndex() {
		for (int i = table.length - 1; i >= 0; i--) {
			if (table[i] != null) {
				return i;
			}
		}
		return -1;
	}

	private void expand() {
		if (getMaxIndex() < table.length - 1 && 
				size / table.length < MAX_LOAD) {
			return;
		}
		rehash((int)(table.length/MAX_LOAD));
	}

	@SuppressWarnings("unchecked")
	private void rehash(int tableSize) {
		Entity<T>[] oldTable = table;
		table = (Entity<T>[]) Array.newInstance(Entity.class, nextPrime(tableSize));
//		table = (Entity<T>[]) new Object[nextPrime(tableSize)];
		size = 0;
		for (Entity<T> oldData: oldTable) {
			if (oldData != null)
				insertVal(oldData.value);
		}
	}

	private void rehash() {
		indexHashFunction = hashFunction.randomFunction();
//		indexHashFunction = 3;
		System.out.println("散列函数--->" + indexHashFunction);
		rehash(table.length);
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void clear() {
//		table = (Entity<T>[]) new Object[table.length];
		table = (Entity<T>[]) Array.newInstance(Entity.class, nextPrime(table.length));
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
		for (int i = 0; i < SEQUENCE_BOUND; i++) {
			int pos = hash(data);
			if (table[pos] != null && 
					table[pos].value.equals(data)) {
				return pos;
			}
		}
		return -1;
	}

	private int hash(T data) {
		int hashVal = hashFunction.hash(data, indexHashFunction) % table.length;
		if (hashVal < 0) {
			hashVal += table.length;
		}
		return hashVal;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for (Entity<T> entity: table) {
			if (null != entity) {
				sb.append("下标->" + i);
				sb.append(", 值->" + entity.value);
				sb.append(", hop->" + Integer.toBinaryString(entity.hop) + "\n");
			}
			i++;
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		HopscotchHashTable<String> ss = new HopscotchHashTable<>();
		Random r = new Random();
		for (int i = 0; i < 100; i++) {
			int k = r.nextInt(100);
			System.out.println(k);
			ss.insert(String.valueOf(k)); 
		}
		System.out.println(ss);
		System.out.println(ss.table.length);
	}
}
