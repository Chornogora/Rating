package model.representation;

import model.Student;

public class Rating {

    private long studentId;

    private int studentPlace;

    private String studentName;

    private String groupName;

    private float points;

    private float additionalPoints;

    public Rating(Student student, float points, float addpoints, int place){
        this.points = points;
        this.additionalPoints = addpoints;

        this.studentPlace = place;
        this.studentId = student.getId();
        this.studentName = student.getName();
        this.groupName = student.getGroup().getName();
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
}
