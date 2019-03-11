package dao;

import model.Group;
import model.Student;

import java.sql.*;
import java.util.ArrayList;

public class StudentDao extends Dao {
    public StudentDao(){
        super("students", "student_id");
        setMaxId();
    }

    public void insert(Student student){
        try(Connection connection = DriverManager.getConnection(connectionURL, userName, password)){
            PreparedStatement st = connection.prepareStatement("INSERT INTO students VALUES (?, ?, ?, ?)");

            st.setLong(1, ++maxId);
            st.setLong(2, student.getGroup().getId());
            st.setString(3, student.getName());
            st.setFloat(4, student.getAdditionalPoints());

            st.execute();
            student.setId(maxId);
        }catch(SQLException e){
            System.out.println("SQLException in StudentDao");
        }
    }

    public void update(Student student){
        try(Connection connection = DriverManager.getConnection(connectionURL, userName, password)){
            PreparedStatement st = connection.prepareStatement("UPDATE students SET group_id = ?, " +
                    "student_name = ?, student_additional_points = ? WHERE student_id = ?");

            st.setLong(1, student.getGroup().getId());
            st.setString(2, student.getName());
            st.setFloat(3, student.getAdditionalPoints());
            st.setLong(4, student.getId());

            st.execute();
        }catch(SQLException e){
            System.out.println("SQLException in StudentDao");
        }
    }

    public ArrayList<Student> select(ArrayList<Group> groups){
        try(Connection connection = DriverManager.getConnection(connectionURL, userName, password)){
            Statement st = connection.createStatement();

            ResultSet set = st.executeQuery("SELECT * FROM students");
            ArrayList<Student> result = new ArrayList<>();

            while(set.next()){
                long group_id = set.getLong(2);
                for(Group group : groups){
                    if(group.getId() == group_id){
                        Student student = new Student(group, set.getString(3), set.getFloat(4));
                        student.setId(set.getLong(1));
                        result.add(student);
                    }
                }
            }

            return result;
        }catch(SQLException e){
            System.out.println("SQLException in StudentDao");
        }
        return null;
    }
}
