import java.sql.SQLException;

public class Books {

    public static void addBook(ConnectionDB connection, String username, String userEmail, int telephone) throws SQLException {
        int i = connection.insertAttribute("users", "username", username) +
                connection.insertAttribute("users", "user_email", userEmail) +
                connection.insertAttribute("users", "telephone", telephone);

    }
}
