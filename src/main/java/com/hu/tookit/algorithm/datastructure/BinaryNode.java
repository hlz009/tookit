package com.hu.tookit.algorithm.datastructure;

class BinaryNode<T>{
	T element;
	BinaryNode<T> left;
	BinaryNode<T> right;
	private transient int height;

	public BinaryNode(BinaryNode<T> left, T element, BinaryNode<T> right) {
		this.element = element;
		this.left = left;
		this.right = right;
	}

//	public T getElement() {
//		return element;
//	}
//	public void setElement(T element) {
//		this.element = element;
//	}
//	public BinaryNode<T> getLeft() {
//		return left;
//	}
//	public void setLeft(BinaryNode<T> left) {
//		this.left = left;
//	}
//	public BinaryNode<T> getRight() {
//		return right;
//	}
//	public void setRight(BinaryNode<T> right) {
//		this.right = right;
//	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
}
