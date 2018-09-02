package com.hu.tookit.algorithm.datastructure;

/**
 * 二叉查找树
 * @author xiaozhi009
 *
 * @param <T>
 */
public class BSearchTree<T extends Comparable<? super T>> {
	
	/**  BTreeNode 实际该类可以创建成内部类 */
	private BTreeNode<T> root;
	
	public BSearchTree() {
		root = null;
	}

	public boolean isEmpty() {
		return root == null;
	}

	public T findMin() {
		if (isEmpty()) {
			throw new RuntimeException("空树");
		}
		return findMin(root).getElement();
	}

	/**
	 * 递归实现
	 * @param node
	 * @return
	 */
	private BTreeNode<T> findMin(BTreeNode<T> node) {
		if (node.getLeft() == null) {
			return node;
		} else {
			return findMin(node.getLeft());
		}
	}

	public T findMax() {
		if (isEmpty()) {
			throw new RuntimeException("空树");
		}
		return findMax(root).getElement();
	}

	/**
	 * 非递归实现
	 * @param node
	 * @return
	 */
	private BTreeNode<T> findMax(BTreeNode<T> node) {
		while(node.getRight() != null) {
			node = node.getRight();
		}
		return node;
	}
	
	public boolean contains(T data) {
		return contains(root, data);
	}

	/**
	 * 递归调用，语义更简单
	 * @param node
	 * @param data
	 * @return
	 */
	private boolean contains(BTreeNode<T> node, T data) {
		if (node == null) {
			return false;
		}
		int compareResult = data.compareTo(node.getElement());
		if (compareResult < 0) {
			return contains(node.getLeft(), data);
		} else if (compareResult > 0) {
			return contains(node.getRight(), data);
		} else {
			return true;
		}
	}

	/**
	 * 非递归形式调用
	 * @param node
	 * @param data
	 * @return
	 */
	private boolean contains2(BTreeNode<T> node, T data) {
		boolean contains = false;
		while (node != null) {
			int compareResult = data.compareTo(node.getElement());
			if (compareResult < 0) {
				node = node.getLeft();
			} else if (compareResult > 0) {
				node = node.getRight();
			} else {
				contains = true;
				break;
			}
		}
		return contains;
	}

	public void insert(T data) {
		insert(root, data);
	}

	private void insert(BTreeNode<T> node, T data) {
		if (null == node) {
			node = new BTreeNode<T>(null, data, null);
			return;
		}
		BTreeNode<T> current = node;
		BTreeNode<T> newNode = new BTreeNode<T>(null, data, null);
		while(current != null) {
			int compareResult = data.compareTo(current.getElement());
			if (compareResult < 0) {
				if (current.getLeft() == null) {
					current.setLeft(newNode);
					break;
				}
				current = current.getLeft();
			} else if (compareResult < 0) {
				if (current.getRight() == null) {
					current.setRight(newNode);
					break;
				}
				current = current.getRight();
			} else {
				break;
			}
		}
	}

	public void remove(T data) {
		remove(root, data);
	}

	private void remove(BTreeNode<T> node, T data) {
		if (null == node) {
			return;
		}
		while(node != null) {
			int compareResult = data.compareTo(node.getElement());
			if (compareResult < 0) {
				node = node.getLeft();
			} else if (compareResult > 0) {
				node = node.getRight();
			} else if (node.getLeft() != null && node.getRight() != null) {
				T minData = findMin(node.getRight()).getElement();
				node.setElement(minData);// 替换element
				remove(node.getRight(), minData);
			} else {
				node = node.getLeft() != null? node.getLeft(): node.getRight();
			}
		}
	}
}
