package com.hu.tookit.algorithm.datastructure;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import org.springframework.boot.info.BuildProperties;

/**
 * 左式堆
 * @author xiaozhi009
 *
 */
public class LeftistHeap<T extends Comparable<? super T>> {

	private LeftistNode<T> root;
	private int size;
	
	public LeftistHeap() {
		root = null;
	}

	public LeftistHeap(T[] elements) {
		buildHeap(elements);
	}

	private class LeftistNode<T> {
		LeftistNode<T> left;
		LeftistNode<T> right;
		int npl;
		T element;
		
		public LeftistNode(T element) {
			this(null, element, null);
		}

		public LeftistNode(LeftistNode<T> left, T element, LeftistNode<T> right) {
			this.left = left;
			this.right = right;
			this.npl = 0;
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
	public void merge(LeftistHeap<T> anotherLeftistHeap) {
//		root = merge(root, anotherLeftistHeap.root);
		size += anotherLeftistHeap.size;
		root = merge2(root, anotherLeftistHeap.root);
		anotherLeftistHeap = null;// 空引用，便于JVM回收
	}

	private void buildHeap(T[] elements) {
		Queue<LeftistNode<T>> result = buildQueue(elements);
		while(result.size() > 1) {
			LeftistNode<T> first = result.poll();
			LeftistNode<T> second = result.poll();
			first = merge2(first, second);
			result.add(first);
		}
		root = result.poll();
	}
	
	private Queue<LeftistNode<T>> buildQueue(T[] elements) {
		Queue<LeftistNode<T>> result = new ArrayBlockingQueue<>(elements.length);
		for (T element: elements) {
			result.add(new LeftistNode<T>(element));
		}
		return result;
	}

	private LeftistNode<T> merge(LeftistNode<T> h1, LeftistNode<T> h2) {
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
	private LeftistNode<T> executeMerge(LeftistNode<T> h1, LeftistNode<T> h2) {
		if (h1.left == null) {// 单节点
			h1.left = h2;
		} else {
			h1.right = merge(h1.right, h2);
			if (h1.right.npl > h1.left.npl) {
				// 交换左右儿子
				swapChildren(h1);
			}
			h1.npl = h1.right.npl + 1;
		}
		return h1;
	}

	/**
	 * 非递归实现 O(logN)
	 * @param h1
	 * @param h2
	 * @return
	 */
	private LeftistNode<T> merge2(LeftistNode<T> h1, LeftistNode<T> h2) {
		if (null == h1) {
			return h2;
		}
		if (null == h2) {
			return h1;
		}
		LeftistNode<T> current = null;
		LeftistNode<T> h = null;
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
		if (current.left != null) {
			// 按最小的取，如果左子堆不存在，则npl为0
			current.npl = current.right.npl+1;
		}
		// 构建堆-交换左右儿子，左儿子不动
		LeftistNode<T> next = h;
		while(next != null) {
			if (next.left == null || next.right != null && 
					next.right.npl > next.left.npl) {
				swapChildren(next);
				if (next.right != null) {
					next.npl = next.right.npl+1;
				}
			}
			next = next.right;
		}
		return h;
//		while(next != null && next.right != null) {
//			if (next.left == null || next.right.npl > next.left.npl) {
//				swapChildren(next);
//				if (next.right == null) {
//					next.npl = 0;
//				} else {
//					next.npl = next.right.npl+1;
//				}
//				continue;
//			}
//			next = next.right;
//		}
	}

	private LeftistNode<T> swapChildren(LeftistNode<T> h) {
		LeftistNode<T> temp = h.right;
		h.right = h.left;
		h.left = temp;
		return h;
	}

	public void insert(T data) {
//		root = merge(root, new LeftistNode<T>(data));
		root = merge2(root, new LeftistNode<T>(data));
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
//		root = merge2(root.left, root.right);
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

	private StringBuffer getResult(StringBuffer sb, LeftistNode<T> data) {
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
//		LeftistHeap<Integer> lh = new LeftistHeap<Integer>();
//		lh.insert(10);
//		lh.insert(1);
//		lh.insert(2);
//		lh.insert(13);
//		lh.insert(9);
//		lh.insert(6);
		
		Integer[] nums = {1, 2, 3, 4, 5, 6, 7, 8};
		LeftistHeap<Integer> lh = new LeftistHeap<Integer>(nums);
		
//		LeftistHeap<Integer> lh2 = new LeftistHeap<Integer>();
//		lh2.insert(20);
//		lh2.insert(4);
//		lh2.insert(8);
//		lh.merge(lh2);
		System.out.println(lh);
//		lh.deleteMin();
////		lh.deleteMin();
//		System.out.println(lh);
		

	}
}
