package function;


import java.sql.*;


public class SQLiteJDBC {

    public SQLiteJDBC(){}

    public Connection connectionToDatabase(){

        String url = "jdbc:sqlite:discourse.sqlite";
        try{

            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement("select name from organisation where name = \"University of canterbury\"");
            ResultSet resultSet = preparedStatement.executeQuery();

            preparedStatement.closeOnCompletion();

            if(resultSet.next()){
                return connection;
            } else {
                return null;
            }
        } catch (SQLException e){
            System.out.println("Can not connect to database discourse");
            return null;
        }

    }


}
