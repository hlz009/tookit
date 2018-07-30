package com.hu.tookit.MathUtil;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 大数字整型运算帮助类,适用于jdk1.8以上
 * 应用场景：目前只适合正整数
 * @author xiaozhi009
 *
 */
public class BigNumberMathematicalUtil {
	private final static int MAX_FACTORIAL_INDEX = 12;

	private BigNumberMathematicalUtil() {}

	/**
	 * 阶乘运算
	 * @param n 
	 * @return
	 */
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
	 * 乘法运算，建议乘数大于被乘数。
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

	/**
	 * 乘法运算
	 * 适合两个数字都超过Integer.MAX_VALUE
	 * @param multiplier 乘数
	 * @param multiplicand 被乘数
	 * @return
	 */
	public static String multiplyLarge(String multiplier, String multiplicand) {
		int[] multiplierArray = StringToIntArray(multiplier);
		int[] multiplicandArray = StringToIntArray(multiplicand);
		int[] sumArray = new int[multiplierArray.length]; 
		for (int i = 0, lenI = multiplicandArray.length; i < lenI; i++) {
			int multiplicandNum = multiplicandArray[i]*(int)(Math.pow(10, i));
			for (int j = 0, len = multiplierArray.length; j < len; j++) {
				sumArray[j] += multiplierArray[j] * multiplicandNum;
			}
		}
		int carryDigit = 0;
		for (int i = 0, len = sumArray.length; i < len; i++) {
			sumArray[i] += carryDigit;
			carryDigit = sumArray[i] / 10;
			sumArray[i] = sumArray[i] % 10;
		}
		sumArray = carryDigit(sumArray, carryDigit);
		return intArrayToString(sumArray);
	}

	private static int[] StringToIntArray(String numStr) {
		isNumeric(numStr);
	    int len = numStr.length();
	    int[] numArray = new int[len];
	    for (int i = 0; i < len; i++) {
	    	// 数字0~9的ASCII码为48~57
	    	numArray[i] = numStr.charAt(i) - 48;
	    }
	    return numArray; 
	}

	private static void isNumeric(String numStr) {
		Pattern pattern = Pattern.compile("[0-9]*"); 
		Matcher isNum = pattern.matcher(numStr);
	    if (!isNum.matches()) {
	       throw new NumberFormatException("字符串只能是数字"); 
	    }
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
