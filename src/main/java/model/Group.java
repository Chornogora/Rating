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

    public String[] getStudentNames(){
        int size = students.size();
        String[] names = new String[size];
        for(int i = 0; i < size; ++i){
            names[i] = this.students.get(i).getName();
        }
        return names;
    }

    public void addStudent(Student st){
        students.add(st);
    }
}