package com.spring.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.spring.domain.User;
import com.spring.domain.UserDao;
import com.spring.util.Level;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
public class UserDaoTest {

	
	private User user1,user2,user3;

	@Autowired
	private UserDao dao;
	
	public static void main(String[] args) {
		JUnitCore.main("com.spring.test.UserDaoTest");
	}

	
	@Before
	public void setUp()
	{
		user1 = new User("1", "1", "1",Level.BASIC,1,0);
		user2 = new User("2", "2", "2",Level.SILVER,1,0);
		user3 = new User("3", "3", "3",Level.GOLD,1,0);

	}

	@Test
	public void addAndGet() throws SQLException, ClassNotFoundException
	{
		dao.deleteAll();

		dao.add(user1);
		dao.add(user2);
		
		checkSameUser(user1, dao.get(user1.getId()));
		checkSameUser(user2, dao.get(user2.getId()));
		
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
		
		dao.add(user1);
		assertThat(dao.getCount(), is(1));
		
		dao.add(user2);
		assertThat(dao.getCount(), is(2));
		
		dao.add(user3);
		assertThat(dao.getCount(), is(3));
	}

	@Test
	public void getAll() throws ClassNotFoundException,SQLException
	{
		dao.deleteAll();

		dao.add(user1);
		dao.add(user2);
		dao.add(user3);


		List<User> list = dao.getAll();
		assertThat(list.size(), is(3));
		
		dao.deleteAll();
		
		 list = dao.getAll();
		assertThat(list.size(), is(0));
		
	}
	
	@Test(expected=DuplicateKeyException.class)
	public void duplicateKey()
	{
		dao.deleteAll();
		dao.add(user1);
		dao.add(user1);
	}
	
	@Test
	public void update()
	{
		dao.deleteAll();
		dao.add(user1);
		
		user1.setName("jisu");
		
		dao.update(user1);
		
		checkSameUser(user1, dao.get(user1.getId()));
		
	}

	public void checkSameUser(User user1,User user2)
	{
		assertThat(user1.getId(), is(user2.getId()));
		assertThat(user1.getName(), is(user2.getName()));
		assertThat(user1.getPassword(), is(user2.getPassword()));
		assertThat(user1.getLevel(), is(user2.getLevel()));
		assertThat(user1.getLogin(), is(user2.getLogin()));
		assertThat(user1.getRecommend(), is(user2.getRecommend()));
	}

}
