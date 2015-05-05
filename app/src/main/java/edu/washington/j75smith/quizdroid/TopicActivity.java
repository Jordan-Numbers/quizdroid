package edu.washington.j75smith.quizdroid;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Random;


public class TopicActivity extends Activity {

    private int topic;
    private int length;
    private String[] questions;
    private String[] answers;
    private String[] fakes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        Intent previous = getIntent();
        int topicSelected = previous.getIntExtra("topicID", 12);
        this.topic = topicSelected;
        displayTopicInfo(this.topic);
        findViewById(R.id.btn_begin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadQuestionFrag(0, 0);
            }
        });
    }

    private void displayTopicInfo(int topic){
        //get the UI elements
        TextView title = (TextView) this.findViewById(R.id.topic_title);
        TextView description = (TextView) this.findViewById(R.id.topic_description);
        //Depending on the topic we need to display the title & descrip
        switch (topic) {
            case R.id.quiz_one:
                title.setText(getString(R.string.quiz_one));
                description.setText(getString(R.string.description_one));
                Intent next = new Intent(TopicActivity.this, QuestionActivity.class);
                this.questions = getResources().getStringArray(R.array.questions_one);
                this.answers = getResources().getStringArray(R.array.correct_one);
                this.fakes = getResources().getStringArray(R.array.wrong_one);
                this.length = this.questions.length;
                break;

            case R.id.quiz_two:
                title.setText(getString(R.string.quiz_two));
                description.setText(getString(R.string.description_two));
                this.questions = getResources().getStringArray(R.array.questions_two);
                this.answers = getResources().getStringArray(R.array.correct_two);
                this.fakes = getResources().getStringArray(R.array.wrong_two);
                this.length = this.questions.length;
                break;

            case R.id.quiz_three:
                title.setText(getString(R.string.quiz_three));
                description.setText(getString(R.string.description_three));
                this.questions = getResources().getStringArray(R.array.questions_three);
                this.answers = getResources().getStringArray(R.array.correct_three);
                this.fakes = getResources().getStringArray(R.array.wrong_three);
                this.length = this.questions.length;
                break;

            default:
                break;
        }
    }

    public void loadQuestionFrag(int index, int correct) {
        //Make our fragment creator objects
        FragmentManager fragManager = getFragmentManager();
        FragmentTransaction fragTransaction = fragManager.beginTransaction();

        //make our bundle
        Bundle topicBundle = new Bundle();
        topicBundle.putInt("index", index);
        topicBundle.putInt("correct", correct);

        //make our fragment
        QuestionFragment firstQuestion = new QuestionFragment();
        firstQuestion.setArguments(topicBundle);

        //clear and set our fragment
        FrameLayout container = (FrameLayout) findViewById(R.id.container);
        container.removeAllViews();
        fragTransaction.add(R.id.container, firstQuestion);
        fragTransaction.commit();
    }

    public void loadAnswerFrag(String userAnswer, int index, int correct) {
        //Make our fragment creator objects
        FragmentManager fragManager = getFragmentManager();
        FragmentTransaction fragTransaction = fragManager.beginTransaction();

        //make our bundle
        Bundle topicBundle = new Bundle();
        topicBundle.putInt("index", index);
        topicBundle.putInt("correct", correct);
        topicBundle.putString("answer", userAnswer);

        //make the new fragment
        AnswerFragment nextAnswer = new AnswerFragment();
        nextAnswer.setArguments(topicBundle);

        //clear then set our fragment
        FrameLayout container = (FrameLayout) findViewById(R.id.container);
        container.removeAllViews();
        fragTransaction.add(R.id.container, nextAnswer);
        fragTransaction.commit();

    }

    //returns three wrong answers for the current topic
    public String[] getFakes() {
        String[] wrong = new String[3];
        Random r = new Random();
        for(int i = 0; i < 3; i++){
            wrong[i] = this.fakes[r.nextInt(15)];
        }
        return wrong;
    }

    //returns the question at the given index
    public String getQuestion(int index) {
        return this.questions[index];
    }

    //returns the answer at the given index
    public String getAnswer(int index){
        return this.answers[index];
    }

    //returns the quiz length
    public int getLength() {
        return this.length;
    }
}
