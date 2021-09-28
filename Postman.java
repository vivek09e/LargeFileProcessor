package com.MileStone;

  
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.io.BufferedReader;  
import java.io.FileReader;  

public class Postman extends Thread {
	  static long start = System.nanoTime();
	 // some time passes
	 
	 
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/h2h_internship";         
	// Database credentials
	static final String USER = "root";
	static final String PASS = "Vivek09e@";
	public static Connection conn = null;
	public static Statement stmt = null;
	
	
	
	
	public static void main(String[] args) {
		
		
		
		//Using line variable to Store every single line one by one of the CSV to process it!!
		String line = ""; 
		
		//Initializing delimiter splitby with "," as CSV store the data by comma !! 
		String splitBy = ","; 
		try{
			//Registering JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			//Opening a connection!!
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			ArrayList<Product> arrlist =new ArrayList<Product>();
			//Creating the Arraylist to save the info of all the customer as an obejct of invoice_details pojo class!!
			//ArrayList <Product> arrlist=new ArrayList<Product>();
			
			// Here I'm initializing count and iteration to zero
			//The main Purpose of using count is to count how many rows are there in csv excluding header!
			//iteration is used to skip first line that is the header line of csv!!
			int count=0,iter=0;
			{
				//Creating BufferedReader object br to read the CSV file!!
				BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Vivek\\Documents\\Postman\\products.csv")); 
				while ((line = br.readLine()) != null)   //To process every single line in csv one by one!!  
					{  
						if(iter == 0)
						{	
							//For first iteration incrementing the iter value to skip the Header Line of the CSV!!
							iter++;  
							continue;
						}
						
						//Creating the id1 the pojo class object to set the data!!
						Product id1=new Product();
			
						//Saving the every line separated by "," in the array of string name a!!   	
						String[] a = line.split(splitBy);
						// using comma as separator
						
						if(a.length<3)
						{
							arrlist.get(arrlist.size() - 1).setDescription(arrlist.get(arrlist.size() - 1).getDescription()+" "+line);
							//System.out.println(line);
							
						}
						else
						{
						//Storing the Business_code in pojo object!! 
						if(a[0].isEmpty())
							id1.setName(null);
						else
							id1.setName(a[0]);
			
						//Storing the Cust_number in pojo object!!
						id1.setSku(a[1]);
						//Storing the Name_customer in pojo object!!
						if(a[2].isEmpty())
							id1.setDescription(null);
						else
							id1.setDescription(a[2]);
			
									arrlist.add(id1);
						System.out.println("NAME: "+arrlist.get(arrlist.size() - 1).getName()+", SKU:"+arrlist.get(arrlist.size() - 1).getSku()+", DESCRIPTION "+arrlist.get(arrlist.size() - 1).getDescription());

						//Incrementing the count as the pojo object is added!!
						count++;
						}
						
					}
				
				//Closing the BufferReader object After reading all the Rows in Csv.
				br.close();
				
				//Printing the Number of Rows Added in the Arrlist!!
				System.out.print("Total Number Of Rows in CSV Exculding Header Row: "+count+"\n");
			}
			Postman t1=new Postman();
			Postman t2=new Postman();
			t1.start();
			
			
			//Creating the stmt object of createStatement to execute the queries!!
			stmt = conn.createStatement();
			
			//Creating query to insert into invoice_details table in the Sqlyog!! 
			String query = "INSERT INTO PostMan VALUES (?, ?, ?)";
			
			//Creating pstmt object of PreparedStatement to Prepare the insert Statement to insert the Data!!
			PreparedStatement pstmt = conn.prepareStatement(query);
			
			//Creating query3 to check that a doc_id Already Exists in the table or not if Exist Skipping the that row!!
			String query3="SELECT * FROM PostMan WHERE sku=";
			
			//Creating the rs object of ResultSet to store the output of the query3!! 
			ResultSet rs=null;
			
			//Initializing RowsAddedinSql variable to 0 it is used to know how many new Rows are Added into the SQL table!!
			int RowsAddedinSql=0;  
			int i=0;
			//Inserting the Data in the Sql Table!!
			for(Product x: arrlist )
			{	
				
				//Storing the Data of query3 in rs!!
				rs=stmt.executeQuery(query3+"'"+x.getSku()+"'");
				
				//Checking if rs is null then entering the data one by one!!
				if(!rs.next())
				{
					//Passing Business_code to pstmt From pojo object which is stored in arrlist!!
					pstmt.setString(1, x.getName());
					
					//Passing Cust_number to pstmt From pojo object which is stored in arrlist!!
					pstmt.setString(2, x.getSku());
					
					//Passing Name_customer to pstmt From pojo object which is stored in arrlist!!
					pstmt.setString(3, x.getDescription());
					pstmt.execute();
					i++;
					//Incrementing the RowsAddedinSql when a row iis added to the Sql Table
					RowsAddedinSql++;
				}
				
			}
		
			//Printing the Number of rows Added in the SQl Table!!
			System.out.println("Number of Rows Added in the Product table: "+RowsAddedinSql);
			
			//Closing the rs object!!
			rs.close();
			long end = System.nanoTime();
			long elapsedTime = end - start; 
			System.out.println("TIME TAKENN ::::: "+elapsedTime); 
		}  
		catch(SQLException se)
		{
			//Handling errors for JDBC
			se.printStackTrace();
		}
		catch(Exception e)
		{
			//Handling errors for Class.forName
			e.printStackTrace();
		}
		finally
		{
			//finally block used to close resources
			try
			{
				if(stmt!=null)
					stmt.close();
			}
			catch(SQLException se2)
			{
			}// nothing we can do
			try
			{
				if(conn!=null)
					conn.close();
			}
			catch(SQLException se)
			{
				se.printStackTrace();
			}
		}
		System.out.println("\nAll the Queries Executed Successfully!!");
	}


}



