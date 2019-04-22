package datahandling;


import java.sql.*;


public class SQLiteJDBC {

    public SQLiteJDBC(){}

    public Connection getConnectionToDatabase(String databaseType){
        String url = "jdbc:sqlite:discourse.sqlite";
        if(databaseType.equalsIgnoreCase("test")){
            url = "jdbc:sqlite:testDatabase.sqlite";
        }
        try{
            Connection connection = DriverManager.getConnection(url);
            return connection;
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Cannot connect to the discourse database");
            return null;
        }

    }


}
