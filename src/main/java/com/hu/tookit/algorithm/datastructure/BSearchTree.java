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
//		root = insert2(root, data);
	}

	/**
	 * 非递归形式，如果插入数据后，需要平衡，则不能这样写
	 * 具体参考AVLTree
	 * @param node
	 * @param data
	 * @return
	 */
	private BTreeNode<T> insert(BTreeNode<T> node, T data) {
		if (null == node) {
			root = new BTreeNode<T>(null, data, null);
			return root;
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
			} else if (compareResult > 0) {
				if (current.getRight() == null) {
					current.setRight(newNode);
					break;
				}
				current = current.getRight();
			} else {
				break;
			}
		}
		return current;
	}

	// 递归
	private BTreeNode<T> insert2(BTreeNode<T> node, T data) {
		if (null == node) {
			return new BTreeNode<T>(null, data, null);
		}
		int compareResult = data.compareTo(node.getElement());
		if (compareResult < 0) {
			node.setLeft(insert(node.getLeft(), data));
		} else if (compareResult > 0) {
			node.setRight(insert(node.getRight(), data));
		}
		return node;
	}
	
	public void remove(T data) {
//		root = remove2(root, data);
		remove(root, data);
	}

	// 非递归
	private BTreeNode<T> remove(BTreeNode<T> node, T data) {
		if (null == node) {
			return node;
		}
		BTreeNode<T> current = node;
		BTreeNode<T> parent = node;
		while(current != null) {
			int compareResult = data.compareTo(current.getElement());
			if (compareResult < 0) {
				parent = current;
				current = current.getLeft();
			} else if (compareResult > 0) {
				parent = current;
				current = current.getRight();
			} else if (current.getLeft() != null && current.getRight() != null) {
				BTreeNode<T> sub = current.getRight();
				while(sub.getLeft() != null) {
					// 类似于findMin
					parent = sub;
					sub = sub.getLeft();
				}
				current.setElement(sub.getElement());
				// sub最多只有右子节点（因为是删除右子树最小的节点，该节点不可能有左子节点）
				if (parent.getLeft() != null) {
					parent.setLeft(sub.getRight());
				} else {
					parent.setRight(sub.getRight());
				}
			} else {
				if (parent.getLeft() != null) {
					parent.setLeft(current.getLeft() != null? current.getLeft(): current.getRight());
				} else {
					parent.setRight(current.getLeft() != null? current.getLeft(): current.getRight());
				}
				break;
			}
		}
		return parent;
	}

	// 递归
	private BTreeNode<T> remove2(BTreeNode<T> node, T data) {
		if (null == node) {
			return node;
		}
		int compareResult = data.compareTo(node.getElement());
		if (compareResult < 0) {
			node.setLeft(remove2(node.getLeft(), data));
		} else if (compareResult > 0) {
			node.setRight(remove2(node.getRight(), data));
		} else if (node.getLeft() != null && node.getRight() != null) {
			T minData = findMin(node.getRight()).getElement();
			node.setElement(minData);// 替换element
			node.setRight(remove2(node.getRight(), minData));
		} else {
			node = node.getLeft() != null? node.getLeft(): node.getRight();
		}
		return node;
	}

	public static void main(String[] args) {
		BSearchTree<Integer> bst = new BSearchTree<Integer>();
		bst.insert(6);
		bst.insert(2);
		bst.insert(1);
		bst.insert(5);
		bst.insert(3);
		bst.insert(4);
		bst.insert(8);
		bst.remove(2);
		System.out.println(bst.contains(2));
		System.out.println(bst.contains(1));
//		System.out.println(bst.contains(3));
		System.out.println(bst.root.getElement());
		System.out.println(bst.root.getLeft().getRight().getLeft().getElement());
//		System.out.println(bst.root.getLeft().getLeft().getElement());
		System.out.println(bst.root.getRight().getElement());
	}
}
