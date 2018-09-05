package com.hu.tookit.algorithm.datastructure;

public class AVLTree<T extends Comparable<? super T>> extends AbstractTree<T> {
	private final static int ALLOWED_IMBALANCE = 1;

	private int height(BinaryNode<T> node) {
		return node == null? -1: node.getHeight();
	}

	@Override
	public void insert(T element) {
		root = insert(root, element);
	}

	private BinaryNode<T> insert(BinaryNode<T> node, T element) {
		if (node == null) {
			return new BinaryNode<T>(null, element, null);
		}
		int compareResult = element.compareTo(node.element);
		if (compareResult < 0) {
			node.left = insert(node.left, element);
		} else if (compareResult > 0) {
			node.right = insert(node.right, element);
		}
		return balance(node);
	}
	
	//配合递归版
	private BinaryNode<T> balance(BinaryNode<T> node) {
		if (null == node) {
			return node;
		}
		if (height(node.left) - height(node.right) > ALLOWED_IMBALANCE) {
			if (height(node.left.left) >= height(node.left.right)) {
				node = rotateWithLeftChild(node);
			} else {
				node = doubleRotateWithLeftChild(node);
				System.out.println("左");
			}
		} else if (height(node.right) - height(node.left) > ALLOWED_IMBALANCE) {
			if (height(node.right.right) >= height(node.right.left)) {
				node = rotateWithRightChild(node);
			} else {
				node = doubleRotateWithRightChild(node);
				System.out.println("右");
			}
		}
		node.setHeight(Math.max(height(node.left), height(node.right)) + 1);
		return node;
	}

	/**
	 * 左旋转 左左旋转   α节点的左儿子的左子树
	 * @param k2 需要平衡的点α
	 * @return 返回新的子树根节点
	 */
	private BinaryNode<T> rotateWithLeftChild(BinaryNode<T> k2) {
		BinaryNode<T> k1 = k2.left;
		k2.left = k1.right;
		k1.right = k2;
		k2.setHeight(Math.max(height(k2.left), height(k2.right)) + 1);
//		k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
		k1.setHeight(Math.max(height(k1.left), k2.getHeight()) + 1);
		return k1;
	}

	/**
	 * 右旋转 右右旋转   α节点的右儿子的右子树
	 * @param k1 需要平衡的点α
	 * @return 返回新的子树根节点
	 */
	private BinaryNode<T> rotateWithRightChild(BinaryNode<T> k1) {
		BinaryNode<T> k2 = k1.right;
		k1.right = k2.left;
		k2.left = k1;
		k1.setHeight(Math.max(height(k1.left), height(k1.right)) + 1);
		k2.setHeight(Math.max(k1.getHeight(), height(k2.right)) + 1);
		return k2;
	}

	/**
	 * 左-右双旋转   α节点的左儿子的右子树
	 * @param k3 需要平衡的点α
	 * @return 返回新的子树根节点
	 */
	private BinaryNode<T> doubleRotateWithLeftChild(BinaryNode<T> k3) {
//		BinaryNode<T> k1 = k3.left;
//		BinaryNode<T> k2 = k3.right;
//		k1.right = k2.left;
//		k3.left = k2.left;
//		k2.left = k1;
//		k2.right = k3;
//		k1.height = Math.max(height(k1.left), height(k1.right)) + 1;
//		k3.height = Math.max(height(k3.left), height(k3.right)) + 1;
//		k2.height = Math.max(k1.height, k3.height) + 1;
//		return k2;
		// 以上其实就是两个单旋转，先对k3的左子树右单旋，再对其左单旋
		k3.left = rotateWithRightChild(k3.left);
		return rotateWithLeftChild(k3);
	}

	/**
	 * 右-左双旋转   α节点的右儿子的左子树
	 * @param k1 需要平衡的点α
	 * @return 返回新的子树根节点
	 */
	private BinaryNode<T> doubleRotateWithRightChild(BinaryNode<T> k1) {
//		BinaryNode<T> k3 = k1.right;
//		BinaryNode<T> k2 = k3.left;
//		k1.right = k2.left;
//		k3.left = k2.right;
//		k2.left = k1;
//		k2.right = k3;
//		k1.height = Math.max(height(k1.left), height(k1.right)) + 1;
//		k3.height = Math.max(height(k3.left), height(k3.right)) + 1;
//		k2.height = Math.max(k1.height, k3.height) + 1;
//		return k2;
		// 以上其实就是两个单旋转，先对k1的右子树左单旋，再对其右单旋
		k1.right = rotateWithLeftChild(k1.right);
		return rotateWithRightChild(k1);
	}

	@Override
	public void remove(T element) {
		root = remove(root, element);
	}

	private BinaryNode<T> remove(BinaryNode<T> node, T element) {
		if (null == node) {
			return null;
		}
		int compareResult = element.compareTo(node.element);
		if (compareResult < 0) {
			node.left = remove(node.left, element);
		} else if (compareResult > 0) {
			node.right = remove(node.right, element);
		} else if(node.left != null && node.right != null){
			T minElement = findMin(node.right).element;
			node.element = minElement;
			node.right = remove(node.right, minElement);
		} else {
			node = node.left != null? node.left: node.right;
		}
		return balance(node);
	}


	public static void main(String[] args) {
		AVLTree<Integer> ll = new AVLTree<>();
		ll.insert(4);
		ll.insert(2);
		ll.insert(6);
		ll.insert(3);
		ll.insert(1);
		ll.insert(5);
		ll.insert(7);
		ll.insert(16);
		ll.insert(15);
		ll.printTree();
	}
}
