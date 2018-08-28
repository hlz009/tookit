package com.hu.tookit.algorithm;

import java.util.Stack;

/**
 * 关于栈的一些小应用
 * @author xiaozhi009
 *
 */
public class AlStack {
	public static void main(String[] args) {
//		String inputStr = "6 5 2 3 + 8 * + 3 + *";
//		System.out.println(calculateStack(inputStr));
//		String inputStr = "1 + 2 * 3 + (3*4+5)*6";
//		//123*+34*5+6*+
//		System.out.println(InfixTosuffix(inputStr));
		String inputStr = "public class AlStack {\r\n" + 
				"	public static void main(String[] args) {"
				+ "}"
				+ "}";
		balanceSymbol(inputStr);
	}

	/**
	 * 后缀运算---四则运算
	 * 比如：a + b*c + (d*e + f) * g
	 * 用后缀运算可以写为： abc*+de*f+g*+
	 * @param inputStr
	 */
	public static int calculateStack(String inputStr) {
		inputStr = inputStr.replace(" ", "");
		int len = inputStr.length();
		int i = 0;
		Stack<Integer> stack = new Stack<Integer>();
		while(i < len) {
			char charStr = inputStr.charAt(i);
			boolean isDigit = Character.isDigit(charStr);
			i++;
			if (isDigit) {// 数字则存入栈中
				stack.push(charStr - '0');
				continue;
			} else {
				// 运算符则从栈中拿出两个操作数进行运算，并将结果放入栈中
				if (stack.isEmpty() || stack.size() < 2) {
					throw new RuntimeException("输入的字符串有误，应该是数字，而不是" + charStr);
				}
				int first = stack.pop();
				int second = stack.pop();
				int result = calculateBetween(first, second, charStr);
				stack.push(result);
			}
		}
		return stack.pop();
	}

	private static int calculateBetween(int first, int second, char ch) {
		switch(ch) {
			case '+':
				return first + second;
			case '-':
				return first - second;
			case '*':
				return first * second;
			case '/':
				return first / second;
			case '÷':
				return first / second;
			case '%':
				return first % second;
			default:
				throw new RuntimeException("运算符错误" + ch);
		}
	}

	/**
	 * 中缀转成后缀
	 * 比如：a + b*c + (d*e + f) * g
	 * 转成后缀： abc*+de*f+g*+
	 */
	public static String InfixTosuffix(String inputStr) {
		inputStr = inputStr.replace(" ", "");
		String resultStr = "";
		Stack<Character> stack = new Stack<Character>();
		for (int i = 0, len = inputStr.length(); i < len; i++) {
			char ch = inputStr.charAt(i);
			if (Character.isDigit(ch)) {
				resultStr += ch;
			} else {
				while (!stack.isEmpty()) {
					int prevPriority = getOperatorPriority(stack.peek());
					int currentPriority = getOperatorPriority(ch);
					if (prevPriority < currentPriority ||
							(prevPriority == 10 && currentPriority != 0)) {
						// 栈顶元素的优先级低于当前元素的优先级
						// 如果是左括号直接推入栈， 此处已经将（设置为了高级别10 ) 设置了0
						// 当栈顶元素为( 下一个元素直接压如堆栈
						stack.push(ch);
						break;
					} else {
						// 把）设置级别为0 (级别设置为10 
						// 只要遇到）便可以直接取出计算
						char currentCh = stack.pop();
						if (currentCh == '(') {
							break;
						}
						resultStr += currentCh;
					}
				}
				if (stack.isEmpty()) {
					stack.push(ch);
				}
			}
		}
		while (!stack.isEmpty()) {
			// 遍历结束，如果栈中还有元素
			// 把之前存入栈中的运算符全部取出
			resultStr += stack.pop();
		}
		return resultStr;
	}

	private static int getOperatorPriority(char ch) {
		switch(ch) {
			case '+':
				return 1;
			case '-':
				return 1;
			case '*':
				return 2;
			case '/':
				return 2;
			case '÷':
				return 2;
			case '%':
				return 2;
			case '(':
				return 10;
			case ')':
				return 0;
			default:
				throw new RuntimeException("运算符错误" + ch);
		}
	}

	/**
	 * 平衡符号
	 * 检测代码中的 {} [] ()等
	 * 左半部分称作开放符号，右半部分称作封闭符号
	 * 如果字符是一个开放符号，则放入堆栈中，如果是封闭符号，不能是空栈，则将对应的开放符号弹出
	 * 文件末尾，如果栈非空则报错。
	 * @param inputStr 读入的字符串
	 */
	public static void balanceSymbol(String inputStr) {
		inputStr = getBalanceSymbolStr(inputStr).replace(" ", "");
		Stack<Character> stack = new Stack<Character>();
		for (int i = 0, len = inputStr.length(); i < len; i++) {
			char currentCh = inputStr.charAt(i);
			int openSymbolIntVal = getOpenSymbol(currentCh);
			if (openSymbolIntVal > 0) {
				stack.push(currentCh);
				continue;
			}
			int closedSymbolIntVal = getClosedSymbol(currentCh);
			if (closedSymbolIntVal > 0) {
				if (stack.isEmpty()) {
					throw new RuntimeException("当前不能为封闭字符：" + currentCh);
				}
				char prevCh = stack.pop();
				if (closedSymbolIntVal != getOpenSymbol(prevCh)) {
					throw new RuntimeException("开放符号："+ prevCh + "与封闭字符："
							+ currentCh + "不能对应");
				}
			}
		}
		if (stack.isEmpty()) {
			System.out.println("验证通过");
		} else {
			throw new RuntimeException("验证不通过，以下字符不能对应：" + stack.toString());
		}
	}

	/**
	 * 去掉代码中的字母
	 * 后期可以增加其他的过滤条件
	 * @param str
	 * @return
	 */
	private static String getBalanceSymbolStr(String str) {
		return str.replaceAll("\\w", "");
	}
	
	private static int getOpenSymbol(char ch) {
		switch(ch){
			case '{':
				return 1;
			case '[':
				return 2;
			case '(':
				return 3;
			default:
				return -1;
		}
	}

	private static int getClosedSymbol(char ch) {
		switch(ch){
			case '}':
				return 1;
			case ']':
				return 2;
			case ')':
				return 3;
			default:
				return -1;
		}
	}
}
