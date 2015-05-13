package edu.washington.j75smith.quizdroid;

import android.app.Application;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom application for Quizdroid
 * Created by jordans_macbook on 5/10/15.
 */

public class QuizApp extends Application implements TopicRepository {

    private static QuizApp instance = null;
    private List<topic> topics;

    public QuizApp(){
        if( instance == null){
            instance = this;
            Log.i("Quizdroid", "QuizApp instance created");
        } else {
            throw new RuntimeException("Cannot create multiple instances of QuizApp");
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        this.topics = initRepository();
    }

    //returns all of the app's topic objects
    @Override
    public List<topic> getAllTopics() {
        return topics;
    }

    //Creates the repo by parsing the JSON source into
    // topic and question domains
    private List<topic> initRepository(){

        List<topic> results = new ArrayList<>();

        try {
            //get the topics
            JSONObject sourceJSON = new JSONObject(getJSON());
            JSONArray topicData = sourceJSON.getJSONArray("topics");
            for(int i = 0; i < topicData.length(); i++){

                //get each component for the topic
                JSONObject currentTopic = topicData.getJSONObject(i);
                String title = currentTopic.getString("title");
                String shortD = currentTopic.getString("desc");

                //We dont have long descriptions yet
                String longD = "";

                //get the questions for this topic
                List<question> topicQuestions = new ArrayList<>();
                JSONArray questions = currentTopic.getJSONArray("questions");
                for(int j = 0; j < questions.length(); j++){

                    //get the question
                    JSONObject currentQuestion = questions.getJSONObject(j);

                    //get the question components
                    String text = currentQuestion.getString("text");
                    int index = currentQuestion.getInt("answer");

                    //get the answers into a list
                    List<String> answerStrings = new ArrayList<>();
                    JSONArray answers = currentQuestion.getJSONArray("answers");
                    for(int l = 0; l < answers.length(); l++) {
                        answerStrings.add(answers.getString(l));
                    }
                    //create and add the new question
                    question newQuestion = new question(answerStrings, text, index);
                    topicQuestions.add(newQuestion);
                }

                topic newTopic = new topic(title, shortD, longD, topicQuestions);
                results.add(newTopic);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return results;
    }

    //helper method to grab the original JSON source
    private String getJSON() {
        String json = null;
        try {

            InputStream input = getAssets().open("questions.json");

            //setup
            int size = input.available();
            byte[] buffer = new byte[size];

            //read and make
            input.read(buffer);
            input.close();
            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;
    }
}
