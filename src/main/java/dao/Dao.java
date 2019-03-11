package dao;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Dao {
    protected static final String connectionURL = "jdbc:postgresql://localhost:5432/rating";
    protected static final String userName = "postgres";
    protected static final String password = "";

    public static boolean isConnection(){
        try(Connection connection = DriverManager.getConnection(connectionURL, userName, password)){
            return true;
        }catch(SQLException e){
            return false;
        }
    }
}
