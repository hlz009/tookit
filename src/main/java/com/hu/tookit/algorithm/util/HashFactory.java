package com.hu.tookit.algorithm.util;

/**
 * 散列接口
 * @author xiaozhi009
 *
 * @param <T>
 */
public interface HashFactory<T> {
	int hash(T data, int which);

	int getNumOfFunctions();

	void generateNewFunctions();
	
	int randomFunction();
}
