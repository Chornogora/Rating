package model;

import java.util.ArrayList;
import java.util.HashMap;
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

        //int[][] points = new int[stProgresses.size()][2];
        float coefSum=0;
        float sum = 0;
        for(int i = 0; i < stProgresses.size(); ++i){
            //points[i][0] = stProgresses.get(i).getLearning().getCoefficient();
            //points[i][1] = stProgresses.get(i).getProgressPoints();
            sum += stProgresses.get(i).getLearning().getCoefficient() * stProgresses.get(i).getProgressPoints();
            coefSum += stProgresses.get(i).getLearning().getCoefficient();
        }

        return (float)(((sum/coefSum)*0.9) + student.getAdditionalPoints());
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
}
