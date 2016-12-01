package studentbooking.db;

import java.sql.*;

/**
 * Created by leiyang on 2016/11/27.
 */
public class DBHelper {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;


    public DBHelper(){
        init();
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    public void setPreparedStatement(PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public void init(){
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection("jdbc:sqlserver://127.0.0.1:1433;DatabaseName=Ticket","sa","ly19960205");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void executeSQL(String sqlstr) {

        try {
            preparedStatement = connection.prepareStatement(sqlstr);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
