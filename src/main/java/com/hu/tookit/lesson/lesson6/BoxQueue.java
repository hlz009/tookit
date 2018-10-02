package com.hu.tookit.lesson.lesson6;

import java.lang.reflect.Array;

/**
 * 盒子序列
 * 每个盒子最多容纳重量C，
 * 用最少的盒子装纳
 * @author xiaozhi009
 *
 */
public class BoxQueue {

	private final static int DEFALUTSIZE = 16;
	private final static int CAPACITY_LIMIT = 5;
	
	private int capacityLimit;// 容器容量重量限制
	private BoxNode<Integer>[] table;
	private int size;

	private class BoxNode<Integer> {
		BoxNode<Integer> nextSbling;// 可以理解为右子树
		Integer element;

		public BoxNode(Integer element) {
			this(null, element);
		}

		public BoxNode(BoxNode<Integer> nextSbling, 
				Integer element) {
			this.nextSbling = nextSbling;
			this.element = element;
		}
	
		@Override
		public String toString() {
			StringBuffer sb = new StringBuffer();
			return getResult(sb, this).toString();
		}
		
		private StringBuffer getResult(StringBuffer sb, BoxNode<Integer> b) {
			sb.append(b.element + ", ");
			
			return sb;
		}
	}

	public BoxQueue(int capacity) {
		this(DEFALUTSIZE, CAPACITY_LIMIT);
	}

	@SuppressWarnings("unchecked")
	public BoxQueue(int length, int capacityLimit) {
		this.table = (BoxNode<Integer>[]) Array.newInstance(BoxNode.class, length);
		this.size = 0;
		this.capacityLimit = capacityLimit;
	}

	public BoxQueue(Integer[] elements) {
		this(elements, CAPACITY_LIMIT);
	}

	public BoxQueue(Integer[] elements, int capacityLimit) {
		this(elements.length, capacityLimit);
		buildHeap(elements);
	}

	/**
	 * 按顺序存放盒子中，超过容量，移入新的盒子中
	 * 
	 * 后期改进方向（两种要在BoxNode中存放剩余容量变量）：
	 * （1）把重物放入最大容量（剩余）的盒子中， 
	 * （2）把重物放入能够容纳（恰好）而又不过载的盒子中。
	 * @param elements
	 */
	private void buildHeap(Integer[] elements) {
		int index = 0;
		for (int i = 0, len = elements.length; i < len;) {
			if (table[index] == null) {
				table[index] = new BoxNode<>(elements[i]);
				i++;
				size++;
			} else {
				BoxNode<Integer> current = table[index];
				int sum = current.element;
				while(current.nextSbling != null) {
					sum += table[index].element;
					current = current.nextSbling;
				}
				if (sum + elements[i] <= capacityLimit) {
					current.nextSbling = new BoxNode<>(elements[i]);
					i++;
				} else {
					// 跳到下一个
					index++;
				}
			}
		}
	}
	
	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	private void expandTrees(int length) {
		BoxNode<Integer>[] oldTrees = table;
		table = (BoxNode<Integer>[]) Array.newInstance(BoxNode.class, length);
		for (int i = 0, len = oldTrees.length; i < len; i++) {
			if (oldTrees[i] != null) {
				table[i] = oldTrees[i];
			}
		}
	}

	/**
	 * 计算的最大容量
	 * （此种情况认为所有的空间都占满元素）
	 * @return
	 */
	private int capacity() {
		return 1 << table.length - 1;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (BoxNode<Integer> b: table) {
			if (null != b) {
				sb.append(b);
			}
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		Integer[] nums = {2,2,3,3};
		BoxQueue bq = new BoxQueue(nums, 5);
//		bq.insert(1);
//		bq.insert(2);
//		bq.insert(3);
//		bq.insert(4);
//		bq.insert(5);
//		bq.insert(6);
		System.out.println(bq);
		System.out.println("使用的盒子数量：->" + bq.size);
//		for (int i = 0; i < 20; i++) {
//			bq.deleteMin();
//			System.out.println(bq);
//		}
	}
}
