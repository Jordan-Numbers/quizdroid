package edu.washington.j75smith.quizdroid;

import java.io.Serializable;
import java.util.List;

/**
 *
 * Created by jordans_macbook on 5/10/15.
 */
public class question implements Serializable {

    private String questionText;
    private List<String> answers;
    private int answerIndex;

    public question(List<String> answers, String questionText, int answerIndex){
        this.setAnswerIndex(answerIndex);
        this.setAnswers(answers);
       this. setQuestionText(questionText);
    }

    public int getAnswerIndex() {
        return answerIndex;
    }

    public void setAnswerIndex(int answerIndex) {
        this.answerIndex = answerIndex;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }
}
