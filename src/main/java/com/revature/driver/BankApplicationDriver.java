package com.revature.driver;

import com.revature.beans.*;
import com.revature.beans.Account.AccountType;
import com.revature.beans.User.UserType;
import com.revature.dao.*;
import com.revature.exceptions.IndexAlreadyExistsException;
import com.revature.exceptions.InvalidCredentialsException;
import com.revature.exceptions.OverdraftException;
import com.revature.exceptions.UnauthorizedException;
import com.revature.exceptions.UsernameAlreadyExistsException;
import com.revature.services.*;
import com.revature.utils.*;

import java.awt.datatransfer.Transferable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import javax.imageio.spi.RegisterableService;
/**
 * This is the entry point to the application
 */
public class BankApplicationDriver {

	private static Connection conn;
	private static UserDao userDb =  new UserDaoDB();
	private static AccountDao accountDb = new AccountDaoDB();
	private static UserService uSrv = new UserService(userDb, accountDb);
	private static AccountService aSrv = new AccountService(accountDb);
	private static User currentUser = null;
	
	//MAIN METHOD
	public static void main(String[] args) {
		BankApplicationDriver myDriver = new BankApplicationDriver();
		boolean applicationRunning = true;
		Scanner myScanner = new Scanner(System.in);
		int myKey = 0;
		conn = ConnectionUtil.getConnection();
		
		do {
			//NO USER LOGGED IN 
			if(currentUser == null)
			{
				boolean properInput = false;
				myDriver.outputIntro();

				while (!properInput) {			//Gets the user's choice for application
					try {
						System.out.println("Enter your choice");
						myKey = myScanner.nextInt();
						properInput =true;
					} catch (InputMismatchException e) {
						System.out.println("Invalid Input type");
					}finally {
						myScanner.nextLine();
					}
				}
				switch (myKey) {
					case 1:
						myDriver.register(myScanner);
						break;
					case 2:
						myDriver.login(myScanner);
						break;
					case 3:
						applicationRunning = false;
						break;
					default:
						break;
				}
			}
			
			//USER IS OF TYPE CUSTOMER
			else if(currentUser.getUserType() == UserType.CUSTOMER)
			{
				boolean properInput = false;
				myDriver.customerOutput();

				while (!properInput) {			//Gets the user's choice for application
					try {
						System.out.println("Enter your choice");
						myKey = myScanner.nextInt();
						properInput =true;
					} catch (InputMismatchException e) {
						System.out.println("Invalid Input type");
					}finally {
						myScanner.nextLine();
					}
				}
				switch (myKey) {
					case 1:
						aSrv.createNewAccount(currentUser);
						break;
					case 2:
						myDriver.viewBalance(myScanner);
						break;
					case 3:
						myDriver.deposit(myScanner);
						break;
					case 4:
						myDriver.withdraw(myScanner);
						break;
					case 5:
						myDriver.transfer(myScanner);
						break;
					case 6:
						currentUser = null;
						break;
					case 7:
						applicationRunning = false;
						break;
					default:
						System.out.println("NO OPTION SELECTED");
						break;
				}
			}
			//USER IS OF TYPE EMPLOYEE
			else if(currentUser.getUserType() == UserType.EMPLOYEE)
			{
				boolean properInput = false;
				myDriver.employeeOutput();

				while (!properInput) {			//Gets the user's choice for application
					try {
						System.out.println("Enter your choice");
						myKey = myScanner.nextInt();
						properInput =true;
					} catch (InputMismatchException e) {
						System.out.println("Invalid Input type");
					}finally {
						myScanner.nextLine();
					}
				}
				switch (myKey) {
				case 1:
					myDriver.toggleApproved(myScanner);
					break;
				case 2:
					currentUser = null;
					break;
				case 3:
					applicationRunning = false;
					break;
				default:
					System.out.println("NO OPTION SELECTED");
					break;
				}
			}
		}while(applicationRunning);
		System.out.println("EXITING APPLICATION");
	}
	
	/**********************************************************************
	 * Method: OututIntro
	 * Purpose: Output an intro to the user about the initial options of
	 * the application
	 * 
	 *********************************************************************/
	public void outputIntro() {
		System.out.println("*********************************************");
		System.out.println("Hello and welcome to the banking application");
		System.out.println("Please enter a choice from the list below");
		System.out.println("                                      ");
		System.out.println("1. Register an account");
		System.out.println("2. Login with your username and password");
		System.out.println("3. Exit the application");
		System.out.println("****************************************");
	}
	/**********************************************************************
	 * Method: CustomerOutput()
	 * Purpose: Outputs info for a user of type customer about the different
	 * options that they have
	 * 
	 *********************************************************************/
	public void customerOutput() {
		System.out.println("*********************************************");
		System.out.println("Hello and welcome to the banking application");
		System.out.println("Please enter a choice from the list below");
		System.out.println("                                      ");
		System.out.println("1. Create a new account");
		System.out.println("2. See balance of a specific account");
		System.out.println("3. Deposit money into an account");
		System.out.println("4. Withdraw money from an account");
		System.out.println("5. Transfer money between accounts");
		System.out.println("6. Logout of User");
		System.out.println("7. Exit the application");
		System.out.println("****************************************");
	}

	/**********************************************************************
	 * Method: EmployeeOutput
	 * Purpose: Output an intro to the user about the initial options of
	 * the application
	 * 
	 *********************************************************************/
	public void employeeOutput() {
		System.out.println("*********************************************");
		System.out.println("Hello and welcome to the banking application");
		System.out.println("Please enter a choice from the list below");
		System.out.println("                                      ");
		System.out.println("1. Toggle Approval for an Account");
		System.out.println("2. Logout of User");
		System.out.println("3. Exit the application");
		System.out.println("****************************************");
	}
	
	/**********************************************************************
	 * Method: Register
	 * Purpose: Registers a user with username and password to be stored
	 * on the database for retrieval
	 * 
	 *********************************************************************/
	public void register(Scanner myScanner)
	{
		int inId = userDb.getAllUsers().size()+1;
		boolean properInput=false;
		/*while(properInput == false)
		try {
			System.out.println("Enter a id");
			inId = myScanner.nextInt();
			properInput = true;
		} catch (InputMismatchException e) {
			System.out.println("Invalid input");
			
			// TODO: handle exception
		}
		finally {
			myScanner.nextLine();
		}*/
		System.out.println("Enter a Username");
		String inUsername = myScanner.nextLine();
		System.out.println("Enter a First Name");
		String inFirst = myScanner.nextLine();
		System.out.println("Enter a Last Name");
		String inLast = myScanner.nextLine();
		System.out.println("Enter a Pasword");
		String inPass = myScanner.nextLine();
		User newUser = new User(inId, inUsername, inPass, inFirst, inLast, UserType.CUSTOMER, null);
		try {
			uSrv.register(newUser);
		} catch (UsernameAlreadyExistsException e) {
			System.out.println("UserName already exists");
			System.out.println("UNABLE TO CREATE USER PLEASE TRY AGAIN WITH A NEW USERNAME");
			// TODO: handle exception
		} catch (IndexAlreadyExistsException e) {
			System.out.println("User at that index already exists");
			System.out.println("UNABLE TO CREATE USER PLEASE TRY AGAIN AT A NEW INDEX");
			// TODO: handle exception
		}
	}
	/**********************************************************************
	 * Method: Login
	 * Purpose: Logs a user into their account by having them enter a 
	 * username and password
	 * 
	 *********************************************************************/
	public void login(Scanner myScanner)
	{
		System.out.println("Enter your Username");
		String inUsername = myScanner.nextLine();
		System.out.println("Enter your password");
		String inPass = myScanner.nextLine();
		try {
			currentUser = uSrv.login(inUsername, inPass);
			System.out.println("Login Successful! Welcome " + currentUser.getFirstName() + "!");
		} catch (InvalidCredentialsException e) {
			System.out.println("FAILED TO LOGIN, INVALID CREDENTIALS");
			System.out.println("PLEASE CHECK YOUR PASSWORD AND USERNAME AND TRY AGAIN");
			// TODO: handle exception
		}
	}
	
	/**********************************************************************
	 * Method: viewBalance
	 * Purpose: Asks the User which account they want to see their balance
	 * for and returns that value from the database
	 * 
	 *********************************************************************/
	public void viewBalance(Scanner myScanner)
	{
		boolean properInput = false;
		System.out.println("You have " + accountDb.getAccountsByUser(currentUser).size() + " accounts. Which one would you like to see?");
		while(!properInput && accountDb.getAccountsByUser(currentUser).size() >0)
			try {
				int accountToGet= myScanner.nextInt();
				List<Account> userAccounts = accountDb.getAccountsByUser(currentUser);
				if(accountToGet > 0 && accountToGet <= userAccounts.size())
				{
					properInput = true;
					System.out.println("That account has $" + userAccounts.get(accountToGet-1).getBalance() + " in it.");
				}
				else
					System.out.println("Invalid account specified you only have " + userAccounts.size() + " accounts");
			} catch (InputMismatchException e) {
				System.out.println("Invalid Input type");
			}finally {
				myScanner.nextLine();
			}
	}
	/**********************************************************************
	 * Method: deposit
	 * Purpose: Deposits money into a user's account
	 * 
	 *********************************************************************/
	public void deposit(Scanner myScanner)
	{
		boolean properInput = false;
		
		while(!properInput&& accountDb.getAccountsByUser(currentUser).size() >0)
			try {
				System.out.println("You have " + accountDb.getAccountsByUser(currentUser).size() + " accounts. Which one would you like to deposit into?");
				int accountToGet= myScanner.nextInt();
				List<Account> userAccounts = accountDb.getAccountsByUser(currentUser);
				if(accountToGet > 0 && accountToGet <= userAccounts.size())
				{
					System.out.println("Your deposit account has $" + userAccounts.get(accountToGet-1).getBalance());
					System.out.println("How much would you like to deposit?");
					double amount = myScanner.nextDouble();
					aSrv.deposit(userAccounts.get(accountToGet-1), amount);
					properInput = true;
					System.out.println("Your deposit account has $" + userAccounts.get(accountToGet-1).getBalance());
				}
				else
					System.out.println("Invalid account specified you only have " + userAccounts.size() + " accounts");
			} catch (InputMismatchException e) {
				System.out.println("Invalid Input type");
			}catch(UnsupportedOperationException e)
			{
				System.out.println("Negative number input. Please use a positive number");
			}catch(UnauthorizedException e){
				System.out.println("You do not have permission to carry out the transaction");
				System.out.println("please contact customer support");
				properInput=true;
			}
			finally {
				myScanner.nextLine();
			}
	}
	/**********************************************************************
	 * Method: withdraw
	 * Purpose: withdraws money from a user's account
	 * 
	 *********************************************************************/
	public void withdraw(Scanner myScanner) {
		boolean properInput = false;
	
		while(!properInput&& accountDb.getAccountsByUser(currentUser).size() >0) {
			try {
				System.out.println("You have " + accountDb.getAccountsByUser(currentUser).size() + " accounts. Which one would you like to withdraw from?");
				int accountToGet= myScanner.nextInt();
				List<Account> userAccounts = accountDb.getAccountsByUser(currentUser);
				if(accountToGet > 0 && accountToGet <= userAccounts.size())
				{
					System.out.println("Your withdrawal account has $" + userAccounts.get(accountToGet-1).getBalance());
					System.out.println("How much would you like to withdraw?");
					double amount = myScanner.nextDouble();
					aSrv.withdraw(userAccounts.get(accountToGet-1), amount);
					properInput = true;
					System.out.println("Your withdrawal account has $" + userAccounts.get(accountToGet-1).getBalance());
				}
				else
					System.out.println("Invalid account specified you only have " + userAccounts.size() + " accounts");
			} catch (InputMismatchException e) {
				System.out.println("Invalid Input type");
			}catch(UnsupportedOperationException e){
				System.out.println("Negative number input. Please use a positive number");
			}catch(OverdraftException e) {
				System.out.println("You pulled out more money than you have. Please try again.");
			}catch(UnauthorizedException e){
				System.out.println("You do not have permission to carry out the transaction");
				System.out.println("please contact customer support");
				properInput = true;
			}
			finally {
				myScanner.nextLine();
			}
		}
	}
	/**********************************************************************
	 * Method: transfer
	 * Purpose: transfers money from one of a user's account to another
	 * 
	 *********************************************************************/
	public void transfer(Scanner myScanner) {
		boolean properInput = false;
		
		while(!properInput&& accountDb.getAccountsByUser(currentUser).size() >0)
			try {
				System.out.println("You have " + accountDb.getAccountsByUser(currentUser).size() + " accounts. Which one would you like to withdraw from?");
				int accountToGet= myScanner.nextInt();
				System.out.println("Which account would you like to deposit into?");
				int accountToDeposit = myScanner.nextInt();
				if(accountToDeposit == accountToGet)
					throw new InputMismatchException();
				List<Account> userAccounts = accountDb.getAccountsByUser(currentUser);
				if(accountToGet > 0 && accountToGet <= userAccounts.size())
				{
					System.out.println("Your withdrawal account has $" + userAccounts.get(accountToGet-1).getBalance());
					System.out.println("How much would you like to withdraw?");
					double amount = myScanner.nextDouble();
					aSrv.transfer(userAccounts.get(accountToGet-1), userAccounts.get(accountToDeposit-1), amount);
					properInput = true;
					System.out.println("Your withdrawal account has $" + userAccounts.get(accountToGet-1).getBalance());
					System.out.println("Your deposit account has $" + userAccounts.get(accountToDeposit-1).getBalance());
				}
				else
					System.out.println("Invalid account specified you only have " + userAccounts.size() + " accounts");
			} catch (InputMismatchException e) {
				System.out.println("Invalid Input type");
			}catch(UnsupportedOperationException e)
			{
				System.out.println("Negative number input. Please use a positive number");
			}catch(OverdraftException e) {
				System.out.println("You pulled out more money than you have. Please try again.");
			}catch (UnauthorizedException e) {
				System.out.println("You do not have permission to carry out the transaction");
				System.out.println("please contact customer support");
				properInput = true;
			}
			finally {
					myScanner.nextLine();
			}
	}
	public void toggleApproved(Scanner myScanner)
	{
		boolean properInput = false;
		int accountId;
		Account currentAccount = new Account();
		while (!properInput) {
			try {
				System.out.println("What is the id of the account you wish to toggle");
				accountId = myScanner.nextInt();
				currentAccount = accountDb.getAccount(accountId);
				if (currentAccount!= null)
					properInput =true;
				else
					throw new InputMismatchException();
			} catch (InputMismatchException e) {
				System.out.println("Invalid Input type");
			}finally {
				myScanner.nextLine();
			}
		}
		if(currentAccount.isApproved())
			currentAccount.setApproved(false);
		else
			currentAccount.setApproved(true);
		accountDb.updateAccount(currentAccount);
		System.out.println("Account Updated");
	}
}
