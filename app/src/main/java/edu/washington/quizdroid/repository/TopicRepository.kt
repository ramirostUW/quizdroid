package edu.washington.quizdroid.repository

interface TopicRepository {
    val topics : List<Topic>;

    fun getTopicNames(): List<String>;
    fun getQuestions(topicName: String): List<Quiz>
    fun getTopic(topicName: String): Topic

}