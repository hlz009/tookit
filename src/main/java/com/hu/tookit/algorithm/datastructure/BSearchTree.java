package com.hu.tookit.algorithm.datastructure;

/**
 * 二叉查找树
 * @author xiaozhi009
 *
 * @param <T>
 */
public class BSearchTree<T extends Comparable<? super T>> extends AbstractTree<T> {
		
	public BSearchTree() {
		root = null;
	}

	// 以下contains，insert， remove，如果没有特殊业务需要，可直接调用
	// 其父类方法。不用在子类中覆写

	@Override
	public boolean contains(T data) {
		/**
		 * or 调用父类 
		 * return super.contains(data);
		 */
		return contains(root, data);
	}

	/**
	 * 非递归形式调用
	 * @param node
	 * @param data
	 * @return
	 */
	private boolean contains(BinaryNode<T> node, T data) {
		boolean contains = false;
		while (node != null) {
			int compareResult = data.compareTo(node.element);
			if (compareResult < 0) {
				node = node.left;
			} else if (compareResult > 0) {
				node = node.right;
			} else {
				contains = true;
				break;
			}
		}
		return contains;
	}

	@Override
	public void insert(T data) {
		/**
		 * or 调用父类 
		 * super.insert(data);
		 */
		insert(root, data);
	}

	/**
	 * 非递归形式，如果插入数据后，需要平衡，则不能这样写
	 * 具体参考AVLTree
	 * @param node
	 * @param data
	 * @return
	 */
	private BinaryNode<T> insert(BinaryNode<T> node, T data) {
		if (null == node) {
			root = new BinaryNode<T>(null, data, null);
			return root;
		}
		BinaryNode<T> current = node;
		BinaryNode<T> newNode = new BinaryNode<T>(null, data, null);
		while(current != null) {
			int compareResult = data.compareTo(current.element);
			if (compareResult < 0) {
				if (current.left == null) {
					current.left = newNode;
					break;
				}
				current = current.left;
			} else if (compareResult > 0) {
				if (current.right == null) {
					current.right = newNode;
					break;
				}
				current = current.right;
			} else {
				break;
			}
		}
		return current;
	}

	@Override
	public void remove(T data) {
		/**
		 * or 调用父类的
		 * super.remove(data);
		 */
//		super.remove(data);
		remove(root, data);
	}

	// 非递归
	private BinaryNode<T> remove(BinaryNode<T> node, T data) {
		if (null == node) {
			return node;
		}
		BinaryNode<T> current = node;
		BinaryNode<T> parent = node;
		while(current != null) {
			int compareResult = data.compareTo(current.element);
			if (compareResult < 0) {
				parent = current;
				current = current.left;
			} else if (compareResult > 0) {
				parent = current;
				current = current.right;
			} else if (current.left != null && current.right != null) {
				BinaryNode<T> sub = current.right;
				while(sub.left != null) {
					// 类似于findMin
					parent = sub;
					sub = sub.left;
				}
				current.element = sub.element;
				// sub最多只有右子节点（因为是删除右子树最小的节点，该节点不可能有左子节点）
				if (parent.left != null) {
					parent.left = sub.right;
				} else {
					parent.right = sub.right;
				}
			} else {
				if (parent.left != null) {
					parent.left = current.left != null? current.left: current.right;
				} else {
					parent.right = current.left != null? current.left: current.right;
				}
				break;
			}
		}
		return parent;
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
		System.out.println(bst.root.element);
		System.out.println(bst.root.left.right.left.element);
//		System.out.println(bst.root.getLeft().getLeft().getElement());
		System.out.println(bst.root.right.element);
	}
}
