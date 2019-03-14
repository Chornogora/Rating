package model;

public class Student extends ModelObject{

    private Group group;

    private String name;

    private float additionalPoints = 0;

    public Student(Group group, String name){
        this.group = group;
        this.name = name;
    }

    public Student(Group group, String name, float additionalPoints){
        this(group, name);
        this.additionalPoints = additionalPoints;
    }

    public void setGroup(Group group) {
        this.group= group;
    }

    public Group getGroup() {
        return group;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public float getAdditionalPoints() {
        return additionalPoints;
    }

    public void setAdditionalPoints(float additionalPoints) throws IllegalArgumentException {
        if(additionalPoints < 0 || additionalPoints > 10)
            throw new IllegalArgumentException("Кількість додаткових балів має бути від 0 до 10");
        this.additionalPoints = additionalPoints;
    }

    @Override
    public String toString(){
        return this.getName();
    }
}