package com.hu.tookit.algorithm.constant;

/**
 * 平衡符号
 * @author xiaozhi009
 *
 */
public enum BalanceSymbol {
	LEFT_BIG_BRACKETS(1, '{', true), RIGHT_BIG_BRACKETS(1, '}', false),
	LEFT_SQUARE_BRACKETS(2, '[', true), RIGHT_SQUARE_BRACKETS(2, ']', false),
	LEFT_BRACKETS(3, '(', true), RIGHT_BRACKETS(3, ')', false);

	private final int symbol;
	private final char ch;
	private final boolean isOpenSymbol;
	
	private BalanceSymbol(int symbol, char ch, boolean isOpenSymbol){
		this.symbol = symbol;
		this.ch = ch;
		this.isOpenSymbol = isOpenSymbol;
	}

	public int getSymbol() {
		return symbol;
	}

	public char getCh() {
		return ch;
	}

	public boolean isOpenSymbol() {
		return isOpenSymbol;
	}

	public static boolean isValid(char ch) {
		return getSymol(ch) != -1;
	}

	public static int getSymol(char ch) {
		return getSymol(ch, true, null);
	}

	private static int getSymol(char ch, boolean isIngore, Boolean isOpenSymbol) {
		BalanceSymbol[] balanceSymbols = BalanceSymbol.values();
		for (BalanceSymbol balanceSymbol: balanceSymbols) {
			if (balanceSymbol.getCh() == ch) {
				if (isIngore) {
					return balanceSymbol.symbol;
				}
				if (isOpenSymbol == balanceSymbol.isOpenSymbol) {
					return balanceSymbol.symbol;
				}
			}
		}
		return -1;
	}

	public static int getOpenSymol(char ch) {
		return getSymol(ch, false, true);
	}

	public static int getClosedSymol(char ch) {
		return getSymol(ch, false, false);
	}
}