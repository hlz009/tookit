package com.hu.tookit.MathUtil;

import java.util.ArrayList;

/**
 * 大数字整型运算帮助类,适用于jdk1.8以上
 * 应用场景：中性大数字运算，当两个数相乘，且其中被乘数与乘数的某一位的乘积
 * 不会超过int的最大位数。
 * @author xiaozhi009
 *
 */
public class BigNumberMathematicalUtil {
	private final static int MAX_FACTORIAL_INDEX = 12;

	public static void main(String[] args) {
		System.out.println(factorial(17));
//		System.out.println(multiply(999999, 999999));
	}

	private BigNumberMathematicalUtil() {}
	
	public static String factorial(int n) {
		int sum = 1;
		int[] sumArray = null;
		for (int i = 1; i <= n; i++) {
			if (i <= MAX_FACTORIAL_INDEX) {
				sum *= i;
			} else {
				if (sumArray == null) {
					sumArray = intToIntArray(sum);
				}
				sumArray = multiplyArray(i, sumArray);
			}
		}
		if (n <= MAX_FACTORIAL_INDEX) {
			return String.valueOf(sum);
		}
		return intArrayToString(sumArray);
	}

	/**
	 * 加法运算
	 * @param add 加数
	 * @param addend 被加数
	 * @return
	 */
	public static String add(int add, int addend) {
		if (add < addend) {
			add = add + addend;
			addend = add - addend;
			add = add - addend;
		}
		int[] addArray = intToIntArray(add);
		addArray[0] += addend;
		int carryDigit = 0;
		for (int i = 0, len = addArray.length; i < len; i++) {
			addArray[i] += carryDigit;
			carryDigit = addArray[i] / 10;
			addArray[i] = addArray[i] % 10;
			if (carryDigit == 0) {
				break;
			}
		}
		addArray = carryDigit(addArray, carryDigit);
		return intArrayToString(addArray);
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
		return intArrayToString(multiplyArray(multiplicand, multiplierArray));
	}

	private static int[] multiplyArray(int multiplicand, int[] multiplierArray) {
		int carryDigit = 0;
		for (int i = 0, len = multiplierArray.length; i < len; i++) {
			multiplierArray[i] *= multiplicand;
			multiplierArray[i] += carryDigit;
			carryDigit = multiplierArray[i] / 10;
			multiplierArray[i] = multiplierArray[i] % 10;
		}
		multiplierArray = carryDigit(multiplierArray, carryDigit);
		return multiplierArray;
	}

	/**
	 * 进位操作
	 * @param numArray 运算后的结果数组
	 * @param carryDigit 进位数值
	 * @return
	 */
	private static int[] carryDigit(int[] numArray, int carryDigit) {
		if (carryDigit > 0) {
			int oldLastIndex = numArray.length - 1;
			int size = getBitOfDigit(carryDigit);
			int[] newNumArray = enlargeArray(numArray, size);
			for (int i = oldLastIndex + 1, len = oldLastIndex + size;
					i <= len; i++) {
				newNumArray[i] += carryDigit;
				carryDigit = newNumArray[i] / 10;
				newNumArray[i] = newNumArray[i] % 10;
			}
			numArray = newNumArray;
		}
		return numArray;
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
