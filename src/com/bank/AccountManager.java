package com.bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {

	private Connection con = null;
	Scanner scan = new Scanner(System.in);
	
	public AccountManager(Connection con,Scanner scan)
	{
		this.con = con;
		this.scan = scan;
	}
	
	public void credit_money(long account_number) throws SQLException
	{
		System.out.print("Enter amount: ");
		double amount = scan.nextDouble();
		System.out.print("Enter security pin:");
		String security_pin = scan.next();
		scan.nextLine();
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/banking","root","root");
			PreparedStatement pstmt = con.prepareStatement("select * from accounts where security_pin=?");
			pstmt.setString(1, security_pin);
			ResultSet res = pstmt.executeQuery();
			
			if(res.next()) {
				String credit_query = "Update accounts set balance= balance+? where account_number=?";
				PreparedStatement pstmt1 = con.prepareStatement(credit_query);
				pstmt1.setDouble(1,amount);
				pstmt1.setLong(2,account_number);
				con.setAutoCommit(false);
				int rows = pstmt1.executeUpdate();
				if(rows>0)
				{
					System.out.println("Rs. "+amount +"credited Successfully");
					con.commit();
					
					return;
				}
				else
				{
					System.out.println("Transaction failed");
					con.rollback();
					con.setAutoCommit(true);
					
				}
			}
			else
			{
				System.out.println("Invalid security pin");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		con.setAutoCommit(true);
	}
	
	public void debit_money(long account_number) throws SQLException
	{
		System.out.println("Enter amount");
		double amount = scan.nextDouble();
		System.out.println("Enter security pin:");
		String pin = scan.next();
		scan.nextLine();
		
		try {
			con.setAutoCommit(false);
			if(account_number!=0)
			{
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/banking","root","root");
				PreparedStatement pstmt = con.prepareStatement("select * from accounts where security_pin=?");
				pstmt.setString(1, pin);
				ResultSet res = pstmt.executeQuery();
				
				if(res.next()) {
					double current_balance = res.getDouble("balance");
					if(amount <= current_balance)
					{
						String credit_query = "Update accounts set balance = balance-? where account_number=?";
						PreparedStatement pstmt1 = con.prepareStatement(credit_query);
						pstmt1.setDouble(1,amount);
						pstmt1.setLong(2,account_number);
						
						int rows  = pstmt1.executeUpdate();
						con.setAutoCommit(false);
						if(rows > 0)
						{
							System.out.println("Rs. "+amount + "debited successfully ");
							con.commit();
							
							return;
						}
						else
						{
							System.out.println("Transaction failed..");
							con.rollback();
							con.setAutoCommit(true);
						}
						
					}
					else
					{
						System.out.println("Invalid balance");
					}
					
				}
				else
				{
					System.out.println("Insufficient balance!..");
				}
			}
			else
			{
				System.out.println("Invalid pin");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		con.setAutoCommit(true);
	}
	
	public void transfer_money(long sender_account_number) throws SQLException
	{
		System.out.print("Enter receiver account number:");
		long receiver_account_number = scan.nextLong();
		System.out.println("Enter amount:");
		double amount = scan.nextDouble();
		System.out.print("Enter security pin");
		String pin = scan.next();
		scan.nextLine();
		
		try {
			
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/banking","root","root");
			con.setAutoCommit(false);
			if(sender_account_number!=0 && receiver_account_number!=0)
			{
				PreparedStatement pstmt = con.prepareStatement("select * from accounts where account_number=? and security_pin=?");
				pstmt.setLong(1, sender_account_number);
				pstmt.setString(2, pin);
				ResultSet res = pstmt.executeQuery();
				if(res.next()) {
					double current_balance = res.getDouble("balance");
					if(amount <= current_balance)
					{
						String debit_query = "Update accounts set balance = balance-? where account_number=?";
						String credit_query = "update accounts set balance = balance+? where account_number=?";
						
						PreparedStatement pstmt1 = con.prepareStatement(credit_query);
						PreparedStatement pstmt2 = con.prepareStatement(debit_query);
						pstmt1.setDouble(1,amount);
						pstmt1.setLong(2,receiver_account_number);
						pstmt2.setDouble(1,amount);
						pstmt2.setLong(2, sender_account_number);
						
						int row1 = pstmt1.executeUpdate();
						int row2 = pstmt2.executeUpdate();
						if(row1 > 0 && row2 >0)
						{
							System.out.println("Transaction succesful");
							System.out.println("Rs."+amount + "Transferred Successfully");
							con.commit();
							con.setAutoCommit(true);
							return;
						}
						else
						{
							System.out.println("Transaction failed");
							con.rollback();
							con.setAutoCommit(true);
						}
					}
					else
					{
						System.out.println("Insufficient balance!..");
					}
				}
				else
				{
					System.out.println("Invalid security pin..");
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		con.setAutoCommit(true);
	}
	
	
	public void getBalance(long account_number)
	{
		System.out.println("Enter security pin: ");
		String pin = scan.next();
		scan.nextLine();
		try {
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/banking","root","root");
			PreparedStatement pstmt = con.prepareStatement("select balance from accounts where account_number=? and security_pin=?");
			pstmt.setLong(1, account_number);
			pstmt.setString(2, pin);
			ResultSet res = pstmt.executeQuery();
			if(res.next())
			{
				double balance = res.getDouble("balance");
				System.out.println("balance: "+balance);
			}
			else
			{
				System.out.println("Invalid pin..");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
