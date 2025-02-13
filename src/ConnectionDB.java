import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ConnectionDB {

    Connection connection;

    public ConnectionDB(String url, String user, String password) throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
    }

    public Connection getConnection() {
        return connection;
    }

    public void printAll(String table) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table);
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        for(int i = 1; i <= resultSetMetaData.getColumnCount(); i++){
            System.out.print(resultSetMetaData.getColumnName(i) + " ");
        }
        while(resultSet.next()){
            System.out.println();
            for(int i = 1; i <= resultSetMetaData.getColumnCount(); i++){
                System.out.print(resultSet.getString(i) + " ");
            }
        }

    }
    public <T> int insertAttribute(String table, String column, T t) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO " + table + "(" + column + ") VALUES(?);");
        preparedStatement.setObject(1, t);
        return preparedStatement.executeUpdate();
    }

    public static <T, Z> void addRow(String table, Map<T, Z> map, Connection connection) throws SQLException {
        String columns = "";
        int l = 1;
        for(T key : map.keySet()) {
            if(l != map.size()) {
                columns = columns + key.toString() + ",";
                l++;
            }
            else{
                columns = columns + key.toString();
            }
        }
        System.out.println(columns);
        String statement = "INSERT INTO " + table + "(" + columns + ") VALUES(";
        for(int i = 1; i <= map.size(); i++){
            if(i != map.size()){
                statement = statement + "?,";
            }
            //TODO eu tenho certeza que eu não preciso usar esse else e posso so usar else ali no if na mesma linha tb talvez
            else {
                statement = statement + "?)";
            }
        }
        System.out.println(statement);
        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        int i = 1;
        for(T key: map.keySet()){
            preparedStatement.setObject(i, map.get(key));
            i ++;
        }
        int j = preparedStatement.executeUpdate();
        if(i > 0) System.out.println("Row added with sucess in table: " + table);
        else System.out.println("Add Row failed");
    }

    // public <T> void filter(Predicate predicate){return;}

    //TODO um edit que o user pode por o where dele sla

    // no add Borrow colocar um que se voce so der enter na data de devolução ou na data de emprestimo ela vai ser contabilizada
    // como hoje, alem disso a verificação de se as datas batem uma com a outra vai ocorrer no programa de java msm
    // calculo da multa tambem vai ser no java tbm
}
