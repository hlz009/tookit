package com.hu.tookit.algorithm.constant;

public enum HashEnum {
	NATIVE(0), ASCII(1), CHARACTERS(2),
	POLYNOMIAL(3);

	private final int index;
	HashEnum(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public int numOfHash() {
		return HashEnum.values().length;
	}

	public static HashEnum getHashEnum(int index) {
		HashEnum[] hashEnums = HashEnum.values();
		for (HashEnum hashEnum: hashEnums) {
			if (hashEnum.index == index) {
				return hashEnum;
			}
		}
		return null;
	}
}
