package model;

public class Student {

    private long id;

    private Group group;

    private String name;

    private float additionalPoints;

    public Student(long id, Group group, String name){
        this.id = id;
        this.name = name;
        this.group = group;
        this.additionalPoints = 0;
    }

    public Student(long id, Group group, String name, float additionalPoints){
        this(id, group, name);
        this.additionalPoints = additionalPoints;
    }

    public long getId() {
        return id;
    }

    public void setGroup_id(Group group) {
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

    public void setAdditionalPoints(float additionalPoints) {
        if(additionalPoints < 0 || additionalPoints > 10)
            throw new IllegalArgumentException("Кількість додаткових балів має бути від 0 до 10");
        this.additionalPoints = additionalPoints;
    }
}