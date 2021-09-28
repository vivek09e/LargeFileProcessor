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
 	        		//preparedStatement.clearBatch();
 	        		connection.commit();
 	        		incr=0;
 	        	}else if (size==totSize){
 	        		preparedStatement.executeBatch();
 	        		//preparedStatement.clearBatch();
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
            //System.out.println(Thread.currentThread().getName()+" : "+emp);
             
                jdbcConnection.insertValues(emp,Productlist.size());
             
             
        }
    }
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Postmantrail employeeThread =  new Postmantrail();
         String line = ""; 
 		 String splitBy = ",";    
         int count=0,iter=0;
         try
         {
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
							Postmantrail.Productlist.get(Postmantrail.Productlist.size() - 1).setDescription(Postmantrail.Productlist.get(Postmantrail.Productlist.size() - 1).getDescription()+" "+line);
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
			
						Postmantrail.Productlist.add(id1);
						System.out.println("NAME: "+Postmantrail.Productlist.get(Postmantrail.Productlist.size() - 1).getName()+", SKU:"+Postmantrail.Productlist.get(Postmantrail.Productlist.size() - 1).getSku()+", DESCRIPTION "+Postmantrail.Productlist.get(Postmantrail.Productlist.size() - 1).getDescription());

						//Incrementing the count as the pojo object is added!!
						count++;
						}
						
					}
				
				//Closing the BufferReader object After reading all the Rows in Csv.
				br.close();
				
				//Printing the Number of Rows Added in the Arrlist!!
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

