package dao;

import model.*;

import java.sql.*;
import java.util.ArrayList;

public class ProgressDao extends Dao{
    public ProgressDao(){
        super("progress", "progress_id");
        setMaxId();
    }

    public void insert(Progress progress){
        try(Connection connection = DriverManager.getConnection(connectionURL, userName, password)){
            PreparedStatement st = connection.prepareStatement("INSERT INTO progress VALUES (?, ?, ?, ?)");

            st.setLong(1, ++maxId);
            st.setLong(2, progress.getStudent().getId());
            st.setLong(3, progress.getLearning().getId());
            st.setInt(4, progress.getProgressPoints());

            st.execute();
            progress.setId(maxId);
        }catch(SQLException e){
            System.out.println("SQLException in ProgressDao");
        }
    }

    public void update(Progress progress){
        try(Connection connection = DriverManager.getConnection(connectionURL, userName, password)){
            PreparedStatement st = connection.prepareStatement("UPDATE progress SET student_id = ?, " +
                    "learning_id = ?, progress_points = ? WHERE progress_id = ?");

            st.setLong(1, progress.getStudent().getId());
            st.setLong(2, progress.getLearning().getId());
            st.setInt(3, progress.getProgressPoints());
            st.setLong(4, progress.getId());

            st.execute();
        }catch(SQLException e){
            System.out.println("SQLException in ProgressDao");
        }
    }

    public ArrayList<Progress> select(ArrayList<Student> groups, ArrayList<Learning> learnings){
        try(Connection connection = DriverManager.getConnection(connectionURL, userName, password)){
            Statement st = connection.createStatement();

            ResultSet set = st.executeQuery("SELECT * FROM progress");
            ArrayList<Progress> result = new ArrayList<>();

            while(set.next()){

                long student_id = set.getLong(2), learning_id = set.getLong(3);
                Student student = null;
                for(Student stud : groups){
                    if(stud.getId() == student_id){
                        student = stud;
                        break;
                    }
                }
                Learning learning = null;
                for(Learning learn : learnings){
                    if(learn.getId() == learning_id){
                        learning = learn;
                        break;
                    }
                }

                Progress progress = new Progress(student, learning, set.getInt(4));
                learning.setId(set.getLong(1));
                result.add(progress);
            }

            return result;
        }catch(SQLException e){
            System.out.println("SQLException in ProgressDao");
        }
        return null;
    }

    public static void main(String[] args) {
        GroupDao gdao = new GroupDao();
        ArrayList<Group> glist = gdao.select();
        StudentDao sdao = new StudentDao();
        ArrayList<Student> slist = sdao.select(glist);
        SubjectDao subdao = new SubjectDao();
        ArrayList<Subject> sublist = subdao.select();
        LearningDao ldao = new LearningDao();
        ArrayList<Learning> llist = ldao.select(glist, sublist);
        ProgressDao pdao = new ProgressDao();
        ArrayList<Progress> plist = pdao.select(slist, llist);
        System.out.println(plist.size());
    }
}
