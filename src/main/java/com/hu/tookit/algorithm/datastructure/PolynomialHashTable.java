package com.hu.tookit.algorithm.datastructure;

import static com.hu.tookit.Math.Prime.nextPrime;

/**
 * 多项式散列表，用于合并同类项
 * @author xiaozhi009
 *
 * @param <T>
 */
public class PolynomialHashTable {

//	private static final int DEFAULT_TABLE_SIZE= 31;
	private Polynomial[] table;
	private transient int size;

	/**
	 * 多项式对象
	 * @author xiaozhi009
	 *
	 */
	public class Polynomial {
		int hash;// 存储幂
		int modulus;// 系数
		
		/**
		 * 覆写hashCode
		 * 可以将取得的hash值作为幂
		 */
		public int hashCode() {
			return hash;
		}
	
		public Polynomial(int hash, int modulus) {
			this.hash = hash;
			this.modulus = modulus;
		}
	}

//	public PolynomialHashTable() {
//		this(DEFAULT_TABLE_SIZE);
//	}

	/**
	 * 指定size大小，
	 * 不提供默认构造方法
	 * @param size
	 */
	@SuppressWarnings("unchecked")
	public PolynomialHashTable(int size) {
		table = new Polynomial[nextPrime(size)];
		size = 0;
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * 插入
	 * @param data
	 * @param isMerage 是否合并
	 */
	public void insert(Polynomial data, boolean isMerage) {
		int index = findMeragePos(data);
		if (isMerage && table[index] != null) {
			data.modulus += table[index].modulus;
		}
		table[index] = data;
		size++;
	}
	
	public boolean contains(Polynomial data) {
		int index = findPos(data);
		return index != -1;
	}

	public void remove(Polynomial data) {
		int index = findPos(data);
		if (index != -1) {
			table[index] = null;
			size--;
		}
	}

	@SuppressWarnings("unused")
	public void clear() {
		for (Polynomial entity: table) {
			entity = null;
		}
		size = 0;
	}

	private int hash(Polynomial data) {
		int hashVal = data.hashCode() % table.length;
		if (hashVal < 0) {
			hashVal += table.length;
		}
		return hashVal;
	}
	
	/**
	 * 直接找到搜索路径上，对hash冲突的值进行合并
	 * @param data
	 * @return
	 */
	private int findMeragePos(Polynomial data) {
		return hash(data);
	}

	/**
	 * 查找下标 
	 * 本示例特殊，找到index 就判断是否找到了，
	 * 不存在冲突的（冲突时，要么覆盖，要么合并）
	 * @param data
	 * @return
	 */
	private int findPos(Polynomial data) {
		int index = hash(data);
		if (table[index] != null && 
				table[index].modulus != data.modulus) {
			return -1;
		}
		return index;
	}

//	@SuppressWarnings("unchecked")
//	private void reHash() {
//		Polynomial[] oldTable = table;
//		table = new Polynomial[nextPrime(2*oldTable.length)];
//		for (int i = 0, len = oldTable.length; i < len; i++) {
//			if (oldTable[i] != null) {
//				insert(oldTable[i], false);
//			}
//		}
//	}

	public PolynomialHashTable multiply(PolynomialHashTable second) {
		Polynomial[] secondTable = second.table;
		PolynomialHashTable result = new PolynomialHashTable(this.table.length + secondTable.length);
		for (int i = 0, iLen = secondTable.length; i < iLen; i++) {
			for (int j = 0, jLen = this.table.length; j < jLen; j++) {
				if (secondTable[i] != null && table[j] != null) {
					Polynomial p = new Polynomial(i+j, table[j].modulus*secondTable[i].modulus);
					result.insert(p, false);
				}
			}
		}
		return result;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (Polynomial polynomial: table) {
			if (polynomial != null) {
				sb.append(polynomial.modulus + "\t" + "X^" + polynomial.hash + "\n");
			}
		}
		return sb.toString();
	}
}
