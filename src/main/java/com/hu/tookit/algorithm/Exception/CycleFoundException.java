package com.hu.tookit.algorithm.Exception;

public class CycleFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

    public CycleFoundException() {
        super("该图有圈");
    }

    public CycleFoundException(String msg) {
        super(msg);
    }
}
