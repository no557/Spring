package com.spring.domain;

import java.util.List;

public interface UserDao {
	
	void 		add(User user);
	User 		get(String user);
	List<User>  getAll();
	void 		deleteAll();
	int 		getCount();

}
