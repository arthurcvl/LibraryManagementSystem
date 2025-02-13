import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
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
            User.editUserAttribute(connection, 3, "user_email", "otavina@gmail.com" ); */
            User.getAllUsers(connection);

            //TODO acho que a User class com o CRUD ficaria muito parecido com a BOOK class e seu CRUD então talvez de pra fazer uma classe que sirva para os dois n sei
            //TODO pensar sobre isso bastante acho que interface classe abstrata essas coisas não resolveriam ou talvez sim pra fazer alguns metodos default mas sla

            Map<String, Object> map = new LinkedHashMap<>();
            map.put("username", "Lucas");
            map.put("user_email", "lucas@outloook.com");
            map.put("telephone", 155);
            ConnectionDB.addRow("users", map, connection);
            User.getAllUsers(connection);


        }

        catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }


    }
}