package com.hu.tookit.algorithm;

import java.util.Iterator;

/**
 * 链表---单链表结构
 * @author xiaozhi009
 *
 * @param <T>
 */
public class SingleLinkedList<T> implements Iterable<T> {
	private transient int size;
	private transient int modCount = 0;
	private Node<T> head;

	private static class Node<T>{
		public T data;
		public Node<T> next;

		public Node(T data, Node<T> next) {
			this.data = data;
			this.next = next;
		}
	}

	public SingleLinkedList (){
		size = 0;
	}

	public int size() {
		return this.size;
	}

	public boolean isEmpty() {
		return this.size == 0;
	}

	public T add(T o) {
		if (head == null) {
			head = new Node<T>(o, null);
		} else {
			Node<T> current = head;
			while(current.next != null) {
				current = current.next;
			}
			current.next = new Node<T>(o, null);
		}
		size++;
		modCount++;
		return o;
	}

	public T add(int idx, T o) {
		if (idx > size) {
			throw new RuntimeException("超过链接长度");
		}
		int index = 1;
		Node<T> current = head;
		while(index < idx) {
			//得到上一个元素位置
			current = current.next;
			index++;
		}
		Node<T> newNode = new Node<T>(o, null);
		Node<T> next = null;
		if (current.next != null) {
			next = current.next;
		}
		current.next = newNode;
		newNode.next = next;
		size++;
		modCount++;
		return o;
	}

	public T remove(T o) {
		int i = getIdx(o);
		remove(i);
		modCount++;
		return o;
	}

	public T remove(int idx) {
		Node<T> current = head;
		int index = 1;
		while(index < idx) {
			//得到上一个元素位置
			current = current.next;
			index++;
		}
		Node<T> next = current.next;
		current.next = current.next.next;
		return next.data;
	}

	public int getIdx(T o) {
		Node<T> current = head;
		int index = 0;
		while(current != null) {
			if (current.data.equals(o)) {
				return index;
			}
			current = current.next;
			index++;
		}
		return -1;
	}

	public T get(int idx) {
		Node<T> current = head;
		int index = 0;
		while(index < idx) {
			current = current.next;
			index++;
		}
		return current.data;
	}

	/**
	 * 此操作也可以直接更换数据 data
	 * @param idx
	 * @param o
	 * @return
	 */
	public T set(int idx, T o) {
		Node<T> current = head;
		int index = 1;
		while(index < idx) {
			current = current.next;
			index++;
		}
		Node<T> next = null;
		if (current.next != null) {
			next = current.next.next;
		}
		Node<T> newNode = new Node<T>(o, null);
		current.next = newNode;
		newNode.next = next;
		return o;
	}

	public void clear() {
		head = null;
		size = 0;
	}

	@Override
	public String toString() {
		if (size == 0) {
			return "[]";
		}
		Node<T> current = head;
		StringBuffer s = new StringBuffer();
		while(current != null) {
			s.append(current.data + ",");
			current = current.next;
		}
		return s.toString();
	}

	@Override
	public Iterator<T> iterator() {
		return new NewLinkedListIterator();
	}

	private class NewLinkedListIterator implements java.util.Iterator<T> {
		
		private Node<T> current = head;
		private Node<T> prev = head;
		private int expectedCount = modCount; // 保证遍历时集合元素不变
		private boolean okToRemove = false; // 遍历时，元素是否还存在
		
		public NewLinkedListIterator() {
			if (current != null) {
				current = current.next;
			}
		}

		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public T next() {
			if (expectedCount != modCount) {
				throw new RuntimeException("遍历时，迭代器已经修改");
			}
			if (!hasNext()) {
				throw new RuntimeException("遍历的元素不存在");
			}
			T nextData = current.data;
			prev = current;
			current = current.next;
			okToRemove = true;
			return nextData;
		}

		public void remove() {
			if (expectedCount != modCount) {
				throw new RuntimeException("遍历时，迭代器已经修改");
			}
			if (!okToRemove) {
				throw new RuntimeException("遍历时，元素已被删除");
			}
			SingleLinkedList.this.remove(prev.data);
			expectedCount++;
			okToRemove = false;
		}
	}

	/**
	 * 交换两个相邻的元素
	 * (不是交换数据)
	 * @param first
	 * @param second
	 */
	public void swap(T first, T second) {
		int firstIndex = getIdx(first);
		int secondIndex = getIdx(second);
		if (firstIndex < -1 || secondIndex < -1) {
			throw new RuntimeException("元素不存在");
		}
		if (firstIndex == secondIndex || first.equals(second)) {
			throw new RuntimeException("同一个元素不能执行交换操作");
		}
		if (firstIndex > secondIndex) {
			T temp = first;
			first = second;
			second = temp;
			firstIndex = firstIndex + secondIndex;
			secondIndex = firstIndex - secondIndex;
			firstIndex = firstIndex - secondIndex;
		}
		Node<T> currentNode = head;
		if (firstIndex == 0) {
			Node<T> next = currentNode.next.next; // second的下一个
			Node<T> tempNode = currentNode.next; // second
			currentNode.next = next;// 第一个指向第三个
			tempNode.next = currentNode; // 将第二个指向
			head = tempNode;
		} else {
			// 得到上一个
			for (int i = 0; i < firstIndex-1; i++) {
				currentNode = currentNode.next;
			}
			Node<T> next = currentNode.next.next.next; // second的下一个
			Node<T> tempNode = currentNode.next; // first
			currentNode.next = currentNode.next.next; // 交换second
			currentNode.next.next = tempNode;// 交换first
			tempNode.next = next; // 指向下面的元素
		}
	}

//	/**
//	 * 或者直接执行两次set操作
//	   (不是交换数据)
//	   set操作是交换元素
//	 * @param first
//	 * @param second
//	 */
//	public void swap(T first, T second) {
//		int firstIndex = getIdx(first);
//		int secondIndex = getIdx(second);
//		if (firstIndex < -1 || secondIndex < -1) {
//			throw new RuntimeException("元素不存在");
//		}
//		if (firstIndex == secondIndex || first.equals(second)) {
//			throw new RuntimeException("同一个元素不能执行交换操作");
//		}
//		set(firstIndex, second);
//		set(secondIndex, first);
//	}
}
