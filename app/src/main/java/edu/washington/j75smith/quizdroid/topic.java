package edu.washington.j75smith.quizdroid;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jordans_macbook on 5/10/15.
 */
public class topic implements Serializable {

    private String title;
    private String shortDescription;
    private String longDescription;
    private List<question> questions;

    public topic(String title, String shortDescription, String longDescription, List<question> questions) {
        this.setShortDescription(shortDescription);
        this.setLongDescription(longDescription);
        this.setTitle(title);
        this.setQuestions(questions);
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<question> questions) {
        this.questions = questions;
    }
}
