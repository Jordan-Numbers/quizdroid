package edu.washington.j75smith.quizdroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;


public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //listen for a topic to be selected
        this.findViewById(R.id.quiz_one).setOnClickListener(onClickListener);
        this.findViewById(R.id.quiz_two).setOnClickListener(onClickListener);
        this.findViewById(R.id.quiz_three).setOnClickListener(onClickListener);
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //go to a topic page sending which one was selected
            Intent next = new Intent(MainActivity.this, TopicActivity.class);
            next.putExtra("topicID", v.getId());
            startActivity(next);
        }
    };
}
