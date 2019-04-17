import java.sql.*;


public class SQLiteJDBC {

    public SQLiteJDBC(){}

    public Connection connectionToDatabase(){

        String url = "jdbc:sqlite:discourse.sqlite";
        try{
            Connection connection = DriverManager.getConnection(url);
            return connection;
        } catch (SQLException e){
            System.out.println("Can not connect to database discourse");
            return null;
        }

    }

}
