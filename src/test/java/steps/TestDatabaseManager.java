package steps;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TestDatabaseManager {

    public void resetDatabase(Connection connection){
        try {
            PreparedStatement statement = connection.prepareStatement("delete from affiliation");
            statement.executeUpdate();
            statement = connection.prepareStatement("delete from actor");
            statement.executeUpdate();
            statement = connection.prepareStatement("delete from organisation");
            statement.executeUpdate();
            statement = connection.prepareStatement("delete from argument");
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
