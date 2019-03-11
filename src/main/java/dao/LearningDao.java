package dao;

import model.Group;
import model.Subject;
import model.Learning;

import java.sql.*;
import java.util.ArrayList;

public class LearningDao extends Dao{

    public LearningDao(){
        super("learning", "learning_id");
        setMaxId();
    }

    public void insert(Learning learning){
        try(Connection connection = DriverManager.getConnection(connectionURL, userName, password)){
            PreparedStatement st = connection.prepareStatement("INSERT INTO learning VALUES (?, ?, ?, ?)");

            st.setLong(1, ++maxId);
            st.setLong(2, learning.getGroup().getId());
            st.setLong(3, learning.getSubject().getId());
            st.setFloat(4, learning.getCoefficient());

            st.execute();
            learning.setId(maxId);
        }catch(SQLException e){
            System.out.println("SQLException in LearningDao");
        }
    }

    public void update(Learning learning){
        try(Connection connection = DriverManager.getConnection(connectionURL, userName, password)){
            PreparedStatement st = connection.prepareStatement("UPDATE learning SET group_id = ?, " +
                    "subject_id = ?, learning_coefficient = ? WHERE learning_id = ?");

            st.setLong(1, learning.getGroup().getId());
            st.setLong(2, learning.getSubject().getId());
            st.setFloat(3, learning.getCoefficient());
            st.setLong(4, learning.getId());

            st.execute();
        }catch(SQLException e){
            System.out.println("SQLException in LearningDao");
        }
    }

    public ArrayList<Learning> select(ArrayList<Group> groups, ArrayList<Subject> subjects){
        try(Connection connection = DriverManager.getConnection(connectionURL, userName, password)){
            Statement st = connection.createStatement();

            ResultSet set = st.executeQuery("SELECT * FROM learning");
            ArrayList<Learning> result = new ArrayList<>();

            while(set.next()){

                long group_id = set.getLong(2), subject_id = set.getLong(3);
                Group group = null;
                for(Group gr : groups){
                    if(gr.getId() == group_id){
                        group = gr;
                        break;
                    }
                }
                Subject subject = null;
                for(Subject sub : subjects){
                    if(sub.getId() == subject_id){
                        subject = sub;
                        break;
                    }
                }

                Learning learning = new Learning(subject, group, set.getFloat(4));
                learning.setId(set.getLong(1));
                result.add(learning);
            }

            return result;
        }catch(SQLException e){
            System.out.println("SQLException in LearningDao");
        }
        return null;
    }
}