package com.revature.services;

import java.util.ArrayList;
import java.util.List;

import com.revature.beans.Account;
import com.revature.beans.User;
import com.revature.beans.Account.AccountType;
import com.revature.beans.User.UserType;
import com.revature.dao.AccountDao;
import com.revature.exceptions.OverdraftException;
import com.revature.exceptions.UnauthorizedException;
import com.revature.utils.SessionCache;

/**
 * This class should contain the business logic for performing operations on Accounts
 */
public class AccountService {
	
	public AccountDao actDao;
	public static final double STARTING_BALANCE = 25d;
	
	public AccountService(AccountDao dao) {
		this.actDao = dao;
	}
	
	/**
	 * Withdraws funds from the specified account
	 * @throws OverdraftException if amount is greater than the account balance
	 * @throws UnsupportedOperationException if amount is negative
	 */
	public void withdraw(Account a, Double amount) throws OverdraftException, UnsupportedOperationException, UnauthorizedException {
		if(a.getBalance() < amount)
			throw new OverdraftException();
		else if(a.isApproved() == false)
			throw new UnauthorizedException();
		else if(amount < 0)
			throw new UnsupportedOperationException();
		else {
			a.setBalance(a.getBalance()-amount);
			actDao.updateAccount(a);
		}
	}
	
	/**
	 * Deposit funds to an account
	 * @throws UnsupportedOperationException if amount is negative
	 */
	public void deposit(Account a, Double amount) throws UnsupportedOperationException, UnauthorizedException{
		if(a.isApproved() == false)
			throw new UnauthorizedException();
		if(amount < 0){
			throw new UnsupportedOperationException();
		}
		else {
			a.setBalance(a.getBalance() + amount);
			actDao.updateAccount(a);
		}
	}
	
	/**
	 * Transfers funds between accounts
	 * @throws UnsupportedOperationException if amount is negative or 
	 * the transaction would result in a negative balance for either account
	 * or if either account is not approved
	 * @param fromAct the account to withdraw from
	 * @param toAct the account to deposit to
	 * @param amount the monetary value to transfer
	 */
	public void transfer(Account fromAct, Account toAct, double amount) throws OverdraftException, UnsupportedOperationException{
		withdraw(fromAct, amount);
		deposit(toAct, amount);
	}
	
	/**
	 * Creates a new account for a given User
	 * @return the Account object that was created
	 */
	public Account createNewAccount(User u) {
		Account newAccount = new Account();
		newAccount.setId(actDao.getAccounts().size() + 1);
		newAccount.setOwnerId(u.getId());
		newAccount.setBalance(STARTING_BALANCE);
		newAccount.setType(AccountType.SAVINGS);
		newAccount.setApproved(true);
		List<Account> userAccounts = u.getAccounts();
		if(userAccounts == null)
		{
			userAccounts = new ArrayList<Account>();
		}
		//userAccounts.add(newAccount);
		//u.setAccounts(userAccounts);
		System.out.println(newAccount);
		actDao.addAccount(newAccount);
		return newAccount;
	}
	
	/**
	 * Approve or reject an account.
	 * @param a
	 * @param approval
	 * @throws UnauthorizedException if logged in user is not an Employee
	 * @return true if account is approved, or false if unapproved
	 */
	public boolean approveOrRejectAccount(Account a, boolean approval) throws UnauthorizedException {
		User currentUser = SessionCache.getCurrentUser().get();
		if(currentUser.getUserType() == UserType.EMPLOYEE){			
			a.setApproved(approval);
			return true;
		}
		else
			throw new UnauthorizedException();
	}
}
