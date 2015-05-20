package edu.washington.j75smith.quizdroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class PreferencesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        //get refs to user input
        RadioGroup radio_sources = (RadioGroup) findViewById(R.id.sources);
        EditText interval = (EditText) findViewById(R.id.when);

        //listen for go-ahead to apply changes
        findViewById(R.id.btn_apply).setOnClickListener(
                                    new ApplyOnClickListener(radio_sources, interval));
    }

    class ApplyOnClickListener implements View.OnClickListener {

        private RadioGroup radio_sources;
        private EditText interval;

        ApplyOnClickListener(RadioGroup radio_sources, EditText interval){
            this.radio_sources = radio_sources;
            this.interval = interval;
        }

        @Override
        public void onClick(View v) {
            RadioButton checked = (RadioButton) this.radio_sources
                                .findViewById(this.radio_sources.getCheckedRadioButtonId());
            if(checked == null){
                checked = (RadioButton) this.radio_sources.getChildAt(0);
            }
            String source = (String) checked.getText();
            int minutes = Integer.parseInt(this.interval.getText().toString());

            SharedPreferences sharedPref = getSharedPreferences("quizdroid", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("when", minutes);
            editor.putString("URL", source);
            editor.apply();

            int w = sharedPref.getInt("when", 999999);
            String u = sharedPref.getString("URL", "FUCK");

            Intent update = new Intent(PreferencesActivity.this, MainActivity.class);
            startActivity(update);
        }
    }
}
