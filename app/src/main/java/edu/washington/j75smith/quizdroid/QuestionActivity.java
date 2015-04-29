package edu.washington.j75smith.quizdroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Random;


public class QuestionActivity extends Activity {

    private int index;
    private int correct;
    private String[] questions;
    private String[] answers;
    private String[] fakes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        //get our data from the intent
        Intent previous = getIntent();
        Bundle extras = previous.getExtras();
        this.index = extras.getInt("index");
        this.correct = extras.getInt("correct");
        this.questions = extras.getStringArray("questions");
        this.answers = extras.getStringArray("answers");
        this.fakes = extras.getStringArray("fakes");

        //display the question and answers
        TextView questionTitle = (TextView) this.findViewById(R.id.question);
        questionTitle.setText(this.questions[this.index]);
        //each correct answer should randomly appear as a,b,c, or d
        RadioGroup answerBtns = (RadioGroup) findViewById(R.id.answers);
        Random r = new Random();
        int correctAnswer = r.nextInt(4);
        for (int i = 0; i < 4; i++) {
            RadioButton currentAnswer = (RadioButton) answerBtns.getChildAt(i);
            if(i == correctAnswer){
                currentAnswer.setText(this.answers[this.index]);
            }else{
                //choose a random wrong answer from the fakes 'corpus'
                int random;
                random = r.nextInt(15);
                currentAnswer.setText(this.fakes[random]);
            }
            currentAnswer.setOnClickListener(onClickListener_radio);
        }
        //setup submitting
        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new SubmitOnClickListener(this.index, this.correct, this.questions,
                                                            this.answers, this.fakes));
    }
    //if they select an answer show the submit button
    private View.OnClickListener onClickListener_radio = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button submit = (Button) findViewById(R.id.submit);
            submit.setVisibility(View.VISIBLE);
        }
    };

    //I hate myself for this too
    class SubmitOnClickListener implements OnClickListener
    {

        int index;
        int correct;
        String[] questions;
        String[] answers;
        String[] fakes;
        String userAnswer;

        public SubmitOnClickListener(int index, int correct, String[] questions, String[] answers, String[] fakes) {
            //we need to keep this data to pass to the next activity(ies)
            this.answers = answers;
            this.questions = questions;
            this.fakes = fakes;
            this.index = index;
            this.correct = correct;
        }

        @Override
        public void onClick(View v)
        {
            RadioGroup answerButtons = (RadioGroup) findViewById(R.id.answers);
            RadioButton checked = (RadioButton) findViewById(answerButtons.getCheckedRadioButtonId());
            //make sure we pass along all data and the checked radio answer
            this.userAnswer = (String) checked.getText();
            Intent next = new Intent(QuestionActivity.this, AnswerActivity.class);
            next.putExtra("index", this.index);
            next.putExtra("correct", this.correct);
            next.putExtra("questions", this.questions);
            next.putExtra("answers", this.answers);
            next.putExtra("fakes", this.fakes);
            next.putExtra("userAnswer", this.userAnswer);
            startActivity(next);
        }
    }

}