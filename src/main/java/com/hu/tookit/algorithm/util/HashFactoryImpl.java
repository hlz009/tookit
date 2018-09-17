package com.hu.tookit.algorithm.util;

import java.util.Random;

/**
 * 散列接口实现类
 * @author xiaozhi009
 *
 * @param <T>
 */
public class HashFactoryImpl<T> implements HashFactory<T> {
	
	public int hash(T data, int which) {
		return Hash.hash(data, which);
	}

	/**
	 * 得到散列表的总数
	 * @return
	 */
	public int getNumOfFunctions() {
		return Hash.getHashNum();
	}

	@Override
	public void generateNewFunctions() {
		Hash.generateFactor();
	}

	@Override
	public int randomFunction() {
		return new Random().nextInt(getNumOfFunctions());
	}
}
