package com.hu.tookit.algorithm.datastructure;

import java.util.ArrayList;
import java.util.Collections;

/**
 * B+树
 * 定义：所有的数据项都在叶子节点上
 * @author xiaozhi009
 *
 */
public class BPlusTree<T extends Comparable<? super T>> {
	
	private static final int DEFAULT_KEY_MIN_NUM = 2;
	private static final int DEFAULT_M = 5;
	private static final int DEFAULT_L = 5;

	private int m = DEFAULT_M;// M阶
	private int keyMaxNum = m-1;// 节点中包含关键字最大个数
	private int keyMinNum = m/2 -1;// 非根节点中包含关键字最小个数
	private int childMaxNum = keyMaxNum + 1;// 子节点最大数
	private int childMinNum = keyMinNum + 1;// 子节点最小数
	private int l = DEFAULT_L;// 数据区域大小
	private int dataMaxNum = l;
	private int dataMinNum = l/2 + 1;

	private BPlusNode<T> root;

	/**
	 * 也可以将keys和data合并，叶子节点是数据，索引节点是关键字
	 * 注意两者的长度不是必须要相等的
	 * @author xiaozhi009
	 *
	 * @param <T>
	 */
	private class BPlusNode<T> {
		boolean isLeaf;//是否指向叶子节点
		ArrayList<T> keys;// 关键字数组
		ArrayList<T> data;// 数据存储数组 ---（叶子节点）
		/**
		 * 可以增加这个属性标识各个叶子节点的关系,这样合并节点时逻辑会简单些
		 */
//		BPlusNode<T> sblingNode;//相邻叶子节点---（叶子节点）
		ArrayList<BPlusNode<T>> child;// 子节点
		BPlusNode<T> parent;// 父节点

		public BPlusNode(boolean isLeaf) {
			this.isLeaf = isLeaf;
			if (isLeaf) {
				this.data = new ArrayList<>(dataMaxNum + 1);
//				this.sblingNode = null;
			} else {
				this.keys = new ArrayList<>(keyMaxNum + 1); 
				this.child = new ArrayList<>(childMaxNum + 1);
			}
		}
		
		public String toString() {
			System.out.println("isLeaf-->"+ this.isLeaf);
			System.out.println("keys-->" + this.keys);
			System.out.println("data-->" + this.data);
//			System.out.println("child-->" + this.child);
			return  null;
		}
	}

	
	public BPlusTree() {
		this(-1, -1);
	}

	public BPlusTree(int m) {
		this(m, -1);
	}

	public BPlusTree(int m, int l) {
		this.root = null;
		if (m != -1) {
			this.m = m;
		}
		if (l != -1) {
			this.l = l;
		}
		if (this.keyMinNum < DEFAULT_KEY_MIN_NUM) {
			this.keyMinNum = DEFAULT_KEY_MIN_NUM;
			this.childMinNum = this.keyMinNum + 1;
		}
	}

	public boolean isEmpty() {
		return root == null;
	}
	
	public void insert(T element) {
		root = insert(root, element);
	}

	/**
	 *  （1）针对叶子类型结点：根据key值找到叶子结点，向这个叶子结点插入记录。
	 *  插入后，若当前结点key的个数小于等于m-1，则插入结束。
	 *  否则将这个叶子结点分裂成左右两个叶子结点，左叶子结点包含前m/2个记录，
	 *  右结点包含剩下的记录，将第m/2+1个记录的key进位到父结点中（父结点一定是索引类型结点），
	 *  进位到父结点的key左孩子指针向左结点,右孩子指针向右结点。
	 *  将当前结点的指针指向父结点。然后继续操作索引节点
	 *  （2）针对索引类型结点：若当前结点key的个数小于等于m-1，则插入结束。
	 *  否则，将这个索引类型结点分裂成两个索引结点，左索引结点包含前(m-1)/2个key，
	 *  右结点包含m-(m-1)/2个key，将第m/2个key进位到父结点中，
	 *  进位到父结点的key左孩子指向左结点, 进位到父结点的key右孩子指向右结点。
	 *  将当前结点的指针指向父结点，然后重复此步骤。
	 * @param node
	 * @param element
	 * @return
	 */
	private BPlusNode<T> insert(BPlusNode<T> node, T element) {
		if(node == null) {
			/**
			 * 叶子节点为根节点
			 */
			node = new BPlusNode<>(true);
			node.data.add(element);
			return node;
		}
		/**
		 * 找到要插入的位置叶子节点
		 */
		BPlusNode<T> currentNode = findInsertPosition(node, element);
		if(currentNode.data.contains(element)) {
			/**
			 * 元素存在，什么也不做
			 */
			return node;
		}
		currentNode.data.add(element);
		sort(currentNode.data);
		if (currentNode.data.size() > dataMaxNum) {
			/**
			 *  扩容，从底到上
			 *  新建叶子节点，并向上合并父节点
			 */
			BPlusNode<T> newNode = new BPlusNode<T>(true);
			BPlusNode<T> parentNode = currentNode.parent;
			if (parentNode == null) {
				parentNode = new BPlusNode<>(false);
				currentNode.parent = parentNode;
			}
			newNode.parent = parentNode;
			int idx = (dataMaxNum + 1)/2;
			for (int i = idx; i <= dataMaxNum; i++) {
				/**
				 * 可以一次循环中，将两个叶子节点的数据写好。
				 */
				newNode.data.add(currentNode.data.get(idx));
				currentNode.data.remove(idx);
			}
			/**
			 * 将新节点的第一个元素放入父节点的keys中
			 */
			T keyElement = newNode.data.get(0);
			parentNode.keys.add(keyElement);
			sort(parentNode.keys);
			if (parentNode.child.isEmpty()) {
				parentNode.child.add(currentNode);
			}
			parentNode.child.add(parentNode.keys.indexOf(keyElement)+1, newNode);
			while(parentNode != null) {
				node = parentNode;
				BPlusNode<T> grandParentNode = parentNode.parent;
				if (parentNode.keys.size() > keyMaxNum) {
					if (grandParentNode == null) {
						grandParentNode = new BPlusNode<>(false);
						parentNode.parent = grandParentNode;
					}
					BPlusNode<T> newParentNode = new BPlusNode<T>(false);
					newParentNode.parent = grandParentNode;
					int middleKeyIdx = (keyMaxNum + 1)/2;
					/**
					 *  中间关键节点要放到grandParentNode中
					 */
					T middleKeyElement = parentNode.keys.get(middleKeyIdx);
					grandParentNode.keys.add(middleKeyElement);
					/**
					 *  删除原来的中间元素关键字key
					 */
					parentNode.keys.remove(middleKeyIdx);
					newParentNode.child.add(parentNode.child.get(middleKeyIdx+1));
					/**
					 * 替换新的指向父节点关系
					 */
					parentNode.child.get(middleKeyIdx+1).parent = newParentNode;
					parentNode.child.remove(middleKeyIdx+1);
					for (int i = middleKeyIdx; i < keyMaxNum; i++) {
						newParentNode.keys.add(parentNode.keys.get(middleKeyIdx));
						newParentNode.child.add(parentNode.child.get(middleKeyIdx + 1));
						parentNode.child.get(middleKeyIdx+1).parent = newParentNode;
						parentNode.keys.remove(middleKeyIdx);
						parentNode.child.remove(middleKeyIdx + 1);
					}
					sort(grandParentNode.keys);
					if (grandParentNode.child.isEmpty()) {
						grandParentNode.child.add(parentNode);
					}
					grandParentNode.child.add(grandParentNode.keys.indexOf(middleKeyElement)+1, newParentNode);
				}
				parentNode = grandParentNode;
			}
		} 
		return node;
	}

	private ArrayList<T> sort(ArrayList<T> list ) {
		Collections.sort(list);
		return list;
	}
	
	private BPlusNode<T> findInsertPosition(BPlusNode<T> node, T element) {
		if (null == node) {
			return null;
		}
		BPlusNode<T> currentNode = node;
		while(!currentNode.isLeaf) {
			int index = 0;
			for (T key: currentNode.keys) {
				if (element.compareTo(key) < 0) {
					break;
				}
				index++;
			}
			currentNode = currentNode.child.get(index);
		}
		return currentNode;
	}

	public boolean contains(T element) {
		BPlusNode<T> currentNode = findInsertPosition(root, element);
		if (null == currentNode) {
			return false;
		}
		return currentNode.data.contains(element);
	}

	public void remove(T element) {
		root = remove(root, element);
	}

	/**
	 * 合并分两种：
	 * （1）若兄弟结点数据key有富余（大于Math.ceil(m)/2 – 1），向兄弟结点借一个记录，
	 * 同时用借到的key替换父结（指当前结点和兄弟结点共同的父结点）点中的key，删除结束。
	 * （2）若兄弟结点中没有富余的数据key,则当前结点和兄弟结点合并成一个新的叶子结点，
	 * 并删除父结点中的key（父结点中的这个key两边的孩子指针就变成了一个指针，正好指向这个新的叶子结点），
	 * 将当前结点指向父结点（必为索引结点），然后更新索引节点
	 * @param node
	 * @param element
	 * @return
	 */
	private BPlusNode<T> remove(BPlusNode<T> node, T element) {
		if (null == node) {
			return null;
		}
		BPlusNode<T> currentNode = findInsertPosition(node, element);
		BPlusNode<T> parentNode = currentNode.parent;
		/**
		 * 如果存在parent获取当前删除节点的下标。
		 */
		int currentNodeIndex = -101;
		if (parentNode != null) {
			currentNodeIndex = parentNode.keys.indexOf(currentNode.data.get(0));
		}
		boolean isRemove = currentNode.data.remove(element);
		System.out.println("要删除的元素---->" + element);
		if (isRemove) {
			if (currentNode.data.size() < dataMinNum) {
				if (node == currentNode) {
					return node;
				}
				/**
				 * 需要合并父节点时，如果删除的时第一个子节点，我们朝其下一个节点合并，
				 * 否则，我们朝其前一个节点合并
				 */
				parentNode = mergeAndBorrowLeafNode(currentNode, parentNode, currentNodeIndex);
				while(parentNode != null) {
					node = parentNode;
					BPlusNode<T> grandParentNode = parentNode.parent;
					if (parentNode.keys.size() < keyMinNum && grandParentNode != null) {
						grandParentNode = mergeAndBorrowParentNode(parentNode, grandParentNode);
					}
					parentNode = grandParentNode;
				}
			}
		}
		return node;
	}

	private BPlusNode<T> mergeAndBorrowParentNode(BPlusNode<T> parentNode, BPlusNode<T> grandParentNode) {
		int parentMergeAndBorrowIndex = grandParentNode.child.indexOf(parentNode);
		if (parentMergeAndBorrowIndex == 0) {// 第一个
			BPlusNode<T> nextParentNode = grandParentNode.child.get(1);
			if (nextParentNode.keys.size() > keyMinNum) {
				parentNode.keys.add(nextParentNode.keys.get(0));
				parentNode.child.add(nextParentNode.child.get(0));
				nextParentNode.keys.remove(0);
				nextParentNode.child.remove(0);
			} else {
				// 合并节点
				// 父节点key下移，放到nextParentNode上
				nextParentNode.keys.add(0, grandParentNode.keys.get(0));
				grandParentNode.keys.remove(0);
				grandParentNode.child.remove(0);
				nextParentNode.child.add(0, parentNode.child.get(parentNode.child.size()-1));
				parentNode.child.get(parentNode.child.size()-1).parent = nextParentNode;
				parentNode.child.remove(parentNode.child.size()-1);
				for(int i = 0, len = parentNode.keys.size(); i < len; i++) {
					nextParentNode.keys.add(i, parentNode.keys.get(i));
					nextParentNode.child.add(i, parentNode.child.get(i));
					parentNode.child.get(i).parent = nextParentNode;
					parentNode.keys.remove(i);
					parentNode.child.remove(i);
				}
			}
		} else {
			BPlusNode<T> prevParentNode = grandParentNode.child.get(parentMergeAndBorrowIndex - 1);
			if (prevParentNode.keys.size() > keyMinNum) {
				int prevBorrowIndex = prevParentNode.keys.size()-1;
				parentNode.keys.add(0, prevParentNode.keys.get(prevBorrowIndex));
				parentNode.child.add(0, prevParentNode.child.get(prevBorrowIndex));
				prevParentNode.keys.remove(prevBorrowIndex);
				prevParentNode.child.remove(prevBorrowIndex);
			} else {
				// 合并节点, 父节点key下移，放到prevParentNode上
				prevParentNode.keys.add(grandParentNode.keys.get(parentMergeAndBorrowIndex-1));
				grandParentNode.keys.remove(parentMergeAndBorrowIndex-1);
				grandParentNode.child.remove(parentMergeAndBorrowIndex);
				prevParentNode.child.add(parentNode.child.get(0));
				parentNode.child.get(0).parent = prevParentNode;
				parentNode.child.remove(0);
				for(int i = 0, len = parentNode.keys.size(); i < len; i++) {
					prevParentNode.keys.add(parentNode.keys.get(i));
					prevParentNode.child.add(parentNode.child.get(i));
					parentNode.child.get(i).parent = prevParentNode;
					parentNode.keys.remove(i);
					parentNode.child.remove(i);
				}
			}
		}
		if (grandParentNode.keys.size() == 0) {
			//说明父节点已经没有了
			grandParentNode = null;
		}
		return grandParentNode;
	}

	private BPlusNode<T> mergeAndBorrowLeafNode(BPlusNode<T> currentNode, BPlusNode<T> parentNode, 
			int currentNodeIndex) {
		if (currentNodeIndex == -1) {// 删除的是第一个节点：
			BPlusNode<T> nextNode = parentNode.child.get(1);
			if (nextNode.data.size() > dataMinNum) {
				// 从下一个节点借一个数据，不需要合并父类节点
				currentNode.data.add(nextNode.data.get(0));
				nextNode.data.remove(0);
				parentNode.keys.set(0, nextNode.data.get(0));
			} else {
				// 与下一个节点合并
				for (int i = 0, len = currentNode.data.size(); i < len; i++) {
					nextNode.data.set(i, currentNode.data.get(i));
				}
				parentNode.keys.remove(0);
				parentNode.child.remove(0);
			}
		} else {
			int mergeAndBorroweIndex = currentNodeIndex;
			BPlusNode<T> prevNode = parentNode.child.get(mergeAndBorroweIndex);
			if (prevNode.data.size() > dataMinNum) {
				// 从上一个节点借一个数据，不需要合并父类节点
				currentNode.data.add(prevNode.data.get(prevNode.data.size() - 1));
				prevNode.data.remove(prevNode.data.size() - 1);
				parentNode.keys.set(0, prevNode.data.get(prevNode.data.size() - 1));
			} else {
				// 与上一个节点合并
				for (int i = 0, len = currentNode.data.size(); i < len; i++) {
					prevNode.data.add(currentNode.data.get(i));
				}
				parentNode.keys.remove(mergeAndBorroweIndex);
				parentNode.child.remove(mergeAndBorroweIndex+1);
			}
		}
		return parentNode;
	}

	public static void main(String[] args) {
		BPlusTree<Integer> bpt = new BPlusTree<Integer>();
		for (int i = 0; i < 99; i++) {
			bpt.insert(i);
		}
//		int countTrue = 0;
//		for(int i = 0; i < 5; i++) {
//			if (bpt.contains(i)) {
//				countTrue++;
//			} else {
//				System.out.println("找不到的数" + i);
//			}
//		}
//		System.out.println("共有多少个数" + countTrue);
//		bpt.remove(2);
//		System.out.println(bpt.root.isLeaf);
//		System.out.println(bpt.root.keys);
		System.out.println(bpt.root.data);
		ArrayList<BPlusTree<Integer>.BPlusNode<Integer>> childs = bpt.root.child;
		for (BPlusTree<Integer>.BPlusNode<Integer> child: childs) {
			System.out.println(child.child);
		}
//		bpt.remove(5);
//		bpt.remove(6);
//		for (int i = 0; i < 40; i++) {
//			bpt.remove(i);
//		}
//		for (int i = 40; i < 99; i++) {
//			bpt.remove(i);
//		}
		childs = bpt.root.child;
		for (BPlusTree<Integer>.BPlusNode<Integer> child: childs) {
			System.out.println(child.child);
		}
	}
}
