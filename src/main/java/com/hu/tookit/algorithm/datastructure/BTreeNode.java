package com.hu.tookit.algorithm.datastructure;

public class BTreeNode<T> {
	
	private transient int deepth = 0;
	private BTreeNode<T> left;
	private BTreeNode<T> right;
	private T element;

	private String resultStr = "";
	
	public BTreeNode (BTreeNode<T> left, T element, BTreeNode<T> right) {
		this.left = left;
		this.element = element;
		this.right = right;
	}

	public BTreeNode<T> getLeft() {
		return left;
	}

	public void setLeft(BTreeNode<T> left) {
		this.left = left;
	}

	public BTreeNode<T> getRight() {
		return right;
	}

	public void setRight(BTreeNode<T> right) {
		this.right = right;
	}

	public T getElement() {
		return element;
	}

	public void setElement(T element) {
		this.element = element;
	}

	public int length() {
		return deepth;
	}

	/**
	 * 中序遍历-- 左中右
	 */
	public String inOrderTraversal() {
		return inOrderTraversal(this);
	}
	private String inOrderTraversal(BTreeNode<T> node) {
		if (node.getLeft() != null) {
			inOrderTraversal(node.getLeft());
		}
		resultStr += node.getElement();
		if (node.getRight() != null) {
			inOrderTraversal(node.getRight());
		}
		return resultStr;
	}
}
