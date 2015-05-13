package edu.washington.j75smith.quizdroid;

import android.app.Application;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import java.util.List;
import java.util.Set;
import java.util.Map;

/**
 * Custom application for Quizdroid
 * Created by jordans_macbook on 5/10/15.
 */

public class QuizApp extends Application implements TopicRepository {

    private static QuizApp instance = null;
    private Map<String, topic> topics;

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

    //returns only the specified topic
    @Override
    public topic getTopic(String topicName) {
        return topics.get(topicName);
    }

    //returns all of the app's topic objects
    @Override
    public List<topic> getAllTopics() {
        //we need a list to hold the topics
        List<topic> allTopics = new List<topic>;

        // we need to grab all topics
        Set<String> keys = topics.keySet();
        for(String key : keys){
            allTopics.add(topics.get(key));
        }

        return allTopics;
    }

    //Creates the repo by parsing the JSON source into
    // topic and question domains
    private Map<String, topic> initRepository(){

        Map<String, topic> results = new HashMap<String, topic>;

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
                List<question> topicQuestions = new List<question>;
                JSONArray questions = currentTopic.getJSONArray("questions");
                for(int i = 0; i < questions.length(); i++){

                    //get the question
                    JSONObject currentQuestion = questions.getJSONObject(i);

                    //get the question components
                    String text = currentQuestion.getString("text");
                    int index = currentQuestion.getInt("answer");

                    //get the answers into a list
                    List<String> answerStrings = new List<String>;
                    JSONArray answers = currentQuestion.getJSONArray("answers");
                    for(int i = 0; i < answers.length(); i++) {
                        answerStrings.add(answers.getString(i));
                    }

                    //dreate and add the new question
                    question newQuestion = new question(answerStrings, text, index);
                    topicQuestions.add(newQuestion);
                }

                topic newTopic = new topic(title, shortD, longD, topicQuestions);
                results.put(title, newTopic);
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
