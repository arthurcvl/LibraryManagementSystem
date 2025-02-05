import java.sql.*;

public class User {


    public static void getAllUsers(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
        while(resultSet.next()){
            System.out.println("Name: " + resultSet.getString("username"));
        }
    }

    public static void addUser(Connection connection, String username, String userEmail, int telephone) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO users(username, user_email, telephone) VALUES(?, ?, ?);");
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, userEmail);
        preparedStatement.setInt(3, telephone);

        int i = preparedStatement.executeUpdate();
        if(i > 0) System.out.println("User " + username + " added with sucess!");
        else System.out.println("Add User failed");
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

    public static void editUser(Connection connection, int user_id){
        return;
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
