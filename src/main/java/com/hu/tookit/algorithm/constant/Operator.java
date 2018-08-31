package com.hu.tookit.algorithm.constant;

/**
 * 操作数
 * @author xiaozhi009
 *
 */
public enum Operator {
	ADD(1, '+'), DECREASE(1, '-'),
	MULTIPLY(2, '*'), DIVIDE(2, '/'), RESIDUAL(2, '%'),
	LEFT_BRACKETS(10, '('), RIGHT_BRACKETS(0, ')');

	private final int priority;
	private final char ch;

	private Operator(int priority, char ch){
		this.priority = priority;
		this.ch = ch;
	}
	
	public int getPriority() {
		return priority;
	}

	public char getCh() {
		return ch;
	}

	public static boolean isValid(char ch) {
		try {
			getPriority(ch);
			return true;
		} catch(RuntimeException e) {
			return false;
		}
	}

	public static int getPriority(char ch) {
		Operator[] operators = Operator.values();
		for (Operator operator: operators) {
			if (operator.getCh() == ch) {
				return operator.priority;
			}
		}
		throw new RuntimeException("运算符错误" + ch);
	} 
}