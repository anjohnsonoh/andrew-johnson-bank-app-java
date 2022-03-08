package com.revature.dao;

import java.util.ArrayList;
import java.util.List;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.revature.beans.Account;
import com.revature.beans.Account.AccountType;
import com.revature.beans.Transaction;
import com.revature.beans.User;
import com.revature.utils.ConnectionUtil;

/**
 * Implementation of AccountDAO which reads/writes to a database
 */
public class AccountDaoDB implements AccountDao {

	private static Connection conn;
	private static Statement stmt;
	private static PreparedStatement pstmt;
	private static ResultSet rs;
	private String table_name = "Account";
	
	private List<Account> accounts = new ArrayList<Account>();
	public void Init()
	{
		String q = "DELETE FROM account;";
		conn = ConnectionUtil.getConnection();
		try {
			Statement s = conn.createStatement();
			s.executeUpdate(q);
			System.out.println(q);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public boolean tableExists(Connection connection, String tableName) throws SQLException {
	    DatabaseMetaData meta = connection.getMetaData();
	    ResultSet resultSet = meta.getTables(null, null, tableName, new String[] {"TABLE"});
	    while(resultSet.next())
	    {
	    	String name = resultSet.getString("TABLE_NAME");
	    	if(name.equals(tableName))
	    		return true;
	    }
	    return false;
	    	
	}
	public Account addAccount(Account a) {
		String q = "INSERT INTO account (accountId, ownerId, balance, type, approved, transactions) values (?,?,?,?,?,?)";
		conn = ConnectionUtil.getConnection();
		try {
			pstmt = conn.prepareStatement(q);
			pstmt.setInt(1, a.getId());
			pstmt.setInt(2, a.getOwnerId());
			pstmt.setDouble(3, a.getBalance());
			pstmt.setString(4, a.getType().name());
			pstmt.setBoolean(5, a.isApproved());
			pstmt.setBlob(6, (Blob)a.getTransactions());
			pstmt.executeUpdate();
			System.out.println(pstmt.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		return null;
	}

	public Account getAccount(Integer actId) {
		String query = "select * from account where accountId="+actId;
		Account act = new Account();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			if(rs.next())
			{
				act.setId(rs.getInt("accountId"));
				act.setOwnerId(rs.getInt("ownerId"));
				act.setBalance(rs.getDouble("balance"));
				if(rs.getString("type").equals("CHECKING"))
					act.setType(AccountType.CHECKING);
				else if (rs.getString("type").equals("SAVINGS"))
					act.setType(AccountType.SAVINGS);
				else
					System.out.println("NO TYPE FOUND");
				act.setApproved(rs.getBoolean("approved"));
				act.setTransactions((List<Transaction>)rs.getBlob("transactions"));
			}
			if(act.getId() == actId)
			{
				return act;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return null;
	}

	public List<Account> getAccounts() {
		List<Account> accountList = new ArrayList<Account>();
		String query = "select * from account";

		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				Account act = new Account();
				act.setId(rs.getInt("accountId"));
				act.setOwnerId(rs.getInt("ownerId"));
				act.setBalance(rs.getDouble("balance"));
				System.out.println(rs.getString("type"));
				if(rs.getString("type").equals("CHECKING"))
					act.setType(AccountType.CHECKING);
				else if (rs.getString("type").equals("SAVINGS"))
					act.setType(AccountType.SAVINGS);
				else
					System.out.println("NO TYPE FOUND");
				act.setApproved(rs.getBoolean("approved"));
				act.setTransactions((List<Transaction>)rs.getBlob("transactions"));
				accountList.add(act);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return accountList;
	}

	public List<Account> getAccountsByUser(User u) {
		// TODO Auto-generated method stub
		List<Account> userAccounts = new ArrayList<Account>();
		String query = "select * from account where ownerId="+u.getId();
		Account act = new Account();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			if(rs.next())
			{
				act.setId(rs.getInt("accountId"));
				act.setOwnerId(rs.getInt("ownerId"));
				act.setBalance(rs.getDouble("balance"));
				if(rs.getString("type").equals("CHECKING"))
					act.setType(AccountType.CHECKING);
				else if (rs.getString("type").equals("SAVINGS"))
					act.setType(AccountType.SAVINGS);
				else
					System.out.println("NO TYPE FOUND");
				act.setApproved(rs.getBoolean("approved"));
				act.setTransactions((List<Transaction>)rs.getBlob("transactions"));
				userAccounts.add(act);
				
			}
			return userAccounts;
		}catch(SQLException e){
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return null;
	}

	public Account updateAccount(Account a) {
		String q = "UPDATE account set ownerId=(?), balance=(?), type=(?), approved=(?), transactions=(?) WHERE accountId=(?)";
		try {
			pstmt = conn.prepareStatement(q);
			pstmt.setInt(6, a.getId());
			pstmt.setInt(1, a.getOwnerId());
			pstmt.setDouble(2, a.getBalance());
			pstmt.setString(3, a.getType().name());
			pstmt.setBoolean(4, a.isApproved());
			pstmt.setBlob(5, (Blob)a.getTransactions());
			pstmt.executeUpdate();
			System.out.println(pstmt.toString());
			return getAccount(a.getId());
		} catch (SQLException e) {
			// TODO: handle exception
		}
		// TODO Auto-generated method stub
		return null;
	}

	public boolean removeAccount(Account a) {
		String q = "DELETE FROM account WHERE accountID=(?)";
		try {
			pstmt = conn.prepareStatement(q);
			pstmt.setInt(1, a.getId());
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			// TODO: handle exception
		}
		// TODO Auto-generated method stub
		return false;
	}

}
