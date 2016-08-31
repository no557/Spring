package com.spring.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.spring.domain.User;
import com.spring.domain.UserDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
@DirtiesContext
public class UserDaoTest {

	@Autowired
	private ApplicationContext context;
	@Autowired
	private UserDao dao;
	
	public static void main(String[] args) {
		JUnitCore.main("com.spring.test.UserDaoTest");
	}

	
	@Before
	public void setUp()
	{
		DataSource dataSource = new SingleConnectionDataSource("jdbc:mysql//localhost/testdb","root","root",true);
		
		dao.setDataSource(dataSource);
		
		System.out.println(this.context);
		System.out.println(this);
	}

	@Test
	public void addAndGet() throws SQLException, ClassNotFoundException
	{
		
		dao.deleteAll();
		

		User user = new User("root","shyun","root");
		User user2 = new User("root2","shyun","root2");

		dao.add(user);
		dao.add(user2);
		
		assertThat(dao.getCount(),is(2));
		
		User userGet1 = dao.get("root");
		assertThat(userGet1.getName(), is(user.getName()));
		assertThat(userGet1.getPassword(), is(user.getPassword()));
		
		User userGet2 = dao.get("root");
		assertThat(userGet2.getName(), is(user.getName()));
		assertThat(userGet2.getPassword(), is(user.getPassword()));
		
	}
	
	@Test(expected=EmptyResultDataAccessException.class)
	public void getUserFailure() throws SQLException, ClassNotFoundException
	{
		dao.deleteAll();
		dao.get("unknown_id");
	}

	@Test
	public void count() throws SQLException, ClassNotFoundException
	{
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.add(new User("1", "1", "1"));
		assertThat(dao.getCount(), is(1));
		
		dao.add(new User("2", "2", "2"));
		assertThat(dao.getCount(), is(2));
		
		dao.add(new User("3", "3", "3"));
		assertThat(dao.getCount(), is(3));
	}


}
