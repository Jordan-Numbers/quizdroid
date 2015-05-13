package edu.washington.j75smith.quizdroid;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class QuestionFragment extends Fragment {
    private int index;
    private int correct;
    private question question;
    private Activity hostActivity;

    public QuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //get our quiz state
            this.index = (int) getArguments().get("index");
            this.correct = (int) getArguments().get("correct");
        }
        if (hostActivity instanceof TopicActivity) {
            this.question = ((TopicActivity) hostActivity).getQuestion(this.index);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_question, container, false);

        //display the question and answers
        TextView questionTitle = (TextView) v.findViewById(R.id.question);
        questionTitle.setText(this.question.getQuestionText());

        //get the radios
        RadioGroup answerBtns = (RadioGroup) v.findViewById(R.id.answers);
        for (int i = 0; i < 4; i++) {
            //set each radio
            RadioButton currentAnswer = (RadioButton) answerBtns.getChildAt(i);
            currentAnswer.setText(this.question.getAnswers().get(i));
            currentAnswer.setOnClickListener(onClickListener_radio);
        }

        Button btn = (Button) v.findViewById(R.id.btn_submit);
        btn.setOnClickListener(new SubmitOnClickListener(this.index, this.correct));

        return v;
    }

    //if they select an answer show the submit button
    private View.OnClickListener onClickListener_radio = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button submit = (Button) getView().findViewById(R.id.btn_submit);
            submit.setVisibility(View.VISIBLE);
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.hostActivity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    class SubmitOnClickListener implements View.OnClickListener
    {

        int index;
        int correct;

        public SubmitOnClickListener(int index, int correct) {
            //we need to keep this data to pass to the next activity(ies)
            this.index = index;
            this.correct = correct;
        }

        @Override
        public void onClick(View v) {
            if (hostActivity instanceof TopicActivity) {
                //get their answer
                RadioGroup answerButtons = (RadioGroup) getView().findViewById(R.id.answers);
                RadioButton checked = (RadioButton) getView().findViewById(answerButtons
                                                             .getCheckedRadioButtonId());
                String userAnswer =  (String) checked.getText();

                //tell the topic activity to load the answer fragment
                ((TopicActivity) hostActivity).loadAnswerFrag(userAnswer, this.index, this.correct);
            }
        }
    }

}