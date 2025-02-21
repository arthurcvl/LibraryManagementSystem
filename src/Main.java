import services.BorrowingService;
import services.ConnectionDB;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
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


//            Map<String,String> map = new LinkedHashMap<>();
//            map.put("n", "arthur\n");
//            map.put("b", "sobrenome");    
//
//            ConnectionDB.addRow("users", map, connection);

//            System.out.println(ConnectionDB.getBorrowingId(connection, 2));

            //ConnectionDB.deleteRow(connection, "borrowings", ConnectionDB.getBorrowingId(connection, 2));
            //BorrowingService.borrowBook(connection, "LIVRO MAGICO", "Arthur", "arthurmail", LocalDate.of(2025, 1, 20), LocalDate.of(2025, 1, 30));
            double v = BorrowingService.calculateFine(connection, 2);
            System.out.println(v);
            //TODO pensar se a classe ConnectionDB e seus metodos devem na realidade ser não estaticos

            //BorrowingService.borrowBook(connection, "LIVRO MAGICO", "Otavio", "otavinlol@gmail.com", LocalDate.of(2025, 1, 28), LocalDate.of(2025, 1, 29));


            connection.close();
        }

        catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }



    }
}


