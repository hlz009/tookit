package com.hu.tookit.algorithm.datastructure;

public abstract class AbstractTree<T extends Comparable<? super T>> {
	
	transient BinaryNode<T> root;
	
	public boolean isEmpty() {
		return root == null;
	}
	
	public void insert(T element) {
		root = insert(root, element);
	}

	private BinaryNode<T> insert(BinaryNode<T> node, T element) {
		if (null == node) {
			return new BinaryNode<T>(null, element, null);
		}
		int compareResult = element.compareTo(node.element);
		if (compareResult < 0) {
			node.left = insert(node.left, element);
		} else if (compareResult > 0) {
			node.right = insert(node.right, element);
		}
		return node;
	}

	public boolean contains(T data) {
		return contains(root, data);
	}

	private boolean contains(BinaryNode<T> node, T data) {
		if (node == null) {
			return false;
		}
		int compareResult = data.compareTo(node.element);
		if (compareResult < 0) {
			return contains(node.left, data);
		} else if (compareResult > 0) {
			return contains(node.right, data);
		} else {
			return true;
		}
	}
	
	public void printTree() {
		if (isEmpty()) {
			System.out.println("Empty tree");
			return;
		}
		printTree(root);
	}

	/**
	 * 中序遍历
	 * @param node
	 */
	void printTree(BinaryNode<T> node) {
		if (node != null) {
			printTree(node.left);
			System.out.println(node.element);
			printTree(node.right);
		}
	}

	public T findMin() {
		if (isEmpty()) {
			throw new RuntimeException("空树");
		}
		return findMin(root).element;
	}

	/**
	 * 递归实现
	 * @param node
	 * @return
	 */
	protected BinaryNode<T> findMin(BinaryNode<T> node) {
		if (node.left == null) {
			return node;
		} else {
			return findMin(node.left);
		}
	}

	public T findMax() {
		if (isEmpty()) {
			throw new RuntimeException("空树");
		}
		return findMax(root).element;
	}

	/**
	 * 非递归实现
	 * @param node
	 * @return
	 */
	private BinaryNode<T> findMax(BinaryNode<T> node) {
		while(node.right != null) {
			node = node.right;
		}
		return node;
	}

	public void remove(T element) {
		root = remove(root, element);
	}

	private BinaryNode<T> remove(BinaryNode<T> node, T element) {
		if (null == node) {
			return new BinaryNode<T>(null, element, null);
		}
		int compareResult = element.compareTo(node.element);
		if (compareResult < 0) {
			node.left = remove(node.left, element);
		} else if (compareResult > 0) {
			node.right = remove(node.right, element);
		} else if (node.left != null && node.right != null){
			node.element = findMin(node.right).element;
			node.right = remove(node.right, node.element);
		} else {
			node = node.left != null? node.left: node.right;
		}
		return node;
	}
}
