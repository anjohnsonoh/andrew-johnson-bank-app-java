package com.revature.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.revature.beans.Account;
import com.revature.beans.User;

/**
 * Implementation of AccountDAO which reads/writes to files
 */
public class AccountDaoFile implements AccountDao {
	// use this file location to persist the data to
	public static String fileLocation = "";
	private static File myFile = new File(fileLocation);
	private static List<Account> accountsList = new ArrayList<Account>();
	public void write()
	{
		try {
			File myfile = new File(fileLocation);
			FileOutputStream fos = new FileOutputStream(myfile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(accountsList);
			oos.close();
			fos.close();
		} catch (IOException e) {
			System.out.println("Error Opening File");
			e.printStackTrace();
		}
	}
	public List<Account> read() 
	{
		try {
			FileInputStream fis = new FileInputStream(myFile);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object o = ois.readObject();
			List<Account> readAccounts = (ArrayList<Account>)o;
			ois.close();
			fis.close();
			return readAccounts;
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
	public Account addAccount(Account a) {
		myFile = new File(fileLocation);
		
		accountsList = read();
		boolean alreadyAdded = false;
		if(accountsList == null)
		{
			accountsList = new ArrayList<Account>();
			accountsList.add(a);
			write();
			return a;
		}
		for (Account currentAccount: accountsList) {
			if(currentAccount.getId().equals(a.getId()))
			{
				alreadyAdded = true;
			}
		}
		if(alreadyAdded == true)
		{
			return null;
		}
		else
		{
			accountsList.add(a);
			write();
			return a;
		}
	}

	public Account getAccount(Integer actId) {
		accountsList = getAccounts();
		
		for (Account account : accountsList) {
			if(account.getId().equals(actId))
				{
					return account;
				}	
		}
		// TODO Auto-generated method stub
		return null;
	}

	public List<Account> getAccounts() {
		accountsList = read();
		return accountsList;
	}

	public List<Account> getAccountsByUser(User u) {
		List<Account> userAccounts = new ArrayList<Account>();
		for (Account account : getAccounts()) {
			if(account.getOwnerId().equals(u.getId()))
				userAccounts.add(account);
		}
		if(userAccounts.size() > 0)
			return userAccounts;
		else
			return null;
	}

	public Account updateAccount(Account a) {
		int i = 0;
		for (Account account : accountsList) {
			if(account.getId().equals(a.getId()))
				{
					accountsList.set(i, a);
					write();
					return a;
					
				}
			i++;
		}
		return null;
		// TODO Auto-generated method stub
	}

	public boolean removeAccount(Account a) {
		for (Account account : accountsList) {
			if(account.getId().equals(a.getId()))
			{
				accountsList.remove(account);
				write();
				return true;
			}
		}
		// TODO Auto-generated method stub
		return false;
	}

}
