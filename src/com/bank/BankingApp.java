package com.bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class BankingApp {

	private static String url = "jdbc:mysql://localhost:3306/banking";
	private static String user = "root";
	private static String password = "root";
	
	private static Connection con=null;
	private static PreparedStatement pstmt = null;
	private static Statement stmt = null;
	private static ResultSet res = null;
	
	
	public static void main(String[] args) {
		
		Scanner scan = new Scanner(System.in);
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url,user,password);
			
			User user = new User(con,scan);
			Accounts accounts = new Accounts(con,scan);
			AccountManager accountManager = new AccountManager(con,scan);
			
			
			String email;
			long account_number;
			
			while(true)
			{
				System.out.println("Welcome to Banking Management System");
				System.out.println();
				System.out.println("1. Register");
				System.out.println("2. Login");
				System.out.println("3. Exit");
				
				System.out.println("Enter ur choice:");
				int choice = scan.nextInt();
				switch(choice) {
				case 1:
					User.registration();
					System.out.println();
					break;
				case 2:
					email = User.login();
					if(email!=null)
					{
						System.out.println();
						System.out.println("User Logged In!..");
						if(!accounts.account_exist(email))
						{
							System.out.println();
							System.out.println("1. Open a new Bank Account:");
							System.out.println("2. Exit");
							int num = scan.nextInt();
							if(num == 1)
							{
								account_number = accounts.open_account(email);
								System.out.println("Account created successfully");
								System.out.println("Your account number is:"+account_number);
							}
							else
							{
								break;
							}
						}
						account_number = accounts.getAccount_number(email);
						int num2 = 0;
						while(num2!=5)
						{
							System.out.println();
							System.out.println("1. Debit money");
							System.out.println("2. Credit money");
							System.out.println("3. Transfer money");
							System.out.println("4. check balance");
							System.out.println("5. log out");
							System.out.println("Enter your choice:");
							num2 = scan.nextInt();
							switch(num2)
							{
							case 1:
								accountManager.debit_money(account_number);
								break;
							
							case 2: 
								accountManager.credit_money(account_number);
								break;
							case 3: 
								accountManager.transfer_money(account_number);
								break;
							case 4:
								accountManager.getBalance(account_number);
								break;
							case 5: 
								break;
							default: System.out.println("Enter valid choice!--");
							break;
							}
							
						}
					}
					else
					{
						System.out.println("Incorrect email or password");
						System.out.println("Try again..");
						User.login();
					}
				case 3:
					System.out.println("Thank you for using banking system");
					System.out.println("Exit system");
					return;
				
				}
			}
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		

	}

}
