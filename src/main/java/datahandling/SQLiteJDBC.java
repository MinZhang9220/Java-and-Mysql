package datahandling;


import java.sql.*;


public class SQLiteJDBC {

    public SQLiteJDBC(){}

    /**
     * Gets the connection to the SQL database used to make queries to the database
     * @param databaseType the type of database. If the string is "test" the test database connection is accessed
     * @return the SQLite connection to the database
     */
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
