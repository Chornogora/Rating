package model.representation;

import model.Student;

public class ProgressResult {

    private long studentId;

    private String studentName;

    private String subjectName;

    private int points;

    public ProgressResult(Student student, String subjectName, int points){
        this.studentId = student.getId();
        this.studentName = student.getName();
        this.subjectName = subjectName;
        this.points = points;
    }

    public long getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public int getPoints() {
        return points;
    }
}