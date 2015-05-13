package edu.washington.j75smith.quizdroid;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.util.List;


public class TopicActivity extends Activity {

    private int length;
    private List<question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        //get which topic we are
        Intent previous = getIntent();
        topic topic = (topic) previous.getExtras().get("topic");

        //setup the quiz length and questions
        this.questions = topic.getQuestions();
        this.length = this.questions.size();

        //set UI
        TextView title = (TextView) this.findViewById(R.id.topic_title);
        TextView description = (TextView) this.findViewById(R.id.topic_description);
        title.setText(topic.getTitle());
        description.setText(topic.getShortDescription());


        findViewById(R.id.btn_begin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadQuestionFrag(0, 0);
            }
        });
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

    //returns the question at the given index
    public question getQuestion(int index) {
        return this.questions.get(index);
    }

    //returns the quiz length
    public int getLength() {
        return this.length;
    }
}
