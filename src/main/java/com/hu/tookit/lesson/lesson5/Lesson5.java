package com.hu.tookit.lesson.lesson5;

import com.hu.tookit.algorithm.datastructure.PolynomialHashTable;
import com.hu.tookit.algorithm.util.Hash;

/**
 * 数据结构与算法分析
 * 第五章课后练习题代码（第三版）
 * @author xiaozhi009
 *
 */
public class Lesson5 {
	
	/**
	 * 5.13 （数组） 
	 * 先将各个多项式的顺序 按升幂排列
	 * @param a
	 * @param b
	 */
	public static void test13a(int[] a, int[] b) {
		int[] result = new int[a.length+b.length];
		for (int i = 0, alen = a.length; i < alen; i++) {
			for (int j = 0, blen = b.length; j < blen; j++) {
				result[i+j] += a[i]*b[j];
			}
		}
		String rs = "";
		for(int i = 0, len = result.length; i < len; i++) {
			if (result[i] != 0) {
				rs += result[i] + "x^" + i + "+"; 
			}
		}
		System.out.println(rs);
	}

	/**
	 * 5.13  采用散列表 
	 * 相乘合并同类型
	 * 本程序使用的散列表是PolynomialHashTable 里面调用的是hashCode
	 * 传参时  选用PolynomialHashTable
	 * 不用提前排序 直接插入数据
	 * 数据大小必须是比最高次幂多1
	 * @param a
	 * @param b
	 */
	public static void test13b(PolynomialHashTable a, PolynomialHashTable b) {
		PolynomialHashTable result = a.multiply(b);
		System.out.println(result);
	}

	/**
	 * 5.15
	 * @param a
	 * @param b
	 */
	public static void test15(String str, String checkedStr) {
		int hashVal = Hash.polynomialHash(str);
		int len = str.length();
		for (int i = 0, s = checkedStr.length(); i < s; i++) {
			if (i+len > s) {
				System.out.println("不存在");
				return;
			}
			String currentStr = checkedStr.substring(i, i+len);
			if (Hash.polynomialHash(currentStr) == hashVal) {
				for (int j = 0; j < len; j++) {
					if (str.charAt(j) != currentStr.charAt(j)) {
						System.out.println("不存在");
						return;
					}
				}
				System.out.println("存在, 位置从" + i);
				return;
			}
		}
	}

	public static void main(String[] args) {
		test15("abc", "sdfgbcbabcsjwoe"); 
	}
}
