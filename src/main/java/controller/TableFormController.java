package controller;

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
    private TableView SSTable;

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

        TableColumn<Rating, Long> sscol = (TableColumn<Rating, Long>) SSTable.getColumns().get(0);
        sscol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        TableColumn<Rating, String> sscol2 = (TableColumn<Rating, String>) SSTable.getColumns().get(1);
        sscol2.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        TableColumn<Rating, String> sscol3 = (TableColumn<Rating, String>) SSTable.getColumns().get(2);
        sscol3.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        TableColumn<Rating, Float> sscol4 = (TableColumn<Rating, Float>) SSTable.getColumns().get(3);
        sscol4.setCellValueFactory(new PropertyValueFactory<>("points"));

        GroupChoice.setValue("Усi групи");
        GroupChoice2.setValue("Усi групи");
        StudentChoice.setValue("Усi студенти");

        refreshComboBoxes();
        refreshTableViews();
    }

    @FXML
    private void setRatingGroup() {
        model.sortStudentByRating();

        ArrayList<Student> students;
        if (GroupChoice.getValue().equals("Усi групи"))
            students = model.getStudents();
        else {
            Group group = model.getGroupByName(GroupChoice.getValue());
            students = group.getStudents();
        }

        ObservableList<Rating> usersData = observableArrayList();
        for (int i = 1; i <= students.size(); ++i) {
            Student st = students.get(i - 1);
            usersData.add(new Rating(st, model.getStudentRating(st), st.getAdditionalPoints(), model.getStudents().indexOf(st) + 1));
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
            students = new ArrayList<Student>();
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

        refreshComboBoxes();
        refreshTableViews();
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

        refreshComboBoxes();
        refreshTableViews();
    }

    @FXML
    private void deleteGroup() {
        Group group = model.getGroupByName(getSelectedGroupName());
        if (group == null) {
            throwError("Не обрана група для видалення!");
            return;
        }

        if (throwWarning("Ця дія призведе до втрати даних про студентів цієї групи. Ви впевнені, що хочете продовжити?")) {

            model.getGroups().remove(group);
            new GroupDao().delete(group);

            refreshComboBoxes();
            refreshTableViews();
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

        refreshComboBoxes();
        refreshTableViews();
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

        refreshComboBoxes();
        refreshTableViews();
    }

    @FXML
    private void deleteSubject() {
        Subject subject = model.getSubjectByName(getSelectedSubjectName());
        if (subject == null) {
            throwError("Не обрана група для видалення!");
            return;
        }

        if (throwWarning("Ця дія призведе до втрати даних про студентів цієї групи. Ви впевнені, що хочете продовжити?")) {

            model.getSubjects().remove(subject);
            new SubjectDao().delete(subject);

            refreshComboBoxes();
            refreshTableViews();
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
            coef = Float.valueOf(formatter.format(Double.valueOf(value)));
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

        refreshComboBoxes();
        refreshTableViews();
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
        }

        float additionalPoints;

        if (stPoint.getText() == null) {
            stPoint.setText("0");
        }

        String value = stPoint.getText();
        DecimalFormat formatter = new DecimalFormat("#.##");
        additionalPoints = Float.valueOf(formatter.format(Double.valueOf(value)));


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

        refreshComboBoxes();
        refreshTableViews();
    }

    //</Работа со студентами>

    //<Ошибки и предупреждения>

    private void throwError(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Помилка");
        alert.setHeaderText("Помилка в роботi програми");
        alert.setContentText(text);
        alert.setResizable(true);
        alert.showAndWait();
    }

    private boolean throwWarning(String text) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Попередження");
        alert.setContentText(text);
        alert.setResizable(true);
        Optional<ButtonType> result = alert.showAndWait();

        return result.get() == ButtonType.OK;
    }

    //</Ошибки и предупреждения>

    //<Обновление элементов интерфейса>

    private void refreshTableViews() {
        setRatingGroup();
        setGroup();
    }

    private void refreshComboBoxes() {
        String[] groupNames = model.getGroupNames();

        ArrayList<String> special = new ArrayList<>();
        special.add("Усi групи");
        special.addAll(Arrays.asList(groupNames));

        GroupChoice.setItems(observableArrayList(special));
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
    }

    //</Обновление элементов интерфейса>
}