package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import model.*;
import dao.*;
import model.representation.ProgressResult;
import model.representation.Rating;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.*;

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

    @FXML
    private Pane SubPointsPane;

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
        GroupChoice.setItems(observableArrayList(values));
        GroupChoice2.setItems(observableArrayList(values));
        StudentChoice.setItems(observableArrayList(model.getStudentNames()));

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

        ComboBox<String> box = (ComboBox) SubPointsPane.lookup("#SubPointsStudent");
        box.setItems(observableArrayList(model.getStudentNames()));
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

        ObservableList<Rating> usersData = observableArrayList();
        for(int i = 1; i <= students.size(); ++i){
            Student st = students.get(i-1);
            usersData.add(new Rating(st, model.getStudentRating(st), st.getAdditionalPoints(), model.getStudents().indexOf(st)+1));
        }
        RatingTable.setItems(usersData);
    }

    @FXML
    private void setGroup(){
        ObservableList<String> variants = observableArrayList();
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

        ObservableList<ProgressResult> usersData = observableArrayList();
        for(int i = 1; i <= students.size(); ++i){
            Student st = students.get(i-1);
            ArrayList<Progress> stpr = model.getStudentProgresses(st);
            for(Progress pr : stpr)
            usersData.add(new ProgressResult(st, pr.getLearning().getSubject().getName(), pr.getProgressPoints()));
        }
        SSTable.setItems(usersData);
    }

    //<Установка предметного балла>

    @FXML
    private void SubjectChoiceEnable(){
        TextField points = (TextField) SubPointsPane.lookup("#SubPointsPoint");
        Button button = (Button) SubPointsPane.lookup("#SubPointsButton");
        points.setDisable(true);
        points.setText("Предметний бал");
        button.setDisable(true);

        ComboBox box = (ComboBox) SubPointsPane.lookup("#SubPointsStudent");
        ComboBox subject = (ComboBox) SubPointsPane.lookup("#SubPointsSubject");

        subject.setDisable(false);
        subject.setValue("Обрати предмет");
        ObservableList<String> variants = observableArrayList();
        List<Subject> list = model.getStudentsSubjects(model.getStudentByName((String)box.getValue()));

        for(Subject sub : list){
            variants.add(sub.getName());
        }
        subject.setItems(variants);
    }

    @FXML
    private void PointsChoiceEnable(){
        TextField points = (TextField) SubPointsPane.lookup("#SubPointsPoint");
        Button button = (Button) SubPointsPane.lookup("#SubPointsButton");
        points.setDisable(false);
        button.setDisable(false);
    }

    @FXML
    private void setSubjectPoints(){
        ComboBox box = (ComboBox) SubPointsPane.lookup("#SubPointsStudent");
        ComboBox subject = (ComboBox) SubPointsPane.lookup("#SubPointsSubject");
        TextField points = (TextField) SubPointsPane.lookup("#SubPointsPoint");

        int point = 0;
        try {
            point = Integer.valueOf(points.getText());
            if(point < 0 || point > 100)
                throw new Exception("Неверный формат данных");
        }catch(Exception e){
            e.printStackTrace();
            throwError("Невірно введений бал. Допустимі цілі значення від 0 до 100.");
            return;
        }

        String studentName = (String) box.getValue(), subjectName = (String)subject.getValue();
        Progress progress = model.getProgressByData(model.getStudentByName(studentName), model.getSubjectByName(subjectName));
        progress.setProgressPoints(point);
        new ProgressDao().update(progress);

        refreshTableViews();
    }
    //</Установка предметного балла>

    private void refreshTableViews(){
        setRatingGroup();
        setGroup();
    }

    //<Ошибки и предупреждения>

    private void throwError(String text){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Помилка");
        alert.setContentText(text);
        alert.showAndWait();
    }

    private void throwWarning(String text){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Попередження");
        alert.setContentText(text);
        alert.showAndWait();
    }

    //</Ошибки и предупреждения>
}
