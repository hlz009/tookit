package com.hu.tookit.Math;

/**
 * 素数（质数）
 * @author xiaozhi009
 *
 */
public class Prime {
	private Prime() {		
	}

	public static int nextPrime(int num) {
		while (!isPrime(num)) {
			num++;
		}
		return num;
	}

	public static boolean isPrime(int num) {
		if (num <= 3) {
			return num > 1;
		}
        for (int i = 2; i <= Math.sqrt(num); i++) {
           if (num % i == 0)
               return false;
        }
	    return true;
	}

	public static void main(String[] args) {
		System.out.println(nextPrime(256));
	}
}
