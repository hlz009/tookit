package com.hu.tookit.algorithm.util;

import java.util.Random;

public class StringHashFactory implements HashFactory<String> {
	
	private static final int DEFAULT_SIZE = 7;

	private final int[] multipliers;
	private final Random r;

	public StringHashFactory() {
		this(DEFAULT_SIZE);
	}
	
	public StringHashFactory(int size) {
		multipliers = new int[size];
		r = new Random();
		generateNewFunctions();
	}

	@Override
	public int hash(String data, int which) {
		final int mutliplier = multipliers[which];
        int hashVal = 0;
        for (int i = 0; i < data.length(); i++)
            hashVal = mutliplier * hashVal + data.charAt(i);
        return hashVal;
	}

	@Override
	public int getNumOfFunctions() {
		return multipliers.length;
	}

	@Override
	public void generateNewFunctions() {
		for (int i = 0; i < multipliers.length; i++)
			multipliers[i] = r.nextInt();
	}

	@Override
	public int randomFunction() {
		return 0;
	}
}
