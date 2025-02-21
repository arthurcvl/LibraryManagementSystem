package services;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

public class ConnectionDB {

    Connection connection;

    public ConnectionDB(String url, String user, String password) throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
    }

    public Connection getConnection() {
        return connection;
    }

    public static String getAll(String table, Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table);
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
        return "";
    }

    public static List<Object> getRowById(Connection connection, String table, int id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table + " WHERE "
                + table.substring(0, table.length() -1) + "_id = " + id);
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        List<Object> list = new ArrayList<>();
        if(resultSet.next()){
            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                list.add(resultSet.getObject(i));
            }
        }
        return list;
    }

    public static <T, Z> void addRow(String table, Map<T, Z> map, Connection connection) throws SQLException {
        StringBuilder columnsBuilder = new StringBuilder();
        map.keySet().forEach(key -> columnsBuilder.append(key).append(","));
        String columns = columnsBuilder.substring(0, columnsBuilder.length() - 1);

        AtomicReference<String> statement = new AtomicReference<>("INSERT INTO " + table + "(" + columns + ") VALUES(");

        // refatorar a parte do stringbuilder ali em cima e a parte de baixo pra usar stream em tudo
        IntStream.rangeClosed(1, map.keySet().size())
                .mapToObj(i -> "?")
                .forEach(s -> statement.updateAndGet(stat -> stat += s + ","));


        PreparedStatement preparedStatement = connection.prepareStatement(statement.updateAndGet(s -> s = s.substring(0, s.length() - 1) + ")"));

        int i = 1;
        for(T key: map.keySet()){
            preparedStatement.setObject(i, map.get(key));
            i ++;

        }
        int j = preparedStatement.executeUpdate();
        if(j > 0) System.out.println("Row added with sucess in table: " + table);
        else System.out.println("Add Row failed");
    }

    public static void deleteRow(Connection connection, String table, int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM " + table + " WHERE " + table.substring(0, table.length() - 1) + "_id = ?;");
        preparedStatement.setInt(1, id);
        int i = preparedStatement.executeUpdate();
        if(i > 0) System.out.println("Row in " + table + " deleted with sucess!");
        else System.out.println("Delete Row failed");
    }

    public static <T> String editRow(Connection connection, String table, int id, String editAttribute, T newValue) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE " + table + " SET " + editAttribute + " = ? WHERE " + table.substring(0, table.length() - 1) +"_id = ?;");
        preparedStatement.setObject(1, newValue);
        preparedStatement.setInt(2, id);

        int i = preparedStatement.executeUpdate();
        if(i > 0 ) return table + " " + editAttribute + "Updated with Success";
        return editAttribute + " Update Failed";
    }

    public static String getBookId(Connection connection, String name){
        return "ni";
    }

    public static int getUserId(Connection connection, String username, String userEmail) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT user_id FROM users WHERE users.username = ? AND users.user_email = ?;");
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, userEmail);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()) return resultSet.getInt("user_id");
        return 0;

    }

    public static int getBorrowingId(Connection connection, int bookId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT borrowing_id FROM borrowings WHERE book_id = ?");
        preparedStatement.setInt(1, bookId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()) return resultSet.getInt("borrowing_id");
        return 0;
    }

    //TODO ver se nos lugares que eu retorno string usar optional<String> é uma boa

    // no add Borrow colocar um que se voce so der enter na data de devolução ou na data de emprestimo ela vai ser contabilizada
    // como hoje, alem disso a verificação de se as datas batem uma com a outra vai ocorrer no programa de java msm
    // calculo da multa tambem vai ser no java tbm
    // na data se o return date for null é porque ainda não foi devolvido

    //TODO Fazer um join left join sei la pra colocar no lugar do user and books id na tabela de borrowing o nome do user e do book
}
