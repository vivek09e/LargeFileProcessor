# Large File Processor.
LARGE FILE PROCESSOR: Aim is to build a system which is able to handle long running processes in a distributed fashion.
1. Steps to run your code:-
  -> Implemented 2 ways to implement the dataone with simple but time consuming other is with Multithreading.
  -> Created the POJO class of 'product.csv' for the better handling of the flow of the data throughout the program.
  -> Created an arraylist of the POJO class to get the data from the csv file.
  -> Created a Multithreaded function to insert multiple batches of data into the DATABASE at the same instance.


2. Details of all the tables and their schema:-
  -> Created a table Postman to insert the data with following Query:
  """CREATE TABLE Postman (
  NAME Varchar(40),
  SKU Varchar(40) PRIMARY KEY,
  DESCRIPTION Varchar(1000)
  );"""

3. “Points to achieve” and number of entries in all your tables with sample 10 rows from each
-> Tried to implement the mutithreaded functions to send bulk amount of data in less time. 
-> Main Table consist of 5,00,000 rows.
![image](https://user-images.githubusercontent.com/73744393/135102192-3288c926-e98e-456b-ac29-aafa3a5b9680.png)

4. Not done from “Points to achieve”. If not achieve, reasons and current workarounds.
-> Not created multiple Tables that could have help to divide the data into small pieces and then and insert all at once.
-> First I wanted to implement the programm purly using the JAVA and I achieved it by implementing the multithreading. but I ran out of the time.
-> Currently created the main table and inserting all the data directly into the table and used multithreading function to achieve some 70% of the goal.

5. What would you improve if given more days:-
-> Use multiple tables along with the multithreading to work efficiently and in optimally ["DIVIDE AND CONQUER"].

