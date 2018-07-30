package com.hu.tookit.MathUtil;

import java.util.ArrayList;

/**
 * 大数字运算帮助类,适用于1.8以上
 * 应用场景：中性大数字运算，两个数相乘，且其中被乘数与乘数的某一位的乘积
 * 不会超过int的位数。
 * @author xiaozhi009
 *
 */
public class BigNumberMathematicalUtil {
	public static void main(String[] args) {
		String result = multiply(137, 33);
		System.out.println(result);
	}
	/**
	 * 乘法运算，建议乘数大于被乘数，提高允许效率。
	 * 第一步：乘数（大的那一位）转成数组，与被乘数进行每一位相乘
	 * 第二步：进位和留位
	 * @param multiplier 乘数
	 * @param multiplicand 被乘数
	 * @return
	 */
	public static String multiply(int multiplier, int multiplicand) {
		if (multiplier < multiplicand) {
			multiplier = multiplier + multiplicand;
			multiplicand = multiplier - multiplicand;
			multiplier = multiplier - multiplicand;
		}
		int[] multiplierArray = intToIntArray(multiplier);
		int carryDigit = 0;
		for (int i = 0, len = multiplierArray.length; i < len; i++) {
			multiplierArray[i] *= multiplicand;
			multiplierArray[i] += carryDigit;
			carryDigit = multiplierArray[i] / 10;
			multiplierArray[i] = multiplierArray[i] % 10;
		}
		if (carryDigit > 0) {
			int oldLastIndex = multiplierArray.length - 1;
			int size = getBitOfDigit(multiplierArray[oldLastIndex]);
			int[] newMultiplierArray = enlargeArray(multiplierArray, size);
			for (int i = oldLastIndex + 1, len = oldLastIndex + size;
					i <= len; i++) {
				newMultiplierArray[i] += carryDigit;
				carryDigit = i / 10;
			}
			multiplierArray = newMultiplierArray;
		}
		return intArrayToString(multiplierArray);
	}

	private static int[] intToIntArray(int num) {
		ArrayList<Integer> numArray = new ArrayList<Integer>();
		while (num != 0) {
			numArray.add(num % 10); //求余数
			num = num / 10;
		}
		int[] nums = new int[numArray.size()*2];
		nums = numArray.stream().mapToInt(Integer::valueOf).toArray();
		return nums;
//		Integer[] nums = new Integer[numArray.size()];
//		return numArray.toArray(nums);
	}

	private static int[] enlargeArray(int[] nums, int size) {
		int[] largeNums = new int[nums.length + size];
		for (int i = 0, len = nums.length; i < len; i++) {
			largeNums[i] = nums[i];
		}
		return largeNums;
	}

	private static int getBitOfDigit(int num) {
		int bitIndex = 0;
		while (num != 0) {
			num = num / 10;
			bitIndex++;
		}
		return bitIndex;
	}

	private static String intArrayToString(int[] numArray) {
		StringBuffer sb = new StringBuffer();
		for (int i = numArray.length-1; i >= 0; i--) {
			sb.append(numArray[i]);
		}
		return sb.toString();
	}
}
