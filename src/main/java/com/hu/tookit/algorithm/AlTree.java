package com.hu.tookit.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.hu.tookit.algorithm.datastructure.BTreeNode;

/**
 * 关于树的一些小应用
 * @author xiaozhi009
 *
 */
public class AlTree {
	public static void main(String[] args) {
//		String inputStr = "123*+34*5+6*+";
//		System.out.println(constructBTree(inputStr).inOrderTraversal());
		BTreeNode<Integer> eight = new BTreeNode<Integer>(null, 8 , null);
		BTreeNode<Integer> seven = new BTreeNode<Integer>(null, 7 , null);
		BTreeNode<Integer> six = new BTreeNode<Integer>(null, 6 , null);
		BTreeNode<Integer> five = new BTreeNode<Integer>(null, 5 , null);
		BTreeNode<Integer> four = new BTreeNode<Integer>(eight, 4 , null);
		BTreeNode<Integer> third = new BTreeNode<Integer>(six, 3 , seven);
		BTreeNode<Integer> second = new BTreeNode<Integer>(four, 2 , five);
		BTreeNode<Integer> root = new BTreeNode<Integer>(second, 1 , third);
		List<Integer> result = ZTraversal(root);
		System.out.println(result);
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

	/**
	 * 二叉树Z型遍历
	 * @param node
	 * @return
	 */
	public static <T> List<T> ZTraversal(BTreeNode<T> node) {
		if (null == node) {
			return null;
		}
		List<T> result = new ArrayList<T>();
		Stack<BTreeNode<T>> leftStack = new Stack<BTreeNode<T>>();
		Stack<BTreeNode<T>> rightStack = new Stack<BTreeNode<T>>();
		leftStack.add(node);
		while (!leftStack.isEmpty() || !rightStack.isEmpty()) {
			while (!leftStack.isEmpty()) {
				BTreeNode<T> newNode = leftStack.pop();
				result.add(newNode.getElement());
				if (newNode.getLeft() != null) {
					rightStack.push(newNode.getLeft());
				}
				if (newNode.getRight() != null) {
					rightStack.push(newNode.getRight());
				}
			}
			while (!rightStack.isEmpty()) {
				BTreeNode<T> newNode = rightStack.pop();
				result.add(newNode.getElement());
				if (newNode.getRight() != null) {
					leftStack.push(newNode.getRight());
				}
				if (newNode.getLeft() != null) {
					leftStack.push(newNode.getLeft());
				}
			}
		}
		return result;
	}
}
