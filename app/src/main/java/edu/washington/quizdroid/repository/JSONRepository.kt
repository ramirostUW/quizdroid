package edu.washington.quizdroid.repository

import java.io.File

class JSONRepository() : TopicRepository {

    private fun initializeRepositoryOnInit() : List<Topic> {
        val file: File = File("/storage/emulated/0/Download/questions.json")

        return emptyList()
    }

    override val topics: List<Topic> = initializeRepositoryOnInit()

    override fun getTopicNames(): List<String>{
        var returnVal = listOf<String>().toMutableList();
        for(topic in topics){
            returnVal.add(topic.title)
        }
        return returnVal;
    }

    override fun getQuestions(topicName: String): List<Quiz>{
        for(topic in topics){
            if(topic.title == topicName)
                return topic.questions;
        }
        return emptyList();
    }

    override fun getTopic(topicName: String): Topic{
        for(topic in topics){
            if(topic.title == topicName)
                return topic;
        }
        throw IllegalArgumentException("No topic found");
    }
}