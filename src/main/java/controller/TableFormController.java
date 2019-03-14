package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;
import dao.*;
import model.representation.ProgressResult;
import model.representation.Rating;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TableFormController implements Initializable {

    @FXML
    private ComboBox<String> GroupChoice;

    @FXML
    private ComboBox<String> GroupChoice2;

    @FXML
    private ComboBox<String> StudentChoice;

    @FXML
    private TableView<Rating> RatingTable;

    @FXML
    private TableView SSTable;

    private Model model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new Model();

        GroupDao gdao = new GroupDao();
        ArrayList<Group> glist = gdao.select();
        model.setGroups(glist);

        SubjectDao subdao = new SubjectDao();
        ArrayList<Subject> sublist = subdao.select();
        model.setSubjects(sublist);

        StudentDao sdao = new StudentDao();
        ArrayList<Student> slist = sdao.select(glist);
        model.setStudents(slist);

        LearningDao ldao = new LearningDao();
        ArrayList<Learning> llist = ldao.select(glist, sublist);
        model.setLearnings(llist);

        ProgressDao pdao = new ProgressDao();
        ArrayList<Progress> plist = pdao.select(slist, llist);
        model.setProgresses(plist);

        model.sortStudentByRating();

        String[] names = model.getGroupNames();
        String[] values = new String[names.length+1];
        values[0]="Усi групи";
        System.arraycopy(names, 0, values, 1, names.length);
        GroupChoice.setItems(FXCollections.observableArrayList(values));
        GroupChoice2.setItems(FXCollections.observableArrayList(values));
        StudentChoice.setItems(FXCollections.observableArrayList(model.getStudentNames()));

        TableColumn<Rating, Long> col = (TableColumn<Rating, Long>) RatingTable.getColumns().get(0);
        col.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        TableColumn<Rating, Integer> col2 = (TableColumn<Rating, Integer>) RatingTable.getColumns().get(1);
        col2.setCellValueFactory(new PropertyValueFactory<>("studentPlace"));
        TableColumn<Rating, String> col3 = (TableColumn<Rating, String>) RatingTable.getColumns().get(2);
        col3.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        TableColumn<Rating, String> col4 = (TableColumn<Rating, String>) RatingTable.getColumns().get(3);
        col4.setCellValueFactory(new PropertyValueFactory<>("groupName"));
        TableColumn<Rating, Float> col5 = (TableColumn<Rating, Float>) RatingTable.getColumns().get(4);
        col5.setCellValueFactory(new PropertyValueFactory<>("points"));

        TableColumn<Rating, Long> sscol = (TableColumn<Rating, Long>) SSTable.getColumns().get(0);
        sscol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        TableColumn<Rating, String> sscol2 = (TableColumn<Rating, String>) SSTable.getColumns().get(1);
        sscol2.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        TableColumn<Rating, String> sscol3 = (TableColumn<Rating, String>) SSTable.getColumns().get(2);
        sscol3.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        TableColumn<Rating, Float> sscol4 = (TableColumn<Rating, Float>) SSTable.getColumns().get(3);
        sscol4.setCellValueFactory(new PropertyValueFactory<>("points"));

        GroupChoice.setValue("Усi групи");
        GroupChoice2.setValue("ПЗПИ-17-1");
        StudentChoice.setValue("Усi студенти");
        setRatingGroup();
        setGroup();
    }

    @FXML
    private void setRatingGroup(){
        model.sortStudentByRating();

        ArrayList<Student> students;
        if(GroupChoice.getValue().equals("Усi групи"))
            students = model.getStudents();
        else {
            Group group = model.getGroupByName(GroupChoice.getValue());
            students = group.getStudents();
        }

        ObservableList<Rating> usersData = FXCollections.observableArrayList();
        for(int i = 1; i <= students.size(); ++i){
            Student st = students.get(i-1);
            usersData.add(new Rating(st, model.getStudentRating(st), st.getAdditionalPoints(), model.getStudents().indexOf(st)+1));
        }
        RatingTable.setItems(usersData);
    }

    @FXML
    private void setGroup(){
        ObservableList<String> variants = FXCollections.observableArrayList();
        variants.add("Усi студенти");
        if(GroupChoice2.getValue().equals("Усi групи")){
            variants.addAll(model.getStudentNames());
        }else{
            variants.addAll(model.getGroupByName(GroupChoice2.getValue()).getStudentNames());
        }
        StudentChoice.setItems(variants);
        StudentChoice.setValue("Усi студенти");
        setStudentChoice();
    }

    @FXML
    private void setStudentChoice(){
        model.sortStudentByRating();

        ArrayList<Student> students;
        String gc2value = GroupChoice2.getValue();
        if(gc2value.equals("Усi групи"))
            students = model.getStudents();
        else {
            Group group = model.getGroupByName(GroupChoice2.getValue());
            students = group.getStudents();
        }
        if(!StudentChoice.getValue().equals("Усi студенти")){
            students = new ArrayList<Student>();
            students.add(model.getStudentByName(StudentChoice.getValue()));
        }

        ObservableList<ProgressResult> usersData = FXCollections.observableArrayList();
        for(int i = 1; i <= students.size(); ++i){
            Student st = students.get(i-1);
            ArrayList<Progress> stpr = model.getStudentProgresses(st);
            for(Progress pr : stpr)
            usersData.add(new ProgressResult(st, pr.getLearning().getSubject().getName(), pr.getProgressPoints()));
        }
        SSTable.setItems(usersData);
    }

}
