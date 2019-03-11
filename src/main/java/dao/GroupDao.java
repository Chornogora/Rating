package dao;

import model.Group;

import java.sql.*;
import java.util.ArrayList;

public class GroupDao extends Dao {

    public GroupDao(){
        super("groups", "group_id");
        setMaxId();
    }

    public void insert(Group group){
        try(Connection connection = DriverManager.getConnection(connectionURL, userName, password)){
            PreparedStatement st = connection.prepareStatement("INSERT INTO groups VALUES (?, ?)");

            st.setLong(1, ++maxId);
            st.setString(2, group.getName());

            st.execute();
            group.setId(maxId);
        }catch(SQLException e){
            System.out.println("SQLException in GroupDao");
        }
    }

    public void update(Group group){
        try(Connection connection = DriverManager.getConnection(connectionURL, userName, password)){
            PreparedStatement st = connection.prepareStatement("UPDATE groups SET group_name = ? WHERE group_id = ?");

            st.setString(1, group.getName());
            st.setLong(2, group.getId());

            st.execute();
        }catch(SQLException e){
            System.out.println("SQLException in GroupDao");
        }
    }

    public ArrayList<Group> select(){
        try(Connection connection = DriverManager.getConnection(connectionURL, userName, password)){
            Statement st = connection.createStatement();

            ResultSet set = st.executeQuery("SELECT * FROM groups");
            ArrayList<Group> result = new ArrayList<>();

            while(set.next()){
                Group group = new Group(set.getString(2));
                group.setId(set.getLong(1));
                result.add(group);
            }

            return result;
        }catch(SQLException e){
            System.out.println("SQLException in GroupDao");
        }
        return null;
    }
}