package com.hu.tookit.algorithm.util;

import java.util.Random;

import com.hu.tookit.Math.Prime;
import com.hu.tookit.algorithm.constant.HashEnum;

/**
 * 散列计算
 * @author xiaozhi009
 *
 */
public class Hash {
	private static Random r = new Random();
	private Hash() {
	}

	/**
	 * 此散列只适合很少的数据
	 * @param key
	 * @return
	 */
	public static int ASCIIHash(String key) {
		int hashVal = 0;
		for (int i = 0; i < key.length(); i++) {
			hashVal += key.charAt(i);
		}
		return hashVal;
	}
	
	private static final int DIGS = 31;
	private static final int MERSENNE_PRIME = (1 << DIGS) - 1;
	private static int a = r.nextInt();
	private static int b = r.nextInt();
	/**
	 * 针对整数的通用散列
	 * @param x
	 * @return
	 */
	public static int universalHash(int x) {
		// a * x + b a和b可以随机选取
		long hashVal = (long) a*x + b;
		hashVal = (hashVal >> DIGS) + (hashVal & MERSENNE_PRIME);
		if (hashVal >= MERSENNE_PRIME) {
			hashVal -= MERSENNE_PRIME;
		}
		return (int) hashVal;
	}

	private static final int SPRIME = 257;
	/**
	 * 针对字符的通用散列
	 * @param x
	 * @return
	 */
	public static int universalStringHash(String key) {
		return universalHash(polynomialHash(key));
	}

	public static void generateFactor() {
		a = r.nextInt();
		b = r.nextInt();
		coefficient = Prime.nextPrime(r.nextInt(SPRIME));
	}

	/**
	 * 假设key至少都有三个字符，使用26个字母加空格进行处理,
	 * 总共27个
	 * @param key
	 * @return
	 */
	public static int charactersHash(String key) {
		if (key.length() < 3) {
			return nativeHash(key);
		}
		return key.charAt(0) + 27*key.charAt(1) + 
				729*key.charAt(2);
	}

	private static int coefficient = 31;
	/**
	 * 多项式   利用Hornor法则计算31的多项式函数
	 * @param key
	 * @return
	 */
	public static int polynomialHash(String key) {
		int hashVal = 0;
		for (int i = 0; i < key.length(); i++) {
			hashVal = coefficient*hashVal + key.charAt(i);
		}
		return hashVal;
	}

	public static <T> int nativeHash(T key) {
		return key.hashCode();
	}

	public static <T> int hash(T data, int which) {
		switch(HashEnum.getHashEnum(which)) {
			case Integer: 
				return universalHash((Integer)data);
			case CHARACTERS:
				return charactersHash((String) data);
			case POLYNOMIAL:
				return polynomialHash((String) data);
			case String:
				return universalStringHash((String) data);
			default :
				return nativeHash(data);
		}
	}

	public static int getHashNum() {
		return HashEnum.values().length;
	}
}
