package com.bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Accounts {

	private Connection con =null;
	Scanner scan = new Scanner(System.in);
	
	public Accounts(Connection con, Scanner scan) {
			
			this.con = con;
			this.scan = scan;
		}
	
	public long open_account(String email)
	{
		if(!account_exist(email))
		{
			String query = "insert into accounts(account_number,full_name,email,balance,security_pin)values(?,?,?,?,?)";
			scan.nextLine();
			System.out.println("Enter Full_name");
			String full_name = scan.nextLine();
			scan.nextLine();
			System.out.println("Enter email");
			String email1 = scan.next();
			scan.nextLine();
			System.out.println("Enter initial amount:");
			double balance = scan.nextDouble();
			System.out.println("Enter secutiry pin:");
			String security_pin = scan.next();
			scan.nextLine();
			try {
				
				long account_number = generatedAccountNumber();
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/banking","root","root");
				PreparedStatement pstmt = con.prepareStatement(query);
				pstmt.setLong(1, account_number);
				pstmt.setString(2, full_name);
				pstmt.setString(3,email1);
				pstmt.setDouble(4,balance);
				pstmt.setString(5, security_pin);
				
				int row = pstmt.executeUpdate();
				
				if(row>0)
				{
					return account_number;
				}
				else
				{
					throw new RuntimeException("Account Creation failed");
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
		throw new RuntimeException("Account number doesn't exist!..");
	}
	
	public boolean account_exist(String email)
	{
		String query = "select account_number from Accounts where email=?";
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/banking","root","root");
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, email);
			ResultSet res = pstmt.executeQuery();
			if(res.next())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	private long generatedAccountNumber()
	{
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/banking","root","root");
			Statement stmt = con.createStatement();
			ResultSet res  = stmt.executeQuery("select account_number from accounts");
			if(res.next()) {
				long last_account_number = res.getLong("account_number");
				return last_account_number+1;
			}
			else
			{
				return 10000100;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return 10000100;
	}
	
	public long getAccount_number(String email)
	{
		String query = "select account_number from accounts where email=?";
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/banking","root","root");
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, email);
			ResultSet res = pstmt.executeQuery();
			
			if(res.next())
			{
				return res.getLong("account_number");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		throw new RuntimeException("Account number doesn't exist!..");
	}
}
