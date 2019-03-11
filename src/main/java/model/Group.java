package model;

import java.util.ArrayList;

public class Group {

    private long id;

    private String name;

    private ArrayList<Student> students;

    public Group(long id, String name){
        this.id = id;
        this.name = name;
        students = new ArrayList<>();
    }

    public Group(long id, String name, ArrayList<Student> st){
        this(id, name);
        students = st;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }
}