import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        Scanner sc = new Scanner(System.in);
        String path = "/home/arthur/eclipse_projects/LibraryManagementSystem/src/credentials.csv";

        try(BufferedReader br = new BufferedReader(new FileReader(path))){
            String line = br.readLine();
            String[] credentials = line.split(",");
            ConnectionDB connectionDB = new ConnectionDB(credentials[0] + "libraryDB", credentials[1], credentials[2]);
            Connection connection = connectionDB.getConnection();
            // User.addUser(connection, "Otavio", "otavio@gmail.com", 2);
            // User.deleteUser(connection, "Otavio", "otavio@gmail.com");
            // System.out.println(User.getUserId(connection, "Otavio", "otavio@gmail.com"));
            /*User.getAllUsers(connection);
            User.editUserAttribute(connection, 3, "user_email", "otavina@gmail.com" );
            User.getAllUsers(connection);*/


        }

        catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }


    }
}