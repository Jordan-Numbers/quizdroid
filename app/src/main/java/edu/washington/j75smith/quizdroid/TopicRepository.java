package edu.washington.j75smith.quizdroid;

import java.util.List;


/**
 * repository to store and retrieve topic domain objects
 * Created by jordans_macbook on 5/12/15.
 */
public interface TopicRepository {

    public List<topic> getAllTopics();

    public topic getTopic(String topicName);
}
