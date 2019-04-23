package steps;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;

import java.io.*;
import java.nio.charset.StandardCharsets;
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
}
