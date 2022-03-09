package com.revature.services;

import java.util.List;

import javax.management.InstanceAlreadyExistsException;

import com.revature.beans.User;
import com.revature.dao.AccountDao;
import com.revature.dao.UserDao;
import com.revature.exceptions.IndexAlreadyExistsException;
import com.revature.exceptions.InvalidCredentialsException;
import com.revature.exceptions.UsernameAlreadyExistsException;

/**
 * This class should contain the business logic for performing operations on users
 */
public class UserService {
	
	UserDao userDao;
	AccountDao accountDao;
	
	public UserService(UserDao udao, AccountDao adao) {
		this.userDao = udao;
		this.accountDao = adao;
	}
	
	/**
	 * Validates the username and password, and return the User object for that user
	 * @throws InvalidCredentialsException if either username is not found or password does not match
	 * @return the User who is now logged in
	 */
	public User login(String username, String password) throws InvalidCredentialsException{
		User currentUser = new User();
		currentUser = userDao.getUser(username, password);
		if (currentUser == null) {
			throw new InvalidCredentialsException();
		}
		else
			return currentUser;
	}
	
	/**
	 * Creates the specified User in the persistence layer
	 * @param newUser the User to register
	 * @throws UsernameAlreadyExistsException if the given User's username is taken
	 */
	public void register(User newUser) throws UsernameAlreadyExistsException, IndexAlreadyExistsException{
		List<User> myUsers = userDao.getAllUsers();
		for (User user : myUsers) {
			if(user.getUsername().equals(newUser.getUsername()))
				throw new UsernameAlreadyExistsException();
			else if(user.getId().equals(newUser.getId()))
				throw new IndexAlreadyExistsException();
		}
		if (userDao.getUser(newUser.getUsername(), newUser.getPassword()) == null) {
			userDao.addUser(newUser);
		}
		else
			throw new UsernameAlreadyExistsException();
	}
}
