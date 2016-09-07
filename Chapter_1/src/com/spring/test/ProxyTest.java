package com.spring.test;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.isNotNull;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.aop.support.Pointcuts;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

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
	
	
	@Test
	public void proxyFactoryBean()
	{
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(new HelloTarget());
		pfBean.addAdvice(new UpperCaseAdivce());
		
		Hello proxiedHello = (Hello) pfBean.getObject();
		
		assertThat(proxiedHello.sayHello("jisu"),is("HELLO JISU"));
		assertThat(proxiedHello.sayHi("jisu"),is("HI JISU"));
		assertThat(proxiedHello.sayThankYou("jisu"),is("THANK YOU JISU"));
	}
	
	@Test
	public void pointCutAdvisor()
	{
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(new HelloTarget());
		
		NameMatchMethodPointcut pointCut = new NameMatchMethodPointcut();
		pointCut.setMappedName("sayH*");
		
		pfBean.addAdvisor(new DefaultPointcutAdvisor(pointCut, new UpperCaseAdivce()));
		
		Hello proxiedHello  =(Hello)pfBean.getObject();

		assertThat(proxiedHello.sayHello("jisu"),is("HELLO JISU"));
		assertThat(proxiedHello.sayHi("jisu"),is("HI JISU"));
		assertThat(proxiedHello.sayThankYou("jisu"),is("THANK YOU JISU"));
	}
	
	static class UpperCaseAdivce implements org.aopalliance.intercept.MethodInterceptor
	{

		@Override
		public Object invoke(MethodInvocation arg0) throws Throwable {
			// TODO Auto-generated method stub
			String ret = (String)arg0.proceed();
			return ret.toUpperCase();
		}
		
	}
	

}
