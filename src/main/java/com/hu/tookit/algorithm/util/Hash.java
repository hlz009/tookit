package com.hu.tookit.algorithm.util;

import com.hu.tookit.algorithm.constant.HashEnum;

/**
 * 散列计算
 * @author xiaozhi009
 *
 */
public class Hash {
	private Hash() {
	}

	/**
	 * 此散列只适合很少的数据，经测试没什么用
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

	/**
	 * 多项式   利用Hornor法则计算37的多项式函数
	 * @param key
	 * @return
	 */
	public static int polynomialHash(String key) {
		int hashVal = 0;
		for (int i = 0; i < key.length(); i++) {
			hashVal = 37*hashVal + key.charAt(i);
		}
		return hashVal;
	}

	public static <T> int nativeHash(T key) {
		return key.hashCode();
	}

	public static <T> int hash(T data, int which) {
		switch(HashEnum.getHashEnum(which)) {
//			case ASCII: //此散列不好用
//				return ASCIIHash(String.valueOf(data));
			case CHARACTERS:
				return charactersHash(String.valueOf(data));
			case POLYNOMIAL:
				return polynomialHash(String.valueOf(data));
			default :
				return nativeHash(data);
		}
	}

	public static int getHashNum() {
		return HashEnum.values().length;
	}

	public static void main(String[] args) {
		Integer i = 0;
		System.out.println(i.hashCode());
	}
}
