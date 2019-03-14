package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Model {

    private ArrayList<Student> students;

    private ArrayList<Group> groups;

    private ArrayList<Subject> subjects;

    private ArrayList<Learning> learnings;

    private ArrayList<Progress> progresses;

    public Model(){

    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
        groups.sort(Comparator.comparing(Group::getName));
    }

    public void setSubjects(ArrayList<Subject> subjects) {
        this.subjects = subjects;
    }

    public void setLearnings(ArrayList<Learning> learnings) {
        this.learnings = learnings;
    }

    public void setProgresses(ArrayList<Progress> progresses) {
        this.progresses = progresses;
    }

    public float getStudentRating(Student student){
        List<Progress> stProgresses = progresses.stream()
                .filter(progress -> progress.getStudent().equals(student))
                .collect(Collectors.toList());

        float coefSum=0;
        float sum = 0;
        for(int i = 0; i < stProgresses.size(); ++i){
            sum += stProgresses.get(i).getLearning().getCoefficient() * stProgresses.get(i).getProgressPoints();
            coefSum += stProgresses.get(i).getLearning().getCoefficient();
        }

        return Float.valueOf(String.format("%.2f", (float)(((sum/coefSum)*0.9) + student.getAdditionalPoints())));
    }

    public List<Learning> getStudentsLearnings(Student student){
        Group stGroup = student.getGroup();
        return learnings.stream()
                .filter(learning -> learning.getGroup().equals(stGroup))
                .collect(Collectors.toList());
    }

    public List<Subject> getStudentsSubjects(Student student){
        List<Learning> stLearnings = getStudentsLearnings(student);
        List<Subject> result = new ArrayList<>(stLearnings.size());
        for(Learning ln : stLearnings)
            result.add(ln.getSubject());
        return result;
    }

    public String[] getGroupNames(){
        String[] names = new String[groups.size()];
        for(int i = 0; i < names.length; ++i){
            names[i] = groups.get(i).getName();
        }
        return names;
    }

    public Group getGroupByName(String name){
        for(Group group : groups)
            if(group.getName().equals(name))
                return group;
        return null;
    }

    public ArrayList<Progress> getStudentProgresses(Student student){
        ArrayList<Progress> result = new ArrayList<>();
        for(Progress pr : progresses){
            if(pr.getStudent().equals(student))
                result.add(pr);
        }
        return result;
    }

    public String[] getStudentNames(){
        int size = students.size();
        String[] names = new String[size];
        for(int i = 0; i < size; ++i){
            names[i] = students.get(i).getName();
        }
        return names;
    }

    public Student getStudentByName(String name){
        for(Student st : students)
            if(st.getName().equals(name))
                return st;
        return null;
    }

    public Subject getSubjectByName(String name){
        for(Subject sb : subjects)
            if(sb.getName().equals(name))
                return sb;
        return null;
    }

    public Learning getLearningByData(Group group, Subject subject){
        for(Learning ln : learnings)
            if(ln.getGroup().equals(group) && ln.getSubject().equals(subject))
                return ln;
        return null;
    }

    public Progress getProgressByData(Student st, Subject sb){
        Learning ln = getLearningByData(st.getGroup(), sb);
        for(Progress pr : progresses)
            if(pr.getStudent().equals(st) && pr.getLearning().equals(ln))
                return pr;
        /*Progress progress = new Progress(st, ln);
        progresses.add(progress);*/
        return null;
    }

    public void sortStudentByRating(){
        students.sort((a, b) -> {
            float ar = getStudentRating(a), br = getStudentRating(b);
            if( ar - br > 0)
                return -1;
            else if (ar - br < 0)
                return 1;
            else return 0;
        });
    }

    public ArrayList<Student> getStudents() {
        return students;
    }
}