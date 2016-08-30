package com.spring.factory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.spring.domain.UserDao;
import com.spring.util.ConnectionMaker;
import com.spring.util.CountingConnectionMaker;
import com.spring.util.SimpleConnectionMaker;


@Configuration
public class CountingDaoFactory {
	
	
//	@Bean
//	public UserDao userDao()
//	{
//		return new UserDao(connectionMaker());
//	}
//	
	@Bean
	public ConnectionMaker connectionMaker(){
		
		return new CountingConnectionMaker(realConnectionMaker());
		
	}
	
	@Bean
	public ConnectionMaker realConnectionMaker()
	{
		return new SimpleConnectionMaker();
	}

}
