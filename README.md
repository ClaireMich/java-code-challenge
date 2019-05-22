# java-code-challenge


Take into consideration that this appication is set up to work with mysql, default configuration is set up for 
host = localhost
port = 3306
username = root
password = root
In order to make this code work go to your command line and position in folder java-code-challenge

For simple test run 

	csv-to-table.bat people.csv

This command line will create a database called "databasetest" with one table "tabletest", all data will be added in this table. 
If it is needed to connect to a different configuration you just need to add them as a parameter 

List of parameters in order 

1) file name (required)
2) Database name 
3) Table Name
4) Host Name
5) Port Number
6) UserName 
7) Password 

For only changing one parameter but keep default values in parameters before it write "d" or "D" for parameters where default value is desire

Examples:

	csv-to-table.bat people.csv People
		This will create a Database (If does not exist) called People

	csv-to-table.bat people.csv d PeopleTableName
		This will connect to default database which is called "databasetest" and create 
		a table called "PeopleTableName" which will have data added
