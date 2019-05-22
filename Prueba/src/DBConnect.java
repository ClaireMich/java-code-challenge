import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DBConnect {

    private Connection connection;
    private Statement statement;
    private String DBUrl;
    private String DBName;
    private Properties properties;
    private int numAttributes;
    private List<String> attributes;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public String getDBUrl() {
        return DBUrl;
    }

    public void setDBUrl(String DBUrl) {
        this.DBUrl = DBUrl;
    }

    public String getDBName() {
        return DBName;
    }

    public void setDBName(String DBName) {
        this.DBName = DBName;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public List getAttributes() {
        return attributes;
    }

    public void setAttributes(List attributes) {
        this.attributes = attributes;
    }

    public int getNumAttributes() {
        return numAttributes;
    }

    public void setNumAttributes(int numAttributes) {
        this.numAttributes = numAttributes;
    }

    public DBConnect(String host, String port, String username, String password)
    {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to a local mysql...");
            properties = new Properties();
            properties.setProperty("user", username);
            properties.setProperty("password", password);
            properties.setProperty("useSSL", "false");
            connection = DriverManager.getConnection("jdbc:mysql://"+host+":"+port, properties);
            setDBUrl("jdbc:mysql://"+host+":"+port);
            System.out.println("Connected successfully...");
            statement=connection.createStatement();
        }catch (Exception ex){
            System.out.println("Error: "+ ex.getMessage());
        }
    }

    public boolean createDBIfNotExists(String database)
    {
        Boolean result =false;
        try {
            System.out.println("Creating database...");
            String query = "CREATE DATABASE IF NOT EXISTS " + database;
            if (statement.execute(query)) {
                result=true;
            }
            setDBName(database);
            System.out.println("Finish creating database...");
        }catch (SQLException ex){
            System.out.println("Create DB if not Exists error: "+ ex.getMessage());
        }
        closeConnection();
        return result;
    }

    public void connectToDataBase(){
        try {
            System.out.println("Connecting to a selected database...");
            connection = DriverManager.getConnection(getDBUrl()+"/"+getDBName(), properties);
            System.out.println("Connected database successfully...");
            statement = connection.createStatement();
        }catch (SQLException ex){
            System.out.println("Connecting to database error: "+ ex.getMessage());
        }
    }

    public void creatingTable(String[] data, String tableName){
        try {
            String sql = "";
            setAttributes(new ArrayList<String>());
            if (data != null) {
                System.out.println("Creating table ...");
                setNumAttributes(data.length);
                sql += "CREATE TABLE IF NOT EXISTS " + tableName + " (" + data[0] + " INTEGER not NULL, ";
                for (int i = 1; i < data.length; i++) {
                    sql += data[i] + " VARCHAR(255), ";
                    getAttributes().add(data[i]);
                }
                sql += " PRIMARY KEY ( " + data[0] + " ))";
            }
            statement.execute(sql);
            System.out.println("Finish creating table...");
        }catch (SQLException ex){
            System.out.println("Creating table error: "+ ex.getMessage());
        }
    }

    public void addingValues(String[] data, String tableName){
        try {
            String sql = "", duplicateSql=" ON DUPLICATE KEY UPDATE ";
            if (data != null) {
                System.out.println("Adding values to table...");
                sql += "Insert into " + tableName + " values ( ";
                if(getNumAttributes()>=data.length){
                    for (int i = 0; i < data.length; i++) {
                        if(i==0)
                            sql += data[i] + ",";
                        else {
                            sql += " '" + data[i] + "',";
                            duplicateSql+= getAttributes().get(i-1) +" = '"+data[i]+"',";
                        }
                    }
                    if(getNumAttributes()>data.length){
                        for(int i= data.length; i < getNumAttributes(); i++) {
                            sql += " ' ',";
                            duplicateSql+= getAttributes().get(i-1) +" = ' ',";
                        }
                    }
                }else
                {
                    for (int i = 0; i < getNumAttributes(); i++) {
                        if(i==0)
                            sql += data[i] + ",";
                        else {
                            sql += " '" + data[i] + "',";
                            duplicateSql+= getAttributes().get(i-1) +" = '"+data[i]+"',";
                        }
                    }
                }
                sql = sql.substring(0, sql.length() - 1);
                duplicateSql= duplicateSql.substring(0, duplicateSql.length() - 1);
                sql+=") "+duplicateSql;
            }
            statement.execute(sql);
            System.out.println("Finish adding values to table...");
        }catch (SQLException ex){
            System.out.println("Creating table error: "+ ex.getMessage());
        }
    }

    public void closeConnection()
    {
        //finally block used to close resources
        try{
            if(statement!=null)
                connection.close();
        }catch(SQLException se){
            System.out.println("Close statement ex:"+ se.getMessage());
        }// do nothing
        try{
            if(connection!=null)
                connection.close();
        }catch(SQLException se){
            System.out.println("Close connection ex:"+ se.getMessage());
        }//end finally try
    }
}

