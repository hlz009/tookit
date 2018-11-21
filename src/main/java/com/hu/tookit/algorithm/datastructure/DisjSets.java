package com.hu.tookit.algorithm.datastructure;

/**
 * 不相交集
 * @author xiaozhi009
 *
 */
public class DisjSets {
	private int[] s;

	public DisjSets(int num) {
		s = new int[num];
		for (int i = 0; i < s.length; i++) {
			// 初始值为-1，可以理解为只有一个元素
			s[i] = -1;
		}
	}

	/**
	 * 路径压缩查找
	 * 从x到根的路径上的每一个节点都使其父节点称为该树的根
	 * 该方法与unionBySize可以完美兼容使用
	 * 该方法与unionByHeight不能完美兼容使用，因为路径压缩后高度无法有效计算
	 * @param x
	 * @return
	 */
	public int findByPathCompression(int x) {
		if (s[x] < 0) {
			return x;
		}
		return s[x] = findByPathCompression(s[x]);
	}

	public int find(int x) {
		if (s[x] < 0) {
			return x;
		}
		return find(s[x]);
	}

	/**
	 * 简单并操作
	 * @param x 合并之后的根
	 * @param y
	 */
	public void union(int x, int y) {
		s[y] = x;
	}

	/**
	 * 根据大小合并，大的合并小的
	 * @param x
	 * @param y
	 */
	public void unionBySize(int x, int y) {
		if (s[x] - s[y] < 0) {
			// x所在的树数目最大
			s[y] = x;
			s[x]--;// 数目增加（这里用负数表示），
		} else {
			s[x] = y;
			s[y]--;
		}
	}

	/**
	 * 根据深度合并，深度大的合并深度小的
	 * 两个深度一样的树，才会增加树的深度
	 * @param x
	 * @param y
	 */
	public void unionByHeight(int x, int y) {
		if (s[x] - s[y] < 0) {
			// x所在树的深度大，x作为
			s[y] = x;
		} else {
			if (s[x] == s[y]) {
				s[x]--;
			}
			s[x] = y;
		}
	}
}
