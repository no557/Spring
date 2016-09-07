package com.spring.test;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.JUnitCore;

import com.spring.proxy.Hello;
import com.spring.proxy.HelloTarget;
import com.spring.proxy.HelloUppercase;

public class ProxyTest {
	
	public static void main(String[] args) {
		JUnitCore.main("com.spring.test.ProxyTest");
	}
	@Test
	public void simpleProxy()
	{
		Hello hello = new HelloTarget();
		Hello helloUpper = new HelloUppercase(hello);
		
		assertThat(hello.sayHello("jisu"),is("Hello jisu"));
		assertThat(hello.sayHi("jisu"),is("Hi jisu"));
		assertThat(hello.sayThankYou("jisu"),is("Thank You jisu"));
		
		
		assertThat(helloUpper.sayHello("jisu"),is("HELLO JISU"));
		assertThat(helloUpper.sayHi("jisu"),is("HI JISU"));
		assertThat(helloUpper.sayThankYou("jisu"),is("THANK YOU JISU"));
	}

}
