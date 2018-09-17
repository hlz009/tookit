package com.hu.tookit.algorithm;

import com.hu.tookit.algorithm.datastructure.CukooHashTable;

/**
 * 猜字谜
 * @author xiaozhi009
 *
 */
public class PuzzleWords {

	private CukooHashTable<String> cukooHash = new CukooHashTable<String>();

	public static void main(String[] args) {
		char[][] ccs = {{'t','h','i','s'}, {'w','a','t','s'},
				{'o','a','h','g'}, {'f','g','d','t'}};
		PuzzleWords puzzleWord = new PuzzleWords();
		puzzleWord.generateWords(ccs);
		System.out.println(puzzleWord.findWord("sh"));
	}


	public boolean findWord(String word) {
		return cukooHash.contains(word);
	}

	public void generateWords(char[][] ccs) {
		for(int i = 0, len = ccs.length; i < len; i++) {
			char[] cs = ccs[i];
			for(int j = 0, slen = cs.length; j < slen; j++) {
				generateWordHash(i, j, 4, ccs);
			}
		}
	}

	private void generateWordHash(int row, int column, 
			 int wordLength, char[][] ccs) {
		// 8个方向
		int rowMax = ccs.length - 1, cloumnMax = ccs[row].length - 1;
		generateHorizontalRightDirection(row, column, cloumnMax, wordLength, ccs);
		generateHorizontalLeftDirection(row, column, 0, wordLength, ccs);
		generateVerticallyDownDirection(row, column, rowMax, wordLength, ccs);
		generateVerticallyUpDirection(row, column, 0, wordLength, ccs);
		generateAngleBbisectorRightAndDownDirection(row, column, rowMax, cloumnMax, wordLength, ccs);
		generateAngleBbisectorLeftAndDownDirection(row, column, rowMax, 0, wordLength, ccs);
		generateAngleBbisectorRightAndUpDirection(rowMax, column, 0, cloumnMax, wordLength, ccs);
		generateAngleBbisectorLeftAndUpDirection(rowMax, column, 0, 0, wordLength, ccs);
	}

	/**
	 * 水平向右
	 * @param row
	 * @param column
	 * @param rowMax
	 * @param cloumnMax
	 * @param wordLength
	 * @param ccs
	 */
	private void generateHorizontalRightDirection(int row, int column, 
			int end, int wordLength, char[][] ccs) {
		int index = 0;
		while (index < wordLength) {
			String resultStr = "";
			int len = Math.min(end, index + column);
			for (int i = column; i <= len; i++) {
				resultStr += ccs[row][i];
			}
			cukooHash.insert(resultStr);
			index++;
		}
	}

	/**
	 * 水平向左
	 * @param row
	 * @param column
	 * @param rowMax
	 * @param cloumnMax
	 * @param wordLength
	 * @param ccs
	 */
	private void generateHorizontalLeftDirection(int row, int column, 
			int end, int wordLength, char[][] ccs) {
		int index = 0;
		while (index < wordLength) {
			String resultStr = "";
			int len = Math.max(end, column - index);
			for (int i = column; i > len; i--) {
				resultStr += ccs[row][i];
			}
			cukooHash.insert(resultStr);
			index++;
		}
	}

	/**
	 * 竖直向下
	 * @param row
	 * @param column
	 * @param rowMax
	 * @param cloumnMax
	 * @param wordLength
	 * @param ccs
	 */
	private void generateVerticallyDownDirection(int row, int column, 
			int end, int wordLength, char[][] ccs) {
		int index = 0;
		while (index < wordLength) {
			String resultStr = "";
			int len = Math.min(end, index + row);
			for (int i = row; i < len; i++) {
				resultStr += ccs[i][column];
			}
			cukooHash.insert(resultStr);
			index++;
		}
	}

	/**
	 * 竖直向上
	 * @param row
	 * @param column
	 * @param rowMax
	 * @param cloumnMax
	 * @param wordLength
	 * @param ccs
	 */
	private void generateVerticallyUpDirection(int row, int column, 
			int end, int wordLength, char[][] ccs) {
		int index = 0;
		while (index < wordLength) {
			String resultStr = "";
			int len = Math.max(end, column - index);
			for (int i = column; i >= len; i--) {
				resultStr += ccs[i][column];
			}
			cukooHash.insert(resultStr);
			index++;
		}
	}

	/**
	 * 右斜向下
	 * @param row
	 * @param column
	 * @param rowEnd
	 * @param columnEnd
	 * @param wordLength
	 * @param ccs
	 */
	private void generateAngleBbisectorRightAndDownDirection(int row, int column, 
			int rowEnd, int columnEnd, int wordLength, char[][] ccs) {
		int index = 0;
		while (index < wordLength) {
			String resultStr = "";
			int rowLen = Math.min(rowEnd, index + row);
			int columnLen = Math.min(columnEnd, index + column);
			for (int i = row, j = column; i <= rowLen && j <= columnLen; i++, j++) {
				resultStr += ccs[i][j];
			}
			cukooHash.insert(resultStr);
			index++;
		}
	}

	/**
	 * 左斜向下
	 * @param row
	 * @param column
	 * @param rowEnd
	 * @param columnEnd
	 * @param wordLength
	 * @param ccs
	 */
	private void generateAngleBbisectorLeftAndDownDirection(int row, int column, 
			int rowEnd, int columnEnd, int wordLength, char[][] ccs) {
		int index = 0;
		while (index < wordLength) {
			String resultStr = "";
			int rowLen = Math.min(rowEnd, index + row);
			int columnLen = Math.max(columnEnd, column - index);
			for (int i = row, j = column; i <= rowLen && j >= columnLen; i++, j--) {
				resultStr += ccs[i][j];
			}
			cukooHash.insert(resultStr);
			index++;
		}
	}

	/**
	 * 右斜向上
	 * @param row
	 * @param column
	 * @param rowEnd
	 * @param columnEnd
	 * @param wordLength
	 * @param ccs
	 */
	private void generateAngleBbisectorRightAndUpDirection(int row, int column, 
			int rowEnd, int columnEnd, int wordLength, char[][] ccs) {
		int index = 0;
		while (index < wordLength) {
			String resultStr = "";
			int rowLen = Math.max(rowEnd, row - index);
			int columnLen = Math.min(columnEnd, index + column);
			for (int i = row, j = column; i >= rowLen && j <= columnLen; i--, j++) {
				resultStr += ccs[i][j];
			}
			cukooHash.insert(resultStr);
			index++;
		}
	}

	/**
	 * 左斜向上
	 * @param row
	 * @param column
	 * @param rowEnd
	 * @param columnEnd
	 * @param wordLength
	 * @param ccs
	 */
	private void generateAngleBbisectorLeftAndUpDirection(int row, int column, 
			int rowEnd, int columnEnd, int wordLength, char[][] ccs) {
		int index = 0;
		while (index < wordLength) {
			String resultStr = "";
			int rowLen = Math.max(rowEnd, row - index);
			int columnLen = Math.max(columnEnd, column - index);
			for (int i = row, j = column; i >= rowLen && j >= columnLen; i--, j--) {
				resultStr += ccs[i][j];
			}
			cukooHash.insert(resultStr);
			index++;
		}
	}
}
