package com.hu.tookit.algorithm;

/**
 * 链表---双链表结构（非循环）
 * @author xiaozhi009
 * @param <T>
 *
 * @param <T>
 */
public class DoubleLinkedList<T> {
	
	private Node<T> head;
	private Node<T> tail;
	private transient int size = 0;
	private transient int modCount = 0;

	private class Node<T> {
		Node<T> prev;
		Node<T> next;
		T value;

		public Node(Node<T> prev, T value, Node<T> next) {
			this.prev = prev;
			this.next = next;
			this.value = value;
		}
	}

	public void add(T data) {
		if (null == head) {
			head = new Node<T>(tail, data, null);
			tail = head;
			size++;
			return;
		}
		Node<T> currentNode = head;
		while(currentNode.next != null) {
			currentNode = currentNode.next;
		}
		Node<T> nextNode = new Node<T>(currentNode, data, null);
		currentNode.next = nextNode;
		tail = nextNode;
		size++;
		System.out.println(size);
		modCount++;
	}

	/**
	 * 交换元素，不是直接交换数据
	 * 新建交换好顺序的元素替换
	 * @param first
	 * @param second
	 */
	public void swap2(T first, T second) {
		int firstIdx = getIdx(first);
		int secondIdx = getIdx(second);
		int idx = Math.min(firstIdx, secondIdx);
		
	}
	
	
	/**
	 * 交换元素，不是直接交换数据
	 * 每次新建一个元素替换
	 * @param first
	 * @param second
	 */
	public void swap(T first, T second) {
		int firstIdx = getIdx(first);
		int secondIdx = getIdx(second);
		reset(firstIdx, second);
		reset(secondIdx, first);
	}

	public void reset(int index, T data) {
		Node<T> currentData = null;
		if (index < size/2) { // 正序
			currentData = head;
			for (int i = 0; i < index; i++) {
				currentData = currentData.next; 
			}
		} else { // 反序
			currentData = tail;
			for (int i = size-1; i > index; i--) {
				currentData = currentData.prev;
			}
		}
		Node<T> newNode = new Node<>(currentData.prev, data, currentData.next);
		if (index == size-1) {
			tail = newNode;
		} else {
			currentData.next.prev = newNode;
		}
		if (index == 0) {
			head = newNode;
		} else {
			currentData.prev.next = newNode;
		}
	}

	public int getIdx(T data) {
		Node<T> currentData = head;
		int idx = 0;
		while(currentData != null) {
			if (currentData.value.equals(data)) {
				break;
			}
			currentData = currentData.next;
			idx++;
		}
		return idx;
	}

	@Override
	public String toString() {
		if (size == 0) {
			return "[]";
		}
		Node<T> current = head;
		StringBuffer s = new StringBuffer();
		while(current != null) {
			s.append(current.value + ",");
			current = current.next;
		}
		return s.toString();
	}
}
