package com.spring.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import com.spring.domain.User;
import com.spring.domain.UserDao;
import com.spring.service.UserService;
import com.spring.util.Level;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
public class UserServiceTest {
	
	@Autowired
	UserService userService;
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	UserDao dao;
	
	@Autowired
	PlatformTransactionManager txManager;
	
	
	private User user1,user2,user3;

	@Before
	public void setUp()
	{
		user1 = new User("1", "1", "1",Level.BASIC,60,0,"gmail");
		user2 = new User("2", "2", "2",Level.SILVER,70,40,"hotmail");
		user3 = new User("3", "3", "3",Level.GOLD,100,20,"naver");

	}
	
	@Test
	public void bean()
	{
		assertThat(this.userService, is(notNullValue()));
	}
	
	@Test
	public void upgradeLevels() throws SQLException{
		
		dao.deleteAll();
		
		dao.add(user1);
		dao.add(user2);
		dao.add(user3);
		
		userService.upgradeLevels();
		checkLevel(user1, Level.SILVER);
		checkLevel(user2, Level.GOLD);
		checkLevel(user3, Level.GOLD);
	}
	
	@Test
	public void add()
	{
		dao.deleteAll();
		
		user2.setLevel(null);
		
		userService.add(user1);
		userService.add(user2);
		
		assertThat(dao.get(user1.getId()).getLevel(), is(user1.getLevel()));
		assertThat(dao.get(user2.getId()).getLevel(), is(Level.BASIC));
		
	}
	
	@Test
	public void upgradeAllOrNothing() throws SQLException{
		
		UserService testUserService = new TestUserService(user2.getId());
		testUserService.setUserDao(this.dao);
		testUserService.setDataSource(this.dataSource);
		testUserService.setTxManager(this.txManager);
		dao.deleteAll();
		
		dao.add(user1);
		dao.add(user2);
		dao.add(user3);
		
		try {
			testUserService.upgradeLevels();
			fail("TestUserServiceException expected");
			
		} catch (TestException e) {
			// TODO: handle exception
		}
		
		checkLevel(dao.get(user1.getId()), Level.BASIC);
		
	}
	
	private void checkLevel(User user, Level expectedLevel){

		User userUpdate = dao.get(user.getId());
		assertThat(userUpdate.getLevel(), is(expectedLevel));
		
	}
	
	static class TestUserService extends UserService{
		private String id;
		private TestUserService(String id){
			this.id = id;
		}
		
		protected void upgradeLevel(User user){
			
			System.out.println(user.getId() + "  " + this.id);
			if(user.getId().equals(this.id))throw new TestException();

			super.upgradeLevel(user);
		}
		
	}

	static class TestException extends RuntimeException{}

}
