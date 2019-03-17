package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import model.*;
import dao.*;
import representation.*;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

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
    private TableView<ProgressResult> SSTable;

    @FXML
    private TableView<SubjectDistribution> DistribTable;

    @FXML
    private TableView<Rating> StGroups;

    @FXML
    private Pane SubPointsPane;

    @FXML
    private Pane GroupPane;

    @FXML
    private Pane SubjectPane;

    @FXML
    private Pane LearningPane;

    @FXML
    private Pane StudentAddPane;

    @FXML
    private Pane editStudentPane;

    private Model model;

    private boolean isStudentEditing = false;

    private Student changingStudent;

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
        String[] values = new String[names.length + 1];
        values[0] = "Усi групи";
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

        TableColumn<ProgressResult, Long> sscol = (TableColumn<ProgressResult, Long>) SSTable.getColumns().get(0);
        sscol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        TableColumn<ProgressResult, String> sscol2 = (TableColumn<ProgressResult, String>) SSTable.getColumns().get(1);
        sscol2.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        TableColumn<ProgressResult, String> sscol3 = (TableColumn<ProgressResult, String>) SSTable.getColumns().get(2);
        sscol3.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        TableColumn<ProgressResult, Integer> sscol4 = (TableColumn<ProgressResult, Integer>) SSTable.getColumns().get(3);
        sscol4.setCellValueFactory(new PropertyValueFactory<>("points"));

        TableColumn<SubjectDistribution, String> dcol = (TableColumn<SubjectDistribution, String>) DistribTable.getColumns().get(0);
        dcol.setCellValueFactory(new PropertyValueFactory<>("groupName"));
        TableColumn<SubjectDistribution, String> dcol2 = (TableColumn<SubjectDistribution, String>) DistribTable.getColumns().get(1);
        dcol2.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        TableColumn<SubjectDistribution, Float> dcol3 = (TableColumn<SubjectDistribution, Float>) DistribTable.getColumns().get(2);
        dcol3.setCellValueFactory(new PropertyValueFactory<>("coefficient"));
        TableColumn<SubjectDistribution, Button> dcol4 = (TableColumn<SubjectDistribution, Button>) DistribTable.getColumns().get(3);
        dcol4.setCellValueFactory(new PropertyValueFactory<>("delButton"));

        TableColumn<Rating, Long> stgcol = (TableColumn<Rating, Long>) StGroups.getColumns().get(0);
        stgcol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        TableColumn<Rating, String> stgcol2 = (TableColumn<Rating, String>) StGroups.getColumns().get(1);
        stgcol2.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        TableColumn<Rating, String> stgcol3 = (TableColumn<Rating, String>) StGroups.getColumns().get(2);
        stgcol3.setCellValueFactory(new PropertyValueFactory<>("groupName"));
        TableColumn<Rating, Float> stgcol4 = (TableColumn<Rating, Float>) StGroups.getColumns().get(3);
        stgcol4.setCellValueFactory(new PropertyValueFactory<>("additionalPoints"));
        TableColumn<Rating, Button> stgcol5 = (TableColumn<Rating, Button>) StGroups.getColumns().get(4);
        stgcol5.setCellValueFactory(new PropertyValueFactory<>("delButton"));
        TableColumn<Rating, Button> stgcol6 = (TableColumn<Rating, Button>) StGroups.getColumns().get(5);
        stgcol6.setCellValueFactory(new PropertyValueFactory<>("editButton"));

        GroupChoice.setValue("Усi групи");
        GroupChoice2.setValue("Усi групи");
        StudentChoice.setValue("Усi студенти");

        refreshData();
    }

    @FXML
    private void setRatingGroup() {
        model.sortStudentByRating();

        ArrayList<Student> students;
        if (GroupChoice.getValue() == null || GroupChoice.getValue().equals("Усi групи"))
            students = model.getStudents();
        else {
            Group group = model.getGroupByName(GroupChoice.getValue());
            students = group.getStudents();
        }

        ObservableList<Rating> usersData = observableArrayList();
        for (int i = 1; i <= students.size(); ++i) {
            Student st = students.get(i - 1);
            usersData.add(new Rating(st, model.getStudentRating(st), model.getStudents().indexOf(st) + 1));
        }
        RatingTable.setItems(usersData);
    }

    @FXML
    private void setGroup() {
        ObservableList<String> variants = observableArrayList();
        variants.add("Усi студенти");
        String value = GroupChoice2.getValue();
        if (value == null || value.equals("Усi групи")) {
            variants.addAll(model.getStudentNames());
        } else {
            variants.addAll(model.getGroupByName(GroupChoice2.getValue()).getStudentNames());
        }
        StudentChoice.setItems(variants);
        StudentChoice.setValue("Усi студенти");
        setStudentChoice();
    }

    @FXML
    private void setStudentChoice() {
        model.sortStudentByRating();

        ArrayList<Student> students;
        String gc2value = GroupChoice2.getValue();
        if (gc2value == null || gc2value.equals("Усi групи"))
            students = model.getStudents();
        else {
            Group group = model.getGroupByName(GroupChoice2.getValue());
            students = group.getStudents();
        }
        if (StudentChoice.getValue() != null && !StudentChoice.getValue().equals("Усi студенти")) {
            students = new ArrayList<>();
            students.add(model.getStudentByName(StudentChoice.getValue()));
        }

        ObservableList<ProgressResult> usersData = observableArrayList();
        for (int i = 1; i <= students.size(); ++i) {
            Student st = students.get(i - 1);
            ArrayList<Progress> stpr = model.getStudentProgresses(st);
            for (Progress pr : stpr)
                usersData.add(new ProgressResult(st, pr.getLearning().getSubject().getName(), pr.getProgressPoints()));
        }
        SSTable.setItems(usersData);
    }

    //<Установка предметного балла>

    @FXML
    private void SubjectChoiceEnable() {
        TextField points = (TextField) SubPointsPane.lookup("#SubPointsPoint");
        Button button = (Button) SubPointsPane.lookup("#SubPointsButton");
        points.setDisable(true);
        points.setText("Предметний бал");
        button.setDisable(true);

        ComboBox<String> box = (ComboBox<String>) SubPointsPane.lookup("#SubPointsStudent");
        ComboBox<String> subject = (ComboBox<String>) SubPointsPane.lookup("#SubPointsSubject");

        subject.setValue("Обрати предмет");
        subject.setDisable(false);

        ObservableList<String> variants = observableArrayList();
        List<Subject> list = model.getStudentsSubjects(model.getStudentByName(box.getValue()));

        for (Subject sub : list) {
            variants.add(sub.getName());
        }
        subject.setItems(variants);
    }

    @FXML
    private void PointsChoiceEnable() {
        TextField points = (TextField) SubPointsPane.lookup("#SubPointsPoint");
        Button button = (Button) SubPointsPane.lookup("#SubPointsButton");
        points.setDisable(false);
        button.setDisable(false);
    }

    @FXML
    private void setSubjectPoints() {
        ComboBox box = (ComboBox) SubPointsPane.lookup("#SubPointsStudent");
        ComboBox subject = (ComboBox) SubPointsPane.lookup("#SubPointsSubject");
        TextField points = (TextField) SubPointsPane.lookup("#SubPointsPoint");

        int point;
        try {
            point = Integer.valueOf(points.getText());
            if (point < 0 || point > 100)
                throw new Exception("Неверный формат данных");
        } catch (Exception e) {
            e.printStackTrace();
            throwError("Невірно введений бал!");
            return;
        }

        String studentName = (String) box.getValue(), subjectName = (String) subject.getValue();
        Progress progress = model.getProgressByData(model.getStudentByName(studentName), model.getSubjectByName(subjectName));
        progress.setProgressPoints(point);
        new ProgressDao().update(progress);

        refreshTableViews();
    }
    //</Установка предметного балла>

    //<Работа с группами>

    private String getNewGroupName() {
        TextField groupName = (TextField) GroupPane.lookup("#GroupName");
        return groupName.getText();
    }

    private String getSelectedGroupName() {
        ComboBox<String> groupBox = (ComboBox<String>) GroupPane.lookup("#GroupBox");
        return groupBox.getValue();
    }

    @FXML
    private void addGroup() {
        String newGroupName = getNewGroupName();

        if (model.getGroupByName(newGroupName) != null) {
            throwError("Назва групи не є унікальною!");
            return;
        } else if (newGroupName.trim().equals("")) {
            throwError("Неправильна назва групи!");
            return;
        }

        Group group = new Group(newGroupName);
        model.getGroups().add(group);
        new GroupDao().insert(group);

        refreshData();
    }

    @FXML
    private void changeGroup() {

        Group group = model.getGroupByName(getSelectedGroupName());
        if (group == null) {
            throwError("Не обрана група для змiни назви!");
        }

        String newGroupName = getNewGroupName();

        if (model.getGroupByName(newGroupName) != null) {
            throwError("Назва групи не є унікальною!");
            return;
        }

        group.setName(newGroupName);
        new GroupDao().update(group);

        refreshData();
    }

    @FXML
    private void deleteGroup() {
        Group group = model.getGroupByName(getSelectedGroupName());
        if (group == null) {
            throwError("Не обрана група для видалення!");
            return;
        }

        if (throwWarning("Ця дія призведе до втрати даних про студентів цієї групи. Ви впевнені, що хочете продовжити?")) {
            removeGroup(group);
            refreshData();
        }
    }

    //</Работа с группами>

    //<Работа с предметами>

    private String getNewSubjectName() {
        TextField subjectName = (TextField) SubjectPane.lookup("#SubjectName");
        return subjectName.getText();
    }

    private String getSelectedSubjectName() {
        ComboBox<String> subjectBox = (ComboBox<String>) SubjectPane.lookup("#SubjectBox");
        return subjectBox.getValue();
    }

    @FXML
    private void addSubject() {
        String newSubjectName = getNewSubjectName();

        if (model.getSubjectByName(newSubjectName) != null) {
            throwError("Назва предмета не є унікальною!");
            return;
        } else if (newSubjectName.trim().equals("")) {
            throwError("Неправильна назва предмета!");
            return;
        }

        Subject subject = new Subject(newSubjectName);
        model.getSubjects().add(subject);
        new SubjectDao().insert(subject);

        refreshData();
    }

    @FXML
    private void changeSubject() {
        Subject subject = model.getSubjectByName(getSelectedSubjectName());
        if (subject == null) {
            throwError("Не обраний предмет для змiни назви!");
        }

        String newSubjectName = getNewSubjectName();

        if (model.getSubjectByName(newSubjectName) != null) {
            throwError("Назва предмета не є унікальною!");
            return;
        }

        subject.setName(newSubjectName);
        new SubjectDao().update(subject);

        refreshData();
    }

    @FXML
    private void deleteSubject() {
        Subject subject = model.getSubjectByName(getSelectedSubjectName());
        if (subject == null) {
            throwError("Не обрана група для видалення!");
            return;
        }

        if (throwWarning("Ця дія призведе до втрати даних про вивчення цієї дисципліни. Ви впевнені, що хочете продовжити?")) {
            removeSubject(subject);
            refreshData();
        }
    }

    //</Работа с предметами>

    //<Работа с преподаванием предметов (Learning)>

    @FXML
    private void setCoefficient() {
        ComboBox<String> learningSub = (ComboBox<String>) LearningPane.lookup("#LearningSubjectBox");
        ComboBox<String> learningGroup = (ComboBox<String>) LearningPane.lookup("#LearningGroupBox");
        TextField coefText = (TextField) LearningPane.lookup("#CoefficientText");

        if (learningSub.getValue() == null) {
            throwError("Предмет не був обраний");
            return;
        } else if (learningGroup.getValue() == null) {
            throwError("Група не була обрана");
            return;
        }

        float coef;

        try {
            String value = coefText.getText();
            DecimalFormat formatter = new DecimalFormat("#.##");
            String v = formatter.format(Double.valueOf(value));
            v = v.replaceAll(",", ".");
            coef =  Float.valueOf(v);
            if(coef < 0)
                throw new Exception();
        } catch (Exception e) {
            throwError("Коефіцієнт введений неправильно!");
            return;
        }

        Learning learning = new Learning(model.getSubjectByName(learningSub.getValue()), model.getGroupByName(learningGroup.getValue()), coef);

        if (model.getLearnings().contains(learning)) {
            learning = model.getLearnings().get(model.getLearnings().indexOf(learning));
            learning.setCoefficient(coef);
            new LearningDao().update(learning);
        } else if (throwWarning("Додати предмет " + learningSub.getValue() + " до групи " + learningGroup.getValue() + "?")) {
            model.getLearnings().add(learning);
            new LearningDao().insert(learning);

            //SMART добавление
            ArrayList<Student> students = learning.getGroup().getStudents();
            ProgressDao prdao = new ProgressDao();
            for(Student st : students){
                Progress progress = new Progress(st, learning);
                model.getProgresses().add(progress);
                prdao.insert(progress);
            }
        } else {
            return;
        }

        refreshData();
    }

    //</Работа с преподаванием предметов (Learning)>

    //<Работа со студентами>

    @FXML
    private void addStudent() {
        TextField stName = (TextField) StudentAddPane.lookup("#StudentAddName");
        ComboBox<String> StudentAddGroup = (ComboBox<String>) StudentAddPane.lookup("#StudentAddGroup");
        TextField stPoint = (TextField) StudentAddPane.lookup("#StudentAddPoint");

        if (stName.getText() == null) {
            throwError("Ім'я студента не введено");
            return;
        } else if (StudentAddGroup.getValue() == null) {
            throwError("Група студента не обрана");
            return;
        }

        float additionalPoints;

        if (stPoint.getText() == null) {
            stPoint.setText("0");
        }

        try {
            String value = stPoint.getText();
            DecimalFormat formatter = new DecimalFormat("#.##");
            String v = formatter.format(Double.valueOf(value));
            v = v.replaceAll(",", ".");
            additionalPoints =  Float.valueOf(v);

            if(additionalPoints > 10 || additionalPoints < 0)
                throw new Exception();
        }catch(Exception e){
            throwError("Додатковi бали введенi неправильно!");
            return;
        }

        Student student = new Student(model.getGroupByName(StudentAddGroup.getValue()), stName.getText(), additionalPoints);
        model.getStudents().add(student);
        student.getGroup().addStudent(student);

        new StudentDao().insert(student);

        //SMART добавление
        List<Learning> stLearnings = model.getStudentsLearnings(student);
        ProgressDao prdao = new ProgressDao();
        for(Learning learn : stLearnings){
            Progress pr = new Progress(student, learn);
            model.getProgresses().add(pr);
            prdao.insert(pr);
        }

        refreshData();
    }

    @FXML
    private void editStudent(){
        TextField newName = (TextField) editStudentPane.lookup("#newStudentName");
        ComboBox<String> newGroup = (ComboBox<String>) editStudentPane.lookup("#newStudentGroup");
        TextField newPoints = (TextField) editStudentPane.lookup("#newStudentPoint");

        if(newName.getText() == null || newName.getText().trim().equals("")){
            throwError("Ім'я студента не введено");
            return;
        }

        float points;

        try{
            String value = newPoints.getText();
            DecimalFormat formatter = new DecimalFormat("#.##");
            String v = formatter.format(Double.valueOf(value));
            v = v.replaceAll(",", ".");
            points =  Float.valueOf(v);
            if(points > 10 || points < 0)
                throw new Exception();
        }catch(Exception e){
            throwError("Додатковi бали введенi неправильно!");
            return;
        }

        changingStudent.setName(newName.getText());
        changingStudent.setGroup(model.getGroupByName(newGroup.getValue()));
        changingStudent.setAdditionalPoints(points);

        new StudentDao().update(changingStudent);
        refreshData();
    }
    //</Работа со студентами>

    //<Ошибки и предупреждения>

    private void throwError(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Помилка");
        alert.setHeaderText("Помилка в роботi програми");
        alert.setContentText(text);
        alert.setHeight(800);
        alert.setResizable(true);
        alert.showAndWait();
    }

    private boolean throwWarning(String text) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Попередження");
        alert.setHeaderText("Підтвердіть свою дію");
        alert.setContentText(text);
        alert.setResizable(true);
        alert.setHeight(800);

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    //</Ошибки и предупреждения>

    //<Обновление элементов интерфейса>

    private void refreshDistribution(){
        ObservableList<SubjectDistribution> data = observableArrayList();
        ArrayList<Learning> learnings = model.getLearnings();

        for(Learning learn : learnings){
            SubjectDistribution subdist = new SubjectDistribution(learn.getGroup().getName(), learn.getSubject().getName(), learn.getCoefficient());
            Button deleteButton = subdist.getDelButton();
            deleteButton.setOnMouseClicked((event)->{
                if(!throwWarning("Весь прогрес студентів групи " + subdist.getGroupName() + ", пов'язаний з предметом " + subdist.getSubjectName() + " буде видалений. Продовити видалення?"))
                    return;

                deleteLearning(learn);
                refreshData();
            });
            data.add(subdist);
        }

        DistribTable.setItems(data);
    }

    private void refreshStGroup(){
        ObservableList<Rating> data = observableArrayList();
        ArrayList<Student> students = model.getStudents();

        for(Student student : students){
            Rating rating = new Rating(student);
            Button deleteButton = rating.getDelButton();

            //SMART удаление
            deleteButton.setOnMouseClicked(event ->{
                if(!throwWarning("Весь прогрес студента " + rating.getStudentName() + " буде видалений. Продовити видалення?"))
                    return;
                deleteStudent(student);

                refreshData();
            });

            Button editButton = rating.getEditButton();
            editButton.setOnMouseClicked(event ->{
                if(!rating.isEditing() && changingStudent == null){
                    rating.setEditing(true);
                    isStudentEditing = true;
                    editStudentPane.setDisable(false);

                    TextField newName = (TextField) editStudentPane.lookup("#newStudentName");
                    newName.setText(rating.getStudentName());
                    ComboBox<String> newGroup = (ComboBox<String>) editStudentPane.lookup("#newStudentGroup");
                    newGroup.setValue(rating.getGroupName());
                    TextField newPoints = (TextField) editStudentPane.lookup("#newStudentPoint");
                    newPoints.setText(String.valueOf(rating.getAdditionalPoints()));

                    for(Rating r : data){
                        if(!r.equals(rating))
                            r.getEditButton().setDisable(true);
                    }
                    changingStudent = student;

                }else if(rating.isEditing()) {
                    rating.setEditing(false);
                    isStudentEditing = false;
                    editStudentPane.setDisable(true);
                    for (Rating r : data) {
                        r.getEditButton().setDisable(false);
                    }
                    changingStudent = null;
                }
            });
            data.add(rating);
        }

        StGroups.setItems(data);
    }

    private void refreshTableViews() {
        setRatingGroup();
        setGroup();
        refreshDistribution();
        refreshStGroup();
    }

    private void refreshComboBoxes() {
        String[] groupNames = model.getGroupNames();

        ArrayList<String> special = new ArrayList<>();
        special.add("Усi групи");
        special.addAll(Arrays.asList(groupNames));

        String[] arr = new String[special.size()];
        special.toArray(arr);
        
        GroupChoice.setItems(observableArrayList(arr));
        GroupChoice.setValue("Усi групи");

        GroupChoice2.setItems(observableArrayList(special));
        GroupChoice2.setValue("Усi групи");

        special = new ArrayList<>();
        special.add("Усi студенти");
        special.addAll(Arrays.asList(model.getStudentNames()));

        StudentChoice.setItems(observableArrayList(special));
        StudentChoice.setValue("Усi студенти");


        ComboBox<String> box = (ComboBox<String>) SubPointsPane.lookup("#SubPointsStudent");
        box.setItems(observableArrayList(model.getStudentNames()));

        ComboBox<String> groupbox = (ComboBox<String>) GroupPane.lookup("#GroupBox");
        groupbox.setItems(observableArrayList(groupNames));

        ComboBox<String> subjectBox = (ComboBox<String>) SubjectPane.lookup("#SubjectBox");
        subjectBox.setItems(observableArrayList(model.getSubjectNames()));

        ComboBox<String> learningSub = (ComboBox<String>) LearningPane.lookup("#LearningSubjectBox");
        learningSub.setItems(observableArrayList(model.getSubjectNames()));

        ComboBox<String> learningGroup = (ComboBox<String>) LearningPane.lookup("#LearningGroupBox");
        learningGroup.setItems(observableArrayList(groupNames));

        ComboBox<String> StudentAddGroup = (ComboBox<String>) StudentAddPane.lookup("#StudentAddGroup");
        StudentAddGroup.setItems(observableArrayList(groupNames));

        ComboBox<String> newStudentGroup = (ComboBox<String>) editStudentPane.lookup("#newStudentGroup");
        newStudentGroup.setItems(observableArrayList(groupNames));
    }

    private void refreshData(){
        refreshComboBoxes();
        refreshTableViews();
        changingStudent = null;
        editStudentPane.setDisable(true);
    }
    //</Обновление элементов интерфейса>

    //<SMART удаление>

    private void deleteLearning(Learning learn){
        List<Progress> garbage = model.getLearningProgresses(learn);
        ProgressDao prdao = new ProgressDao();
        for(Progress pr : garbage){
            model.getProgresses().remove(pr);
            prdao.delete(pr);
        }
        model.getLearnings().remove(learn);
        new LearningDao().delete(learn);

        refreshData();
    }

    private void deleteStudent(Student student){
        List<Progress> garbage = model.getStudentProgresses(student);
        ProgressDao prdao = new ProgressDao();
        for(Progress pr : garbage){
            model.getProgresses().remove(pr);
            prdao.delete(pr);
        }
        model.getStudents().remove(student);
        new StudentDao().delete(student);

        refreshData();
    }

    private void removeSubject(Subject subject){
        List<Learning> lst = model.getSubjectLearnings(subject);
        for(Learning learn : lst)
            deleteLearning(learn);

        model.getSubjects().remove(subject);
        new SubjectDao().delete(subject);

        refreshData();
    }

    private void removeGroup(Group group){
        for(Student st : group.getStudents())
            deleteStudent(st);
        for(Learning ln : model.getGroupLearnings(group))
            deleteLearning(ln);

        model.getGroups().remove(group);
        new GroupDao().delete(group);

        refreshData();
    }
    //</SMART удаление>
}