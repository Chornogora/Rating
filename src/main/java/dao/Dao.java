package dao;


import model.ModelObject;

import java.sql.*;

public abstract class Dao {
    protected static final String connectionURL = "jdbc:postgresql://localhost:5432/rating";
    protected static final String userName = "postgres";
    protected static final String password = "";

    protected final String tableName;
    protected final String idFieldName;
    protected long maxId;

    protected Dao(String tableName, String idFieldName) {
        this.tableName = tableName;
        this.idFieldName = idFieldName;
    }

    public static boolean isConnection(){
        try(Connection connection = DriverManager.getConnection(connectionURL, userName, password)){
            return true;
        }catch(SQLException e){
            return false;
        }
    }

    protected boolean delete(ModelObject object){
        try(Connection connection = DriverManager.getConnection(connectionURL, userName, password)){
            Statement st = connection.createStatement();

            return st.execute(String.format("DELETE FROM %s WHERE %s = %s", tableName, idFieldName, object.getId()));

        }catch(SQLException e){
            return false;
        }
    }

    protected void setMaxId(){
        try(Connection connection = DriverManager.getConnection(connectionURL, userName, password)){
            Statement st = connection.createStatement();

            ResultSet set = st.executeQuery(String.format("SELECT MAX(%s) FROM %s", idFieldName, tableName));
            set.next();
            maxId = set.getLong(1);
        }catch(SQLException e){
            System.out.println("SQLException in GroupDao");
        }
    }
}
