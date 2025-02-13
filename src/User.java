import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class User {


    public static void getAllUsers(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
        while(resultSet.next()){
            System.out.println("Name: " + resultSet.getString("username"));
            System.out.println("Email: " + resultSet.getString("user_email"));
            System.out.println("Telephone: " + resultSet.getInt("telephone"));
        }
    }


    public static void addUser(Connection connection, String username, String userEmail, int telephone) throws SQLException {
        ConnectionDB.addRow("users",
                new LinkedHashMap<>(Map.of("username", username, "user_email", userEmail, "telephone", telephone)),
                connection);
    }


    public static int getUserId(Connection connection, String username, String userEmail) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT user_id FROM users WHERE users.username = ? AND users.user_email = ?;");
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, userEmail);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt("user_id");
    }

    public static void editUser(Connection connection, int user_id, String newName, String newEmail, int newTelephone) throws SQLException {
        System.out.println(editUserAttribute(connection, user_id, "username", newName));
        System.out.println(editUserAttribute(connection, user_id, "user_email", newEmail));
        System.out.println(editUserAttribute(connection, user_id, "telephone", newTelephone));
    }

    public static <T> String editUserAttribute(Connection connection, int user_id, String editAttribute, T newValue) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE users SET users." + editAttribute + " = ? WHERE user_id = ?;");
        if(newValue instanceof String) preparedStatement.setString(1, (String) newValue);
        else preparedStatement.setInt(1, (int) newValue);
        preparedStatement.setInt(2, user_id);

        int i = preparedStatement.executeUpdate();
        if(i > 0 ) return "User" + editAttribute + "Updated with Success";
        return editAttribute + " Update Failed";
    }


    public static void deleteUser(Connection connection, String username, String userEmail) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM users WHERE users.username = ? AND users.user_email = ?;");
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, userEmail);

        int i = preparedStatement.executeUpdate();
        if(i > 0) System.out.println("User " + username + " deleted with sucess!");
        else System.out.println("Delete User failed");
    }
}
