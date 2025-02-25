package services;

import exceptions.NotFoundException;
import exceptions.TimeException;

import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BorrowingService {




    public static void borrowBook(Connection connection, String bookName, String userName, String userEmail, LocalDate borrowingDate, LocalDate returnDate) throws SQLException {
        int availableBookId = getAvailableBook(connection, bookName);
        if(availableBookId == 0) {
            System.out.println("Book not found");
            return;
        }

        int userId = ConnectionDB.getUserId(connection, userName, userEmail);
        if(userId == 0) {
            System.out.println("User not found");
            return;
        }

        Map<String, Object> map = new LinkedHashMap<>();

        map.put("book_id", availableBookId);
        map.put("user_id", userId);
        map.put("borrowing_date", borrowingDate);
        if(returnDate != null){
            if(returnDate.isBefore(borrowingDate)) throw new TimeException("The Return Date cannot be Before the Borrowing Date");
            map.put("return_date", returnDate);
        }
        ConnectionDB.addRow("borrowings", map, connection);
        System.out.println(ConnectionDB.editRow(connection, "books", availableBookId, "state", "Borrowed"));

    }

    public static int getAvailableBook(Connection connection, String bookName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT book_id FROM books WHERE bookname = ? AND state = 'Available';");
        preparedStatement.setString(1, bookName);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()) return resultSet.getInt("book_id");
        System.out.println("Book not Found or not Available");
        return 0;
    }

    public static double calculateFine(Connection connection, int bookId) throws SQLException {
        double fine = 0;
        if(bookId == 0) throw new NotFoundException("Book not Found");

        int borrowing_id = ConnectionDB.getBorrowingId(connection, bookId);
        if(borrowing_id == 0) throw new NotFoundException("Borrowing not Found");

        List<Object> borrowings = ConnectionDB.getRowById(connection, "borrowings", borrowing_id);
        if(borrowings.get(4) == null) throw new TimeException("You only can calculate the fine when the book got returned!");
        int days = Period.between(((Date) borrowings.get(3)).toLocalDate(), ((Date) borrowings.get(4)).toLocalDate()).getDays();
        if(days > 7){
            fine += days * 2;
        }
        return fine;

    }

    public static void returnBookAndPayFine(Connection connection, int bookId) throws SQLException {
        double fine = calculateFine(connection, bookId);
        if(fine == 0){
            System.out.println("There is no fine on the loan!");
        }
        else{
            System.out.println("Paying " + fine + " on the Fine!");
        }

        // TODO ISSO NÂO FAZ SENTIDO
        // talvez uma verificação pra dar edit row so se o delete row der certo
        ConnectionDB.deleteRow(connection, "borrowings", ConnectionDB.getBorrowingId(connection, bookId));
        System.out.println(ConnectionDB.editRow(connection, "books", bookId, "state", "Available"));
    }


    public static void getALlBorrowings(Connection connection) throws SQLException {
        String query = "SELECT bo.borrowing_id, u.username, bk.bookname, bk.state FROM borrowings bo LEFT JOIN books bk ON bo.book_id = bk.book_id LEFT JOIN users u ON bo.user_id = u.user_id";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
            System.out.print(resultSetMetaData.getColumnName(i) + " ");
        }
        while (resultSet.next()) {
            System.out.println();
            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                System.out.print(resultSet.getString(i) + " ");
            }
        }
    }
}
