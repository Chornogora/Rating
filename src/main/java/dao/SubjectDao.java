package dao;

import model.Subject;

import java.sql.*;
import java.util.ArrayList;

public class SubjectDao extends Dao {

    public SubjectDao(){
        super("subjects", "subject_id");
        setMaxId();
    }

    public void insert(Subject subject){
        try(Connection connection = DriverManager.getConnection(connectionURL, userName, password)){
            PreparedStatement st = connection.prepareStatement("INSERT INTO subjects VALUES (?, ?)");

            st.setLong(1, ++maxId);
            st.setString(2, subject.getName());

            st.execute();
            subject.setId(maxId);
        }catch(SQLException e){
            System.out.println("SQLException in SubjectDao");
        }
    }

    public void update(Subject subject){
        try(Connection connection = DriverManager.getConnection(connectionURL, userName, password)){
            PreparedStatement st = connection.prepareStatement("UPDATE subjects SET subject_name = ? WHERE subject_id = ?");

            st.setString(1, subject.getName());
            st.setLong(2, subject.getId());

            st.execute();
        }catch(SQLException e){
            System.out.println("SQLException in SubjectDao");
        }
    }

    public ArrayList<Subject> select(){
        try(Connection connection = DriverManager.getConnection(connectionURL, userName, password)){
            Statement st = connection.createStatement();

            ResultSet set = st.executeQuery("SELECT * FROM subjects");
            ArrayList<Subject> result = new ArrayList<>();

            while(set.next()){
                Subject subject = new Subject(set.getString(2));
                subject.setId(set.getLong(1));
                result.add(subject);
            }

            return result;
        }catch(SQLException e){
            System.out.println("SQLException in SubjectDao");
        }
        return null;
    }
}
