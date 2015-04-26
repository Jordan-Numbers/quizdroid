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
        setContentView(R.layout.activity_main).setOnClickListener(onClickListener);
        this.findViewById(R.id.marvel_quiz).setOnClickListener(onClickListener);
        this.findViewById(R.id.math_quiz).setOnClickListener(onClickListener);
        this.findViewById(R.id.pop_quiz).setOnClickListener(onClickListener);
        this.findViewById(R.id.physics_quiz).setOnClickListener(onClickListener);
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent next = new Intent(this, topicActivity);
            next.putExtra("quizId",v.getId());
            startActivity(next);
        }
    };

}
