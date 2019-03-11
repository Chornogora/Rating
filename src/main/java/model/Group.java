package model;

import java.util.ArrayList;

public class Group extends ModelObject{

    private String name;

    private ArrayList<Student> students;

    public Group(String name){
        this.name = name;
        students = new ArrayList<>();
    }

    public Group(String name, ArrayList<Student> st){
        this(name);
        students = st;
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