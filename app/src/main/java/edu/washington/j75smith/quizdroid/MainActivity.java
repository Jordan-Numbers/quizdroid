package edu.washington.j75smith.quizdroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.util.List;


public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //listen for a topic to be selected
        QuizApp app = (QuizApp) getApplication();

        List<topic> topics = app.getAllTopics();

        //get the buttons
        Button btn_one = (Button) this.findViewById(R.id.quiz_one);
        Button btn_two = (Button) this.findViewById(R.id.quiz_two);
        Button btn_three = (Button) this.findViewById(R.id.quiz_three);

        //set button text
        btn_one.setText(topics.get(0).getTitle());
        btn_two.setText(topics.get(1).getTitle());
        btn_three.setText(topics.get(2).getTitle());

        // listen to the buttons
        OnClickListener listener = new mainOnClickListener(topics);
        btn_one.setOnClickListener(listener);
        btn_two.setOnClickListener(listener);
        btn_three.setOnClickListener(listener);
    }

    class mainOnClickListener implements OnClickListener {

        List<topic> topics;

        mainOnClickListener(List<topic> topics){
            this.topics = topics;
        }

        @Override
        public void onClick(View v) {
            topic selectedTopic;
            //which one was selected?
            switch (v.getId()) {
                case R.id.quiz_one:
                    selectedTopic = topics.get(0);
                    break;

                case R.id.quiz_two:
                    selectedTopic = topics.get(1);
                    break;

                case R.id.quiz_three:
                    selectedTopic = topics.get(2);
                    break;

                default:
                    selectedTopic = topics.get(0);
                    break;
            }

            Intent next = new Intent(MainActivity.this, TopicActivity.class);
            next.putExtra("topic", selectedTopic);
            startActivity(next);
        }
    };
}
