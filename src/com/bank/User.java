package com.bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class User {
	
	private static  Connection con = null;
	private static Scanner scan = new Scanner(System.in);
	
	public User(Connection con, Scanner scan)
	{
		this.con = con;
		this.scan = scan;
	}
	
	public static void registration()
	{
		scan.nextLine();
		System.out.println("Full_Name");
		String full_name = scan.nextLine();
		scan.nextLine();
		System.out.println("Enter email:");
		String email = scan.next();
		scan.nextLine();
		System.out.println("Enter password:");
		String password = scan.next();
		scan.nextLine();
		
		if(user_exists(email))
		{
			System.out.println("User is already is existed");
			return;
		}
		
		String query = "insert into user(full_name,email,password)values(?,?,?)";
		
		try {
			
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/banking","root","root");
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, full_name);
			pstmt.setString(2, email);
			pstmt.setString(3, password);
			
			int n = pstmt.executeUpdate();
			
			if(n>0)
			{
				System.out.println("Registration is successfull");
			}
			else
			{
				System.out.println("Registration is failed");
			}
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static String login()
	{
		System.out.println("Email: ");
		String email = scan.next();
		scan.nextLine();
		System.out.println("Enter password: ");
		String password = scan.next();
		scan.nextLine();
		
		String query = "select * from user where email=? and password=?";
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/banking","root","root");
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, email);
			pstmt.setString(2, password);
			
			
			ResultSet res = pstmt.executeQuery();
			if(res.next()) {
				return email;
			}
			else
			{
				return null;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean user_exists(String email)
	{
		String query = "select * from user where email=?";
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/banking","root","root");
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, email);
			
			ResultSet res = pstmt.executeQuery();
			
			if(res.next()) {
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

	
}
