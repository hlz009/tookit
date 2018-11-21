package com.spring.aop.proxy;

public class DemoServiceImpl implements DemoService {

	@Override
	public void test() {
		System.out.println("调用了test方法");
		test1();// 方法间的调用，而非代理
	}

	@Override
	public void test1() {
		System.out.println("调用了test1方法");
	}

	@Override
	public void abc() {
		System.out.println("调用了abc方法");
	}

}
