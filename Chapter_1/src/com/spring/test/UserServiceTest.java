package com.spring.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
	UserDao dao;
	
	
	private User user1,user2,user3;

	@Before
	public void setUp()
	{
		user1 = new User("1", "1", "1",Level.BASIC,60,0);
		user2 = new User("2", "2", "2",Level.SILVER,70,40);
		user3 = new User("3", "3", "3",Level.GOLD,100,20);

	}
	
	@Test
	public void bean()
	{
		assertThat(this.userService, is(notNullValue()));
	}
	
	@Test
	public void upgradeLevels(){
		
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
	
	private void checkLevel(User user, Level expectedLevel){

		User userUpdate = dao.get(user.getId());
		assertThat(userUpdate.getLevel(), is(expectedLevel));
		
	}

}
