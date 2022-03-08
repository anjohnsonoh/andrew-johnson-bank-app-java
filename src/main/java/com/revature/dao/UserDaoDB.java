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
import com.revature.beans.User.UserType;
import com.revature.beans.Transaction;
import com.revature.beans.User;
import com.revature.utils.ConnectionUtil;

import com.revature.beans.User;

/**
 * Implementation of UserDAO that reads/writes to a relational database
 */
public class UserDaoDB implements UserDao {
	
	private static Connection conn;
	private static Statement stmt;
	private static PreparedStatement pstmt;
	private static ResultSet rs;
	private String table_name = "User";
	private List<User> users = new ArrayList<User>();
	/**
	 * Inserts a new user into the persistence layer
	 * @param user the user to insert
	 * @return the newly added user object
	 */
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
	public User addUser(User user) {
		String q = "INSERT INTO user (id, username, password, firstName, lastName, userType, accounts) values (?,?,?,?,?,?,?)";
		conn = ConnectionUtil.getConnection();
		try {
			pstmt = conn.prepareStatement(q);
			pstmt.setInt(1, user.getId());
			pstmt.setString(2, user.getUsername());
			pstmt.setString(3, user.getPassword());
			pstmt.setString(4, user.getFirstName());
			pstmt.setString(5, user.getLastName());
			System.out.println(user.getUserType());
			if(user.getUserType() == null)
				user.setUserType(UserType.CUSTOMER);
			pstmt.setString(6, user.getUserType().name());
			pstmt.setBlob(7, (Blob)user.getAccounts());
			pstmt.executeUpdate();
			System.out.println(pstmt.toString());
			return user;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return null;
	}

	public User getUser(Integer userId) {
		String query = "select * from user where id="+userId;
		User user = new User();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			if(rs.next())
			{
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setFirstName(rs.getString("firstName"));
				user.setLastName(rs.getString("lastName"));
				if(rs.getString("userType").equals("CUSTOMER"))
					user.setUserType(UserType.CUSTOMER);
				else if (rs.getString("type").equals("EMPLOYEE"))
					user.setUserType(UserType.EMPLOYEE);
				else
					System.out.println("NO TYPE FOUND");
				user.setAccounts((List<Account>) rs.getBlob("accounts"));
			}
			
			if(user.getId().equals(userId))
			{
				return user;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return null;
	}

	public User getUser(String username, String pass) {
		for (User user : users) {
			if(user.getUsername() == username && user.getPassword() == pass)
				return user;
		}// TODO Auto-generated method stub
		return null;
	}

	public List<User> getAllUsers() {
		List<User> userList = new ArrayList<User>();
		
		String q = "select * from user";
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(q);
			while(rs.next())
			{
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setFirstName(rs.getString("firstName"));
				user.setLastName(rs.getString("lastName"));
				if(rs.getString("userType").equals("CUSTOMER"))
					user.setUserType(UserType.CUSTOMER);
				else if (rs.getString("type").equals("EMPLOYEE"))
					user.setUserType(UserType.EMPLOYEE);
				else
					System.out.println("NO TYPE FOUND");
				user.setAccounts((List<Account>) rs.getBlob("accounts"));
				userList.add(user);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userList;
		
	}

	public User updateUser(User u) {
		for (User user : users) {
			if(user.getUsername() == u.getUsername() && user.getPassword() == u.getPassword())
				{
					user = u;
					return u;
				}
		}
		// TODO Auto-generated method stub
		return null;
	}

	public boolean removeUser(User u) {
		int i = 0;
		for (User user : users) {
			if(user.getUsername() == u.getUsername() && user.getPassword() == u.getPassword())
			{
				users.remove(i);
				return true;
			}
			i++;
				
		}
		// TODO Auto-generated method stub
		return false;
	}

}
