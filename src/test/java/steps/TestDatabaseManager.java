package steps;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDatabaseManager {

    /**
     * Resets the database and closes the database connection
     * @param connection the connection to the database
     */
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

    public void compareStringToFile(String expectedResult, String fileName){
        try {
            InputStream fips = new FileInputStream(new File("./src/test/resources/inputfiles/" + fileName));
            String result = IOUtils.toString(fips, StandardCharsets.UTF_8);
            Assert.assertEquals(result, expectedResult);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    public void populateDatabaseWithOrganisations(Connection connection){
        try{
            PreparedStatement statement = connection.prepareStatement("insert into organisation(name) values (?)");
            statement.setString(1,"test");
            statement.closeOnCompletion();
            assertEquals(1,statement.executeUpdate());
            statement = connection.prepareStatement("insert into organisation(name) values (?)");
            statement.setString(1,"University of Canterbury");
            statement.closeOnCompletion();
            assertEquals(1,statement.executeUpdate());
            statement = connection.prepareStatement("insert into organisation(name) values (?)");
            statement.setString(1,"U.A");
            statement.closeOnCompletion();
            assertEquals(1,statement.executeUpdate());
            statement = connection.prepareStatement("insert into organisation(name) values (?)");
            statement.setString(1,"nonUniqueOrganisation");
            statement.closeOnCompletion();
            assertEquals(1,statement.executeUpdate());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void populateDatabaseWithActors(Connection connection){
        try{
            PreparedStatement statement = connection.prepareStatement("insert into actor(firstname,lastname) values (?,?)");
            statement.setString(1,"Katsuki");
            statement.setString(2,"Bakugo");
            statement.closeOnCompletion();
            assertEquals(1,statement.executeUpdate());
            statement = connection.prepareStatement("insert into actor(firstname,lastname) values (?,?)");
            statement.setString(1,"All");
            statement.setString(2,"Might");
            statement.closeOnCompletion();
            assertEquals(1,statement.executeUpdate());
            statement = connection.prepareStatement("insert into actor(firstname,lastname) values (?,?)");
            statement.setString(1,"Ochaco");
            statement.setString(2,"Uraraka");
            statement.closeOnCompletion();
            assertEquals(1,statement.executeUpdate());
            statement = connection.prepareStatement("insert into actor(firstname,lastname) values (?,?)");
            statement.setString(1,"Tenya");
            statement.setString(2,"Iida");
            statement.closeOnCompletion();
            assertEquals(1,statement.executeUpdate());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
