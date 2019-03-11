package model;

public class Progress {

    private long id;

    private Student student;

    private Learning learning;

    private int progressPoints;

    public Progress(long id, Student student, Learning learning){
        this.id = id;
        this.student = student;
        this.learning = learning;
        this.progressPoints = 0;
    }

    public Progress(long id, Student student, Learning learning, int progressPoints){
        this(id, student, learning);
        this.progressPoints = progressPoints;
    }

    public long getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Learning getLearning() {
        return learning;
    }

    public void setLearning(Learning learning) {
        this.learning = learning;
    }

    public int getProgressPoints() {
        return progressPoints;
    }

    public void setProgressPoints(int progressPoints) {
        this.progressPoints = progressPoints;
    }
}