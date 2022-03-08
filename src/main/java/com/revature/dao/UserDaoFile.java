package com.revature.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;

import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Array;
import java.io.FileOutputStream;


import com.revature.beans.Account;
import com.revature.beans.User;
import com.revature.beans.User.UserType;

/**
 * Implementation of UserDAO that reads and writes to a file
 */
public class UserDaoFile implements UserDao {
	
	public static String fileLocation = "src\\users.txt";
	private static File myFile = new File(fileLocation);
	public static List<User> usersList = new ArrayList<User>();
	public void write()
	{
		try {
			File myfile = new File(fileLocation);
			FileOutputStream fos = new FileOutputStream(myfile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(usersList);
			oos.close();
			fos.close();
		} catch (IOException e) {
			System.out.println("Error Opening File");
			e.printStackTrace();
		}
	}
	public List<User> read()
	{
		myFile = new File(fileLocation);

		try {
			FileInputStream fis = new FileInputStream(myFile);
			ObjectInputStream ois = new ObjectInputStream(fis);
			//System.out.println((User) ois.readObject());
			Object o = ois.readObject();
			List<User> readUsers = (ArrayList<User>)o;
			ois.close();
			fis.close();
			return readUsers;
		} catch (ClassNotFoundException e) {
				System.out.println("CLASS NOT FOUND EXCEPTION");
				e.printStackTrace();
		}catch(EOFException e)
		{
			return null;
		}catch (IOException e) {
			
		}
		return null;
	}
	public User addUser(User user) {
		if(!Files.exists(Paths.get(fileLocation)))
			myFile = new File(fileLocation);
		usersList = read();
		boolean alreadyAdded = false;
		if(usersList == null)
		{
			System.out.println("Here");
			usersList = new ArrayList<User>();
			usersList.add(user);
			write();
			System.out.println("User has been added to the file " + user);
			return user;
		}
		for (User currentUser : usersList) {
			if(currentUser.getId().equals(user.getId()))
			{
				System.out.println("alreadyadded");
				alreadyAdded = true;
			}
		}
		if(alreadyAdded == true)
		{
			return null;
		}
		else
		{
			usersList.add(user);
			write();
			System.out.println("User has been added to the file " + user);
			return user;
		}
	}

	public User getUser(Integer userId) {
		System.out.println("IHJOAFGHPIOWQEHPGTOIWHPEOIGHPOIHIOGH");
		usersList = getAllUsers();
		
		for (User user : usersList) {
			System.out.println("Get User" + user);
			System.out.println(user.getId());
			System.out.println(userId);
			System.out.println(user.getId().equals(userId));
			if(user.getId().equals(userId))
				{
					return user;
				}	
		}
		// TODO Auto-generated method stub
		return null;
	}

	public User getUser(String username, String pass) {
		for (User user : usersList) {
			if(user.getUsername().equalsIgnoreCase(username) && user.getPassword() == pass)
				return user;
		}// TODO Auto-generated method stub
		return null;
	}

	public List<User> getAllUsers() {
		usersList = read();
		return usersList;
	}
		
	public User updateUser(User u) {
		int i = 0;
		for (User user : usersList) {
			if(user.getId().equals(u.getId()))
				{
					usersList.set(i, u);
					write();
					return u;
				}
			i++;
		}
		return null;
		// TODO Auto-generated method stub
	}

	public boolean removeUser(User u) {
		for (User user : usersList) {
			if(user.getId().equals(u.getId()))
			{
				usersList.remove(user);
				write();
				return true;
			}
		}
		// TODO Auto-generated method stub
		return false;
	}

}
