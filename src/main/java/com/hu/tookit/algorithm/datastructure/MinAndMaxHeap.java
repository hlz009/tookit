package com.hu.tookit.algorithm.datastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 最小，最大值堆
 * 结构与二叉堆相同，支持deleteMin和deleteMax两种操作
 * 偶数深度上：任意节点X，存储在X上的元素小于它的父亲，但是大于它的祖父
 * 奇数深度上：任意节点X，存储在X上的元素大于它的父亲，但是小于它的祖父
 * 最小值在偶数深度上，最大值在奇数深度上。
 * @author xiaozhi009
 *
 */
public class MinAndMaxHeap<T extends Comparable<? super T>> {
	private mmNode<T> root;
	private int size;

	private class mmNode<T> {
		mmNode<T> left;
		mmNode<T> right;
		T element;
		
		public mmNode(T element) {
			this.element = element;
		}
	}

	public T findMin() {
		return root.element;
	}

	public T findMax() {
		if (root.left.element.compareTo(root.right.element) > 0) {
			return root.left.element;
		}
		return root.right.element;
	}

	public void insert(T element) {
		if (root == null) {
			size++;
			root = new mmNode<>(element);
			return;
		}
		mmNode<T> current = root;
		int i = 0;
		while(i < size+1) {
			if (i % 2 == 0) {//偶数
				if (current.element.compareTo(element) > 0) {
					T temp = element;
					element = current.element;
					current.element = temp;
				} else if (current.element.compareTo(element) < 0) {
					i++;
					if (current.left != null && current.right != null) {
						if (current.left.element.compareTo(current.right.element) <= 0) {
							current = current.left;
						} else {
							current = current.right;
						}
					} else {
						if (current.left == null) {
							current.left = new mmNode<>(element);
						} else {
							current.right = new mmNode<>(element);						
						}
						size++;
						return;
					}
				} else {
					return;
				}
			} 
			if (i % 2 != 0) {
				if (current.element.compareTo(element) > 0) {
					i++;
					if (current.left != null && current.right != null) {
						if (current.left.element.compareTo(current.right.element) >= 0) {
							current = current.left;
						} else {
							current = current.right;
						}
					} else {
						if (current.left == null) {
							current.left = new mmNode<>(element);
						} else {
							current.right = new mmNode<>(element);
						}
						size++;
						return;
					}
				} else if (current.element.compareTo(element) < 0) {
					T temp = element;
					element = current.element;
					current.element = temp;
				} else {
					return;
				}
			}			
		}
	}

	public void insert2(T element) {
		
	}
	
	public void deleteMin() {
		root = merge(root.left, root.right);
	}

	public void deleteMax() {
		if (root.left.element.compareTo(root.right.element) > 0) {
			mmNode<T> current = root.left;
			current = merge(current.left, current.right);
			root.left = current;
		} else {
			mmNode<T> current = root.right;
			current = merge(current.left, current.right);
			root.right = current;
		}
	}
	
	public mmNode<T> merge(mmNode<T> h1, mmNode<T> h2) {
		if (h1 == null) {
			return h1;
		}
		if (h2 == null) {
			return h2;
		}
		return excuteMerge(h1, h2);
	}

	private mmNode<T> excuteMerge(mmNode<T> h1, mmNode<T> h2) {
		return null;
	}

	/**
	 * 二叉树Z型遍历
	 * @param node
	 * @return
	 */
	public List<T> ZTraversal() {
		mmNode<T> node = root;
		if (null == node) {
			return null;
		}
		List<T> result = new ArrayList<T>();
		Stack<mmNode<T>> leftStack = new Stack<mmNode<T>>();
		Stack<mmNode<T>> rightStack = new Stack<mmNode<T>>();
		leftStack.add(node);
		while (!leftStack.isEmpty() || !rightStack.isEmpty()) {
			while (!leftStack.isEmpty()) {
				mmNode<T> newNode = leftStack.pop();
				result.add(newNode.element);
				if (newNode.left != null) {
					rightStack.push(newNode.left);
				}
				if (newNode.right != null) {
					rightStack.push(newNode.right);
				}
			}
			while (!rightStack.isEmpty()) {
				mmNode<T> newNode = rightStack.pop();
				result.add(newNode.element);
				if (newNode.right != null) {
					leftStack.push(newNode.right);
				}
				if (newNode.left != null) {
					leftStack.push(newNode.left);
				}
			}
		}
		return result;
	}

	@Override
	public String toString() {
		if (root == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		return getResult(sb, root).toString();
	}

	private StringBuffer getResult(StringBuffer sb, mmNode<T> data) {
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
		MinAndMaxHeap<Integer> mm = new MinAndMaxHeap<>();
		mm.insert(6);
		mm.insert(81);
		mm.insert(87);
		mm.insert(14);
		mm.insert(17);
		mm.insert(12);
		mm.insert(28);
		mm.insert(99);
		mm.insert(3);
		System.out.println(mm.ZTraversal());
		System.out.println(mm.root.element);
		System.out.println(mm.findMin());
		System.out.println(mm.findMax());
	}
}
