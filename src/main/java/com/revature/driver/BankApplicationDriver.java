package com.revature.driver;

import com.revature.beans.*;
import com.revature.beans.Account.AccountType;
import com.revature.dao.*;
import com.revature.utils.*;
import java.sql.Connection;
import java.sql.SQLException;
/**
 * This is the entry point to the application
 */
public class BankApplicationDriver {

	private static Connection conn;
	public static void main(String[] args) {
		// your code here...
		User user;
		AccountDaoDB myAccounts = new AccountDaoDB();
		UserDaoDB myUsers = new UserDaoDB();
		boolean appRunning = true;
		conn = ConnectionUtil.getConnection();
		do
		{
			try {
				System.out.println(myAccounts.tableExists(conn, "account"));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			myAccounts.Init();
			System.out.println(myAccounts.getAccounts());
			Account newAccount = new Account();
			newAccount.setId(3);
			newAccount.setOwnerId(1);
			newAccount.setBalance(123.45);
			newAccount.setType(AccountType.CHECKING);
			myAccounts.addAccount(newAccount);
			User newUser = new User();
			newUser.setFirstName("PeePee");
			newUser.setLastName("PooPoo");
			for(int i = 0; i < 5; i++)
			{
				newUser.setId(i+5);
				myUsers.addUser(newUser);
			}
			for(int i = 0; i < 5; i++)
			{
				newAccount = myAccounts.getAccount(i+5);
				myAccounts.removeAccount(newAccount);
				System.out.println("Deleted");
			}
			System.out.println(myAccounts.getAccount(5));
			System.out.println(myAccounts.getAccounts());
			
			appRunning = false;
			System.out.println("");
		}while(appRunning == true);
	}

}
