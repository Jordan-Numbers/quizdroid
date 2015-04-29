package edu.washington.j75smith.quizdroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class TopicActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        Intent previous = getIntent();
        int topic = previous.getIntExtra("topicID", 12);
        displayTopicInfo(topic);
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
                this.findViewById(R.id.btn_begin).setOnClickListener(onClickListener_one);
                break;

            case R.id.quiz_two:
                title.setText(getString(R.string.quiz_two));
                description.setText(getString(R.string.description_two));
                this.findViewById(R.id.btn_begin).setOnClickListener(onClickListener_two);
                break;

            case R.id.quiz_three:
                title.setText(getString(R.string.quiz_three));
                description.setText(getString(R.string.description_three));
                this.findViewById(R.id.btn_begin).setOnClickListener(onClickListener_three);
                break;

            default:
                break;
        }
    }

    //I hate myself for this
    // each Q needs {index, questionArray, answerArray, wrongAnswerArray}
    //for whichever topic is selected choose a listener that gets the correct resources
    private View.OnClickListener onClickListener_one = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent next = new Intent(TopicActivity.this, QuestionActivity.class);
            String[] questions = getResources().getStringArray(R.array.questions_one);
            String[] answers = getResources().getStringArray(R.array.correct_one);
            String[] fakes = getResources().getStringArray(R.array.wrong_one);
            next.putExtra("index", 0);
            next.putExtra("correct", 0);
            next.putExtra("questions", questions);
            next.putExtra("answers", answers);
            next.putExtra("fakes", fakes);
            startActivity(next);
        }
    };

    private View.OnClickListener onClickListener_two = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent next = new Intent(TopicActivity.this, QuestionActivity.class);
            String[] questions = getResources().getStringArray(R.array.questions_two);
            String[] answers = getResources().getStringArray(R.array.correct_two);
            String[] fakes = getResources().getStringArray(R.array.wrong_two);
            next.putExtra("index", 0);
            next.putExtra("correct", 0);
            next.putExtra("questions", questions);
            next.putExtra("answers", answers);
            next.putExtra("fakes", fakes);
            startActivity(next);
        }
    };

    private View.OnClickListener onClickListener_three = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent next = new Intent(TopicActivity.this, QuestionActivity.class);
            String[] questions = getResources().getStringArray(R.array.questions_three);
            String[] answers = getResources().getStringArray(R.array.correct_three);
            String[] fakes = getResources().getStringArray(R.array.wrong_three);
            next.putExtra("index", 0);
            next.putExtra("correct", 0);
            next.putExtra("questions", questions);
            next.putExtra("answers", answers);
            next.putExtra("fakes", fakes);
            startActivity(next);
        }
    };

}
