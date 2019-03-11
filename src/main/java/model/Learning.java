package model;

public class Learning {

    private long id;

    private Subject subject;

    private Group group;

    private int coefficient;

    public Learning(long id, Subject subject, Group group){
        this.id = id;
        this.subject = subject;
        this.group = group;
        coefficient = 0;
    }

    public Learning(long id, Subject subject, Group group, int coefficient){
        this(id, subject, group);
        this.coefficient = coefficient;
    }

    public long getId() {
        return id;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public int getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(int coefficient) {
        this.coefficient = coefficient;
    }
}