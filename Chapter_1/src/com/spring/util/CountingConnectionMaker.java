package com.spring.util;

import java.sql.Connection;
import java.sql.SQLException;

public class CountingConnectionMaker implements ConnectionMaker {

	int counter=0;
	private ConnectionMaker connectionMaker;

	public CountingConnectionMaker(ConnectionMaker realConnectionMaker) {
		
		this.connectionMaker = realConnectionMaker;
		
	}

	@Override
	public Connection makeNewConnection() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		
		this.counter++;

		return this.connectionMaker.makeNewConnection();
	}

	public int getCounter() {
		return counter;
	}
	
	

}
