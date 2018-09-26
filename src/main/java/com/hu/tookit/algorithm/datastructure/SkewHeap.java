package com.hu.tookit.algorithm.datastructure;

/**
 * 斜堆
 * 与左式堆类似，但是不保留零路径长
 * @author xiaozhi009
 *
 * @param <T>
 */
public class SkewHeap <T extends Comparable<? super T>>{
	private SkewNode<T> root;

	@SuppressWarnings("hiding")
	private class SkewNode<T> {
		SkewNode<T> left;
		SkewNode<T> right;
		T element;
		
		public SkewNode(T element) {
			this(null, element, null);
		}

		public SkewNode(SkewNode<T> left, T element, SkewNode<T> right) {
			this.left = left;
			this.right = right;
			this.element = element;
		}
	}

	public boolean isEmpty() {
		return root == null;
	}

	/**
	 * 大根的左式堆与小根左式堆的右子堆（右儿子）合并
	 * @param anotherLeftistHeap
	 */
	public void merge(SkewHeap<T> anotherLeftistHeap) {
		root = merge(root, anotherLeftistHeap.root);
//		root = merge2(root, anotherLeftistHeap.root);
	}

	private SkewNode<T> merge(SkewNode<T> h1, SkewNode<T> h2) {
		if (null == h1) {
			return h2;
		}
		if (null == h2) {
			return h1;
		}
		if (h1.element.compareTo(h2.element) < 0) {
			return executeMerge(h1, h2);
		} else {
			return executeMerge(h2, h1);
		}
	}

	/**
	 * 递归实现 O(logN)
	 * @param h1
	 * @param h2
	 * @return
	 */
	private SkewNode<T> executeMerge(SkewNode<T> h1, SkewNode<T> h2) {
		if (h1.left == null) {// 单节点
			h1.left = h2;
		} else {
			h1.right = merge(h1.right, h2);
			// 右路径最后一个节点不交换,该节点是没有有子节点（也可能没左节点）
			if (h1.right != null) {
				// 交换左右儿子
				swapChildren(h1);
			}
		}
		return h1;
	}

	/**
	 * 非递归实现 O(logN)
	 * @param h1
	 * @param h2
	 * @return
	 */
	private SkewNode<T> merge2(SkewNode<T> h1, SkewNode<T> h2) {
		if (null == h1) {
			return h2;
		}
		if (null == h2) {
			return h1;
		}
		SkewNode<T> current = null;
		SkewNode<T> h = null;
		// 第一步：合并两个堆的右路径
		while(h1 != null && h2 != null) {
			if (h1.element.compareTo(h2.element) < 0) {
				if (null == current) {
					current = h1;
					h = current;
				} else {
					current.right = h1;
					current = current.right;
				}
				h1 = h1.right;
			} else {
				if (null == current) {
					current = h2;
					h = current;
				} else {
					current.right = h2;
					current = current.right;
				}
				h2 = h2.right;
			}
		}
		// 将剩下的子堆拼接，并将子堆的npl加1放到上一个根元素上
		if (h1 == null && h2 != null) {
			current.right = h2;
		} else if (h1 != null && h2 == null) {
			current.right = h1;
		}
		// 构建堆-交换左右儿子，左儿子不动
		SkewNode<T> next = h;
		while(next != null) {
			if (next.right != null) {
				swapChildren(next);
			}
			next = next.right;
		}
		return h;
	}

	private SkewNode<T> swapChildren(SkewNode<T> h) {
		SkewNode<T> temp = h.right;
		h.right = h.left;
		h.left = temp;
		return h;
	}

	public void insert(T data) {
//		root = merge(root, new SkewNode<T>(data));
		root = merge2(root, new SkewNode<T>(data));
	}

	public T findMin() {
		return root.element;
	}

	public T deleteMin() {
		if (null == root) {
			return null;
		}
		// 去掉根元素 合并两个子堆即可
		T element = root.element;
		root = merge(root.left, root.right);
		return element;
	}

	@Override
	public String toString() {
		if (root == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		return getResult(sb, root).toString();
	}

	private StringBuffer getResult(StringBuffer sb, SkewNode<T> data) {
		sb.append(data.element + ", ");
		if (data.left != null) {
			getResult(sb, data.left);
		}
		if (data.right != null) {
			getResult(sb, data.right);
		}
		return sb;
	}

	public static void main(String[] args) {
		SkewHeap<Integer> sh = new SkewHeap<Integer>();
		sh.insert(10);
		sh.insert(1);
		sh.insert(2);
		sh.insert(13);
		sh.insert(9);
		sh.insert(6);
		
//		SkewHeap<Integer> sh2 = new SkewHeap<Integer>();
//		sh2.insert(20);
//		sh2.insert(4);
//		sh2.insert(8);
//		sh.merge(sh2);
		System.out.println(sh);
		sh.deleteMin();
////		sh.deleteMin();
		System.out.println(sh);
	}
}
