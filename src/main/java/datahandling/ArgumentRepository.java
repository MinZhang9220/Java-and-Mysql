package datahandling;

import models.Argument;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The argument repository used to make queries to the database
 */
public class ArgumentRepository {

    /**
     * The SQL connection which is the database connection
     */
    private Connection connection;

    /**
     * Constructor for the argument repository
     * @param connection the SQL connection used to make queries to the database
     */
    public ArgumentRepository(Connection connection){
        this.connection = connection;
    }

    /**
     * Method to insert an argument in the database.
     * @param argument the argument to be inserted
     * @return true if the argument is successfully inserted, false otherwise
     */
    public boolean insertArgument(Argument argument){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into argument(discourseid,startindex,endindex,rephrasing) values (?,?,?,?)");
            preparedStatement.setInt(1,argument.getDiscourse().getId());
            preparedStatement.setInt(2,argument.getStartIndex());
            preparedStatement.setInt(3,argument.getEndIndex());
            preparedStatement.setString(4,argument.getRephrasing());
            preparedStatement.closeOnCompletion();
            int count = preparedStatement.executeUpdate();
            return count > 0;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get how many duplicate argument in database.
     * The count for duplicate is 1 if there is an argument with the same discourse id, startIndex and endIndex exist in database,otherwise count will be 0.
     * @param argument
     * @param startIndex
     * @param endIndex
     * @return count
     */
    public Integer getDuplicateArgumentCount(Argument argument, int startIndex, int endIndex){
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select count(*) as total from argument " +
                            "where discourseid = ? and startindex = ? and endindex = ?");
            statement.setInt(1, argument.getDiscourse().getId());
            statement.setInt(2, startIndex);
            statement.setInt(3, endIndex);
            ResultSet result = statement.executeQuery();
            int count = 0;
            while (result.next()) {
                count = result.getInt("total");
            }
            return count;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
