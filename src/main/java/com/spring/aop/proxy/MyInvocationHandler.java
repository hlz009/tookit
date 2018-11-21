package com.spring.aop.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MyInvocationHandler implements InvocationHandler {

	private Object target; // 目标对象

	MyInvocationHandler(Object target){
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) 
			throws Throwable {
		if (method.getName().startsWith("test")) {
			System.out.println("通过代理进行调用" + method.getName());
		}
		return method.invoke(target, args);
	}

	public static void main(String[] args) {
		MyInvocationHandler handler = new MyInvocationHandler(new DemoServiceImpl());
		DemoService proxy = (DemoService) Proxy.newProxyInstance(MyInvocationHandler.class.getClassLoader(), 
				new Class[] {DemoService.class}, handler);
	
		proxy.test();
		proxy.test1();
//		proxy.abc();
	}
}
