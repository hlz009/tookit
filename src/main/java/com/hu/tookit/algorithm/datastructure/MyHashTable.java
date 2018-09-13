package com.hu.tookit.algorithm.datastructure;

public interface MyHashTable<T> {

	int size();

	boolean isEmpty();

	void clear();

	boolean contains(T t);

	void insert(T t);

	void remove(T t);
}
