package com.spring.test;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.isNotNull;

import java.lang.reflect.Proxy;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;

import com.spring.proxy.Hello;
import com.spring.proxy.HelloTarget;
import com.spring.proxy.UppercaseHandler;

public class ProxyTest {
	
	public static void main(String[] args) {
		JUnitCore.main("com.spring.test.ProxyTest");
	}
	@Test
	public void simpleProxy()
	{
		Hello hello = new HelloTarget();
		
		assertThat(hello.sayHello("jisu"),is("Hello jisu"));
		assertThat(hello.sayHi("jisu"),is("Hi jisu"));
		assertThat(hello.sayThankYou("jisu"),is("Thank You jisu"));
	}
	
	@Test
	public void dynamicProxy()
	{
		Hello proxiedHello = (Hello)Proxy.newProxyInstance(getClass().getClassLoader(),
				new Class[] {Hello.class},new UppercaseHandler(new HelloTarget()));
		
		assertThat(proxiedHello.sayHello("jisu"),is("HELLO JISU"));
		assertThat(proxiedHello.sayHi("jisu"),is("HI JISU"));
		assertThat(proxiedHello.sayThankYou("jisu"),is("THANK YOU JISU"));
	}

}
