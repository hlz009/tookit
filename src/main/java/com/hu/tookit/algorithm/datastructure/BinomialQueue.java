package com.hu.tookit.algorithm.datastructure;

import java.lang.reflect.Array;

/**
 * 二项队列
 * @author xiaozhi009
 *
 */
public class BinomialQueue<T extends Comparable<? super T>> {

	private final static int DEFALUTSIZE = 16;
	private BinomialNode<T>[] trees;
	private int size;

	private class BinomialNode<T> {
		BinomialNode<T> leftChild;
		BinomialNode<T> nextSbling;// 可以理解为右子树
		T element;

		public BinomialNode(T element) {
			this(null, null, element);
		}

		public BinomialNode(BinomialNode<T> leftChild, 
				BinomialNode<T> nextSbling, T element) {
			this.leftChild = leftChild;
			this.nextSbling = nextSbling;
			this.element = element;
		}
	
		@Override
		public String toString() {
			StringBuffer sb = new StringBuffer();
			return getResult(sb, this).toString();
		}
		
		private StringBuffer getResult(StringBuffer sb, BinomialNode<T> b) {
			sb.append(b.element + ", ");
			if (b.leftChild != null) {
				getResult(sb, b.leftChild);
			}
			if (b.nextSbling != null) {
				getResult(sb, b.nextSbling);
			}
			return sb;
		}
	}

	public BinomialQueue() {
		this(DEFALUTSIZE);
	}

	@SuppressWarnings("unchecked")
	public BinomialQueue(int length) {
		trees = (BinomialNode<T>[]) Array.newInstance(BinomialNode.class, length);
		size = 0;
	}

	public BinomialQueue(T element) {
		this(1);
		trees[0] = new BinomialNode<T>(element);
		size ++;
	}

	public void insert(T element) {
		merge(new BinomialQueue<T>(element));
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public T deleteMin() {
		if (isEmpty()) {
			return null;
		}
		int minIndex = findMinIndex();
		T minVal = trees[minIndex].element;
		// 保留的node，重新构造队列
		BinomialNode<T> deletedNode = trees[minIndex].leftChild;
		// 根据二项式性质 拆分后的二项树，建立minIndex+1大小的数组装载每个二项树
		BinomialQueue<T> deletedQueue = new BinomialQueue<T>(minIndex + 1);
		deletedQueue.size = 1 << minIndex - 1;
		for (int i = minIndex - 1; i >= 0; i--) {
			deletedQueue.trees[i] = deletedNode;
			deletedNode = deletedNode.nextSbling;
			deletedQueue.trees[i].nextSbling = null;
		}
		// 将原来数组中minIndex的二项树去掉
		trees[minIndex] = null;
		size -= deletedQueue.size +1; // 加1 是因为去掉了删除了根的元素
		merge(deletedQueue);
		return minVal;
	}

	public void merge(BinomialQueue<T> rhs) {
		if (this == rhs) {
			return;
		}
		size += rhs.size;
		if (size > capacity()) {
			// 找出两个树的最大容量，合并之后最多只需要最大的容量+1（二项式的性质决定的）
			int maxLength = Math.max(trees.length, rhs.trees.length);
			expandTrees(maxLength + 1);
		}
		// i-下标，j-元素个数
		BinomialNode<T> carry = null;// 存储上一步骤的合并树
		for (int i = 0, j = 1; j <= size; i++, j*=2) {
			BinomialNode<T> b1 = trees[i];// 当前的树,合并的结果放入当前树中
			BinomialNode<T> b2 = i < rhs.trees.length? rhs.trees[i]: null;
			// 8种情况合并
			carry = merge(b1, b2, carry, i);
		}
	
		// 清除被合并的二项队列--便于JVM回收
		for (int i = 0, len = rhs.trees.length; i < len; i++) {
			rhs.trees[i] = null;
		}
		rhs.size = 0;
	}

	/**
	 * 主要有4种情况 （1）b1和b2均为空;(2)b1空，b2不空;(3)b1不空，b2空;(4)b1,b2均不为空
	 * 加上上一步骤传过来的树（为空和不为空两种情况），共计8种情况
	 * @param b1
	 * @param b2
	 * @param carry
	 * @param index
	 */
	private BinomialNode<T> merge(BinomialNode<T> b1, BinomialNode<T> b2, 
			BinomialNode<T> carry, int index) {
		int whichCase = b1 == null? 0: 1;
		whichCase += b2 == null? 0: 2;
		whichCase += carry == null? 0: 4;
		
		switch (whichCase) {
			case 0:// 均为空
				break;
			case 1:// b1不为空
				trees[index] = b1;
				break;
			case 2:// b2不为空
				trees[index] = b2;
				break;
			case 3:// b1,b2不为空
				carry = combineTree(b1, b2);
				trees[index] = null;
				break;
			case 4:// carry不为空
				trees[index] = carry;
				carry = null;
				break;
			case 5:// b1, carry不为空
				carry = combineTree(b1, carry);
				trees[index] = null;
				break;
			case 6:// b2, carry不为空
				carry = combineTree(b2, carry);
				trees[index] = null;
				break;
			case 7:// 都不为空，留下carry
				trees[index] = carry;
				carry = combineTree(b1, b2);
				break;
			default:
				break;
		}
		return carry;
	}

	private void expandTrees(int length) {
		BinomialNode<T>[] oldTrees = trees;
		trees = (BinomialNode<T>[]) Array.newInstance(BinomialNode.class, length);
		for (int i = 0, len = oldTrees.length; i < len; i++) {
			if (oldTrees[i] != null) {
				trees[i] = oldTrees[i];
			}
		}
	}

	/**
	 * 相同数量节点的树合并
	 * 让大树的根成为小树的子树（这里设定为左子树）
	 * 图中所示代码  根元素 只有一个子树
	 * （只能保证根元素最小，其他元素不一定满足比子节点都小这个规则）
	 * @param b1
	 * @param b2
	 * @return
	 */
	private BinomialNode<T> combineTree(BinomialNode<T> b1, 
			BinomialNode<T> b2) {
		if (b1.element.compareTo(b2.element) > 0) {
			return combineTree(b2, b1);
		}
		b2.nextSbling = b1.leftChild;
		b1.leftChild = b2;
		return b1;
	}

	/**
	 * 计算的最大容量
	 * （此种情况认为所有的空间都占满元素）
	 * @return
	 */
	private int capacity() {
		return 1 << trees.length - 1;
	}
	
	private int findMinIndex() {
		T minVal = null;
		int index = -1;
		// 从第二个元素开始比较
		for (int i = 0, j = 1; j <= size; i++, j*=2) {
			if (null == minVal && null != trees[i]) {
				minVal = trees[i].element;
				index = i;
				continue;
			}
			if (null != trees[i] && minVal.compareTo(trees[i].element) > 0) {
				minVal = trees[i].element;
				index = i;
				break;
			}
		}
		return index;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (BinomialNode<T> b: trees) {
			if (null != b) {
				sb.append(b);
			}
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		BinomialQueue<Integer> bq = new BinomialQueue<>();
		bq.insert(1);
		bq.insert(2);
		bq.insert(3);
		bq.insert(4);
		bq.insert(5);
		System.out.println(bq);
		for (int i = 0; i < 20; i++) {
			bq.deleteMin();
			System.out.println(bq);
		}
	}
}
