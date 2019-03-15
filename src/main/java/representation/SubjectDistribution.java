package representation;

import javafx.scene.control.Button;

public class SubjectDistribution {
    private String groupName;

    private String subjectName;

    private float coefficient;

    private Button delButton;

    public SubjectDistribution(String group, String subject, float coef){
        this.groupName = group;
        this.subjectName = subject;
        this.coefficient = coef;
        delButton = new Button("Видалити");
    }

    public String getGroupName() {
        return groupName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public float getCoefficient() {
        return coefficient;
    }

    public Button getDelButton() {
        return delButton;
    }

    @Override
    public String toString(){
        return groupName + " " + subjectName;
    }
}
