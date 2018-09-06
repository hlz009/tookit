package com.hu.tookit.algorithm.datastructure;

public class SplayTree<T extends Comparable<? super T>> extends AbstractTree<T> {

	private class SplayNode<T> extends BinaryNode<T> {
		private SplayNode<T> parent;
		private SplayNode<T> left;
		private SplayNode<T> right;

		public SplayNode(SplayNode<T> left, T element, SplayNode<T> right) {
			super(left, element, right);
			this.parent = null;
		}
	}

	@Override
	public void insert(T element) {
		SplayNode<T> newNode = insert((SplayNode<T>)root, element);
		splay(newNode);
	}

	private SplayNode<T> insert(SplayNode<T> node, T element) {
		if (null == node) {
			return new SplayNode<T>(null, element, null);
		}
		SplayNode<T> current = (SplayNode<T>) node;
		SplayNode<T> parent = current;
		SplayNode<T> newNode = new SplayNode<T>(null, element, null);
		while(current != null) {
			int compareResult = element.compareTo(current.element);
			if (compareResult < 0) {
				if (current.left == null) {
					current.left = newNode;
					newNode.parent = current;
					break;
				} 
				parent = current;
				current = current.left;
				current.parent = parent;
			} else if (compareResult > 0) {
				if (current.right == null) {
					current.right = newNode;
					newNode.parent = current;
					break;
				} 
				parent = current;
				current = current.right;
				current.parent = parent;
			}
		}
		return newNode;
	}

	/**
	 * 很多删除路径：
	 * // 路径1：先将元素推到根，删除；删除根节点，将左子树最大的节点作为根（本次代码选中）
	 * // 路径2：找到删除的元素，找到这个元素的父节点或子节点，将其作为根。
	 */
	@Override
	public void remove(T element) {
		// 先将元素推到根，删除
		SplayNode<T> selectNode = get(element);
		splay(selectNode);
		root = remove((SplayNode<T>)root);
	}

	private SplayNode<T> remove(SplayNode<T> node) {
		// 删除根节点，将左子树最大的节点作为根,此时该根是没有右儿子的。
		SplayNode<T> leftNode = node.left;
		if (leftNode == null) {// 无左子树
			node = node.right;
			node.parent = null;
			return node;
		}
		SplayNode<T> newRoot = (SplayNode<T>) findMax(leftNode);
		splay(newRoot);
		newRoot.right = node.right;
		newRoot.right.parent = newRoot;
		return newRoot;
	}
	
	@Override
	public boolean contains(T element) {
		SplayNode<T> selectNode = get(element);
		splay(selectNode);
		return selectNode != null;
	}

	/**
	 * 自底向上实现
	 * @param node 要移动的元素
	 */
	private SplayNode<T> splay(SplayNode<T> node) {
		SplayNode<T> parent = node.parent;
		while(parent != null) {
			SplayNode<T> grandParent = parent.parent;
			if (grandParent == null) {
				if (parent.left == node) {
					node = rotateWithLeftChild(parent);
				} else {
					node = rotateWithRightChild(parent);
				}
				root = node;// 这样写不需要调用时，再处理根节点
				return node;
			}
			if (grandParent.left == parent) {
				if (parent.left == node) {
					node = rotateWithLeftChild(parent);
					grandParent.left = node;
					node = rotateWithLeftChild(grandParent);
				} else {
					node = doubleRotateWithLeftChild(grandParent);
				}
				if (node.parent != null) {
					node.parent.left = node;
				}
			} else if (grandParent.right == parent){
				if (parent.left == node) {
					node = doubleRotateWithRightChild(grandParent);
				} else {
					node = rotateWithRightChild(parent);
					grandParent.right = node;
					node = rotateWithRightChild(grandParent);
				}
				if (node.parent != null) {
					node.parent.right = node;
				}
			}
			parent = node.parent;
		}
		root = node;// 这样写不需要调用时，再处理根节点
		return node;
	}

	private SplayNode<T> get(T element) {
		SplayNode<T> current = (SplayTree<T>.SplayNode<T>) root;
		boolean exist = false;
		while (current != null) {
			int compareResult = element.compareTo(current.element);
			if (compareResult < 0) {
				current = current.left;
			} else if (compareResult > 0) {
				current = current.right;
			} else {
				exist = true;
				break;
			}
		}
		if (!exist) { // 具体可以根据业务来处理
			throw new RuntimeException("元素" + element + "不存在");
		}
		return current;
	}


	/**
	 * 左旋转 左左旋转   α节点的左儿子的左子树
	 * @param k2 需要平衡的点α
	 * @return 返回新的子树根节点
	 */
	private SplayNode<T> rotateWithLeftChild(SplayNode<T> k2) {
		SplayNode<T> k1 = k2.left;
		k2.left = k1.right;
		k1.right = k2;
		// 交换parent
		k1.parent = k2.parent;
		k2.parent = k1;
		return k1;
	}

	/**
	 * 右旋转 右右旋转   α节点的右儿子的右子树
	 * @param k1 需要平衡的点α
	 * @return 返回新的子树根节点
	 */
	private SplayNode<T> rotateWithRightChild(SplayNode<T> k1) {
		SplayNode<T> k2 = k1.right;
		k1.right = k2.left;
		k2.left = k1;
		// 交换parent
		k2.parent = k1.parent;
		k1.parent = k2;
		return k2;
	}

	/**
	 * 左-右双旋转   α节点的左儿子的右子树
	 * @param k3 需要平衡的点α
	 * @return 返回新的子树根节点
	 */
	private SplayNode<T> doubleRotateWithLeftChild(SplayNode<T> k3) {
		k3.left = rotateWithRightChild(k3.left);
		return rotateWithLeftChild(k3);
	}

	/**
	 * 右-左双旋转   α节点的右儿子的左子树
	 * @param k1 需要平衡的点α
	 * @return 返回新的子树根节点
	 */
	private SplayNode<T> doubleRotateWithRightChild(SplayNode<T> k1) {
		k1.right = rotateWithLeftChild(k1.right);
		return rotateWithRightChild(k1);
	}

	public void printTree() {
		printTree((SplayNode<T>)root);
	}

	private void printTree(SplayNode<T> node) {
		if (node != null) {
			printTree(node.left);
			System.out.println(node.element);
			printTree(node.right);
		}
	}

	private SplayNode<T> findMax(SplayNode<T> node) {
		while(node.right != null) {
			node = node.right;
		}
		return node;
	}

	public static void main(String[] args) {
		SplayTree<Integer> st = new SplayTree<>();
		st.insert(5);
		st.insert(4);
		st.insert(3);
		st.insert(2);
		st.insert(1);
//		st.printTree();
//		System.out.println(st.contains(3));
//		st.printTree();
//		st.remove(1);
		System.out.println(st.root.element);
	}
}
