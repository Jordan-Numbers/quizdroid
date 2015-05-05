package edu.washington.j75smith.quizdroid;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class AnswerFragment extends Fragment {
    private int index;
    private int correct;
    private String userAnswer;
    private String answer;
    private Activity hostActivity;

    public AnswerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //get our quiz state
            this.index = (int) getArguments().get("index");
            this.correct = (int) getArguments().get("correct");
            this.userAnswer = (String) getArguments().get("answer");
        }
        if (hostActivity instanceof TopicActivity) {
            this.answer = ((TopicActivity) hostActivity).getAnswer(this.index);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_answer, container, false);

        //get some ui elements
        TextView answer_yours = (TextView) v.findViewById(R.id.answer_yours);
        TextView answer_correct = (TextView) v.findViewById(R.id.answer_correct);
        TextView score = (TextView) v.findViewById(R.id.score);
        //Did they get it right?
        if(this.userAnswer.equals(this.answer)){
            this.correct++;
        }
        //set the UI display
        answer_yours.setText("Your Answer: " + this.userAnswer);
        answer_correct.setText("Correct Answer: " + this.answer);
        int quizLength = ((TopicActivity) this.hostActivity).getLength();
        String displayScore = "You have " + this.correct + " out of " + quizLength + " correct";
        score.setText(displayScore);
        Button btn_next = (Button) v.findViewById(R.id.btn_next);
        //where are we in the quiz?
        this.index++;
        if(this.index == quizLength){
            btn_next.setText("Finish");
        }else{
            btn_next.setText("Next");
        }

        btn_next.setOnClickListener(new NextOnClickListener(this.index, this.correct));

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.hostActivity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    class NextOnClickListener implements View.OnClickListener {

        int index;
        int correct;

        public NextOnClickListener(int index, int correct) {
            //we need to keep this data to pass to the next activity(ies)
            this.index = index;
            this.correct = correct;
        }

        @Override
        public void onClick(View v) {
            Button btn_clicked = (Button) v;
            String btn_type = (String) btn_clicked.getText();
            if(btn_type.equals("Next")) {
                if (hostActivity instanceof TopicActivity) {
                    //tell the topic activity to load the answer fragment
                    ((TopicActivity) hostActivity).loadQuestionFrag(this.index, this.correct);
                }
            } else {
                //if the quiz is done, send them back to the begin to take another
                Intent next = new Intent(hostActivity, MainActivity.class);
                startActivity(next);
            }
        }
    }
}
