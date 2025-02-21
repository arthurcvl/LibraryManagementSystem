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
        if(availableBookId == 0) return;

        int userId = ConnectionDB.getUserId(connection, userName, userEmail);
        if(userId == 0) return;

        Map<String, Object> map = new LinkedHashMap<>();

        map.put("book_id", availableBookId);
        map.put("user_id", userId);
        map.put("borrowing_date", borrowingDate);
        if(returnDate != null){
            if(returnDate.isBefore(borrowingDate)) throw new TimeException("The Return Date cannot be Before the Borrowing Date");
            map.put("return_date", returnDate);
        }
        ConnectionDB.addRow("borrowings", map, connection);
        ConnectionDB.editRow(connection, "books", availableBookId, "state", "Borrowed");


    }

    public static int getAvailableBook(Connection connection, String bookName) throws SQLException {
        int book_id = 0;
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT book_id FROM books WHERE bookname = ? AND state = 'Available';");
        preparedStatement.setString(1, bookName);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()) return resultSet.getInt("book_id");
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

    public static void printALlBorrowings(Connection connection){
        // USE inner join to print all the borrowings using the relation of the foreign keys to get the bookname and username
    }
}
