package representation;

import javafx.scene.control.Button;
import model.Student;

public class Rating {

    private long studentId;

    private int studentPlace;

    private String studentName;

    private String groupName;

    private float points;

    private float additionalPoints;

    //Поля для таблицы "Студенты и группы"

    private Button delButton;

    private Button editButton;

    private boolean isEditing = false;

    public Rating(Student student){
        this.studentId = student.getId();
        this.studentName = student.getName();
        this.groupName = student.getGroup().getName();
        this.additionalPoints = student.getAdditionalPoints();

        delButton = new Button("Видалити");
        editButton = new Button("Змiнити");
    }

    public Rating(Student student, float points, int place){
        this(student);
        this.points = points;
        this.studentPlace = place;
    }

    public long getStudentId() {
        return studentId;
    }

    public int getStudentPlace() {
        return studentPlace;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getGroupName() {
        return groupName;
    }

    public float getPoints() {
        return points;
    }

    public float getAdditionalPoints() {
        return additionalPoints;
    }

    public Button getDelButton() {
        return delButton;
    }

    public Button getEditButton() {
        return editButton;
    }

    public boolean isEditing() {
        return isEditing;
    }

    public void setEditing(boolean editing) {
        isEditing = editing;
    }
}
