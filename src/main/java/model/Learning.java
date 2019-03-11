package model;

public class Learning extends ModelObject{

    private Subject subject;

    private Group group;

    private float coefficient=0;

    public Learning(Subject subject, Group group){
        this.subject = subject;
        this.group = group;
    }

    public Learning(Subject subject, Group group, float coefficient){
        this(subject, group);
        this.coefficient = coefficient;
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

    public float getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(int coefficient) {
        this.coefficient = coefficient;
    }
}