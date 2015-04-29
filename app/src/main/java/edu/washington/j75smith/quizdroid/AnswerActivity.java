package edu.washington.j75smith.quizdroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class AnswerActivity extends Activity {

    private int index;
    private int correct;
    private String[] questions;
    private String[] answers;
    private String[] fakes;
    private String userAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        //get our data from the intent
        Intent previous = getIntent();
        Bundle extras = previous.getExtras();
        this.index = extras.getInt("index");
        this.correct = extras.getInt("correct");
        this.questions = extras.getStringArray("questions");
        this.answers = extras.getStringArray("answers");
        this.fakes = extras.getStringArray("fakes");
        this.userAnswer = extras.getString("userAnswer");
        String correctAnswer = this.answers[this.index];
        //get some ui elements
        TextView answer_yours = (TextView) findViewById(R.id.answer_yours);
        TextView answer_correct = (TextView) findViewById(R.id.answer_correct);
        TextView score = (TextView) findViewById(R.id.score);
        //Did they get it right?
        if(this.userAnswer.equals(correctAnswer)){
            this.correct++;
        }
        //set the UI display
        answer_yours.setText("Your Answer: " + this.userAnswer);
        answer_correct.setText("Correct Answer: " + this.answers[index]);
        int quizLength = this.questions.length;
        String displayScore = "You have " + this.correct + " out of " + quizLength + " correct";
        score.setText(displayScore);
        Button btn_next = (Button) findViewById(R.id.btn_next);
        //where are we in the quiz?
        this.index++;
        if(this.index == quizLength){
            btn_next.setText("Finish");
        }else{
            btn_next.setText("Next");
        }
        btn_next.setOnClickListener(new SubmitOnClickListener(this.index, this.correct, this.questions,
                                                              this.answers, this.fakes));
    }

    //I hate myself for this too
    class SubmitOnClickListener implements View.OnClickListener
    {

        int index;
        int correct;
        String[] questions;
        String[] answers;
        String[] fakes;

        public SubmitOnClickListener(int index, int correct, String[] questions, String[] answers, String[] fakes) {
            //we still need to pass along all that data in case there are more questions
            this.answers = answers;
            this.questions = questions;
            this.fakes = fakes;
            this.index = index;
            this.correct = correct;
        }

        @Override
        public void onClick(View v)
        {
            Button btn_clicked = (Button) v;
            String btn_type = (String) btn_clicked.getText();
            if(btn_type.equals("Next")) {
                //if there is another question pass along the data
                Intent next = new Intent(AnswerActivity.this, QuestionActivity.class);
                next.putExtra("index", this.index);
                next.putExtra("correct", this.correct);
                next.putExtra("questions", this.questions);
                next.putExtra("answers", this.answers);
                next.putExtra("fakes", this.fakes);
                startActivity(next);
            } else {
                //if the quiz is done, send them back to the begin to take another
                Intent next = new Intent(AnswerActivity.this, MainActivity.class);
                startActivity(next);
            }
        }
    };
}
