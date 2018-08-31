package com.hu.tookit.algorithm;

import java.util.Stack;

import com.hu.tookit.algorithm.datastructure.BTreeNode;

/**
 * 关于树的一些小应用
 * @author xiaozhi009
 *
 */
public class AlTree {
	public static void main(String[] args) {
		String inputStr = "123*+34*5+6*+";
		System.out.println(constructBTree(inputStr).inOrderTraversal());
	}

	/**
	 * 构建一个二叉树
	 * 存入后缀四则运算字符
	 * @param inputStr
	 * @return
	 */
	public static BTreeNode<Character> constructBTree(String inputStr) {
		inputStr = inputStr.replace(" ", "");
		Stack<BTreeNode<Character>> stack = new Stack<>();
		int i = 0, len = inputStr.length();
		while(i < len) {
			char ch = inputStr.charAt(i);
			if (Character.isDigit(ch)) {
				BTreeNode<Character> bNode = new BTreeNode<Character>(null, ch, null);
				stack.push(bNode);
			} else {
				if (stack.isEmpty() || stack.size() < 2) {
					throw new RuntimeException("后缀字符串非法");
				}
				BTreeNode<Character> first = stack.pop();
				BTreeNode<Character> second = stack.pop();
				BTreeNode<Character> newNode = new BTreeNode<Character>(first, ch, second);
				stack.push(newNode);
			}
			i++;
		}
		return stack.peek();
	}
}
