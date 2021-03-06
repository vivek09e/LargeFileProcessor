package com.MileStone;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
 
class JdbcConnection {
    String selectSQL = "INSERT INTO PostMan VALUES (?, ?, ?)";
    static Connection connection;
    PreparedStatement preparedStatement=null;
    public static Statement stmt = null;
    int incr=0;
    int totSize=0;
    static{
         try{
        System.out.println("Connecting to the database...");  
        Class.forName("com.mysql.jdbc.Driver");
         connection = DriverManager.getConnection("jdbc:mysql://localhost/h2h_internship", "root", "Vivek09e@"); 
        connection.setAutoCommit(false);
         } catch(Exception e){
             e.printStackTrace();
         }
    }
     
    public void insertValues(Product product, int size){
         
        synchronized (this) {
        incr++;
        totSize++;
         try{
        	 String query3="SELECT * FROM PostMan WHERE sku=";
 			ResultSet rs=null;
 			stmt = connection.createStatement();
 	         rs=stmt.executeQuery(query3+"'"+product.getSku()+"'");
 	        if(!rs.next())
 	        {
 	         
 	        	preparedStatement = connection.prepareStatement(selectSQL);        
 	        	preparedStatement.setString(1, product.getName());
 	        	preparedStatement.setString(2, product.getSku());
 	        	preparedStatement.setString(3, product.getDescription());	
 	        	preparedStatement.addBatch();
 	        	if(incr==1000){
 	        		System.out.println(totSize+" : "+"Batch ecxcuted");
 	        		preparedStatement.executeBatch();
 	        		connection.commit();
 	        		incr=0;
 	        	}else if (size==totSize){
 	        		preparedStatement.executeBatch();
 	        		connection.setAutoCommit(true);
 	        	}
 	        }
         	} catch(Exception e){
         		e.printStackTrace();
         	}
        }
    }
}
 
public class Postmantrail implements Runnable {
 
    JdbcConnection jdbcConnection = new JdbcConnection();
    public static List<Product> Productlist;
     
     
    Postmantrail(){
    	Productlist = new ArrayList<Product>();
         
    }
    public void run(){
         
        Iterator<Product> itr =  Productlist.iterator();
        while(itr.hasNext()){
        	Product emp = itr.next();           
                jdbcConnection.insertValues(emp,Productlist.size());
             
             
        }
    }
    
    public static void main(String[] args) {
        Postmantrail employeeThread =  new Postmantrail();
         String line = ""; 
 		 String splitBy = ",";    
         int count=0,iter=0;
         try
         {
			{
				
				BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Vivek\\Documents\\Postman\\products.csv")); 
				while ((line = br.readLine()) != null)     
					{  
						if(iter == 0)
						{	
						
							iter++;  
							continue;
						}
						
						
						Product id1=new Product();
			
					  	
						String[] a = line.split(splitBy);
					
						
						if(a.length<3)
						{
							Postmantrail.Productlist.get(Postmantrail.Productlist.size() - 1).setDescription(Postmantrail.Productlist.get(Postmantrail.Productlist.size() - 1).getDescription()+" "+line);
							
							
						}
						else
						{
						 
						if(a[0].isEmpty())
							id1.setName(null);
						else
							id1.setName(a[0]);
			
						
						id1.setSku(a[1]);
						if(a[2].isEmpty())
							id1.setDescription(null);
						else
							id1.setDescription(a[2]);
			
						Postmantrail.Productlist.add(id1);
						System.out.println("NAME: "+Postmantrail.Productlist.get(Postmantrail.Productlist.size() - 1).getName()+", SKU:"+Postmantrail.Productlist.get(Postmantrail.Productlist.size() - 1).getSku()+", DESCRIPTION "+Postmantrail.Productlist.get(Postmantrail.Productlist.size() - 1).getDescription());
						count++;
						}
						
					}
				
				
				br.close();
				System.out.print("Total Number Of Rows in CSV Exculding Header Row: "+count+"\n");
			}
         }
         catch(Exception e)
 		{
 			e.printStackTrace();
 		}
        Thread thread;
        Thread thread1;
        Thread thread2;
         thread = new Thread(employeeThread,"Thread");
         thread1 = new Thread(employeeThread,"Thread1");
         thread2 = new Thread(employeeThread,"Thread2");
        thread.start();
        thread1.start();
        thread2.start();
         
    }
 
}

