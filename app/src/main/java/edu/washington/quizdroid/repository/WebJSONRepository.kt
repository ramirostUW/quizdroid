package edu.washington.quizdroid.repository

import android.os.Environment
import android.util.JsonReader
import android.util.Log
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL


class WebJSONRepository(override val topics: List<Topic>) : TopicRepository {

    constructor() : this(HardCodedRepository().topics)
    constructor(myPathToAFile: String) : this(WebJSONRepository.initializeRepositoryOnInit(myPathToAFile))

    companion object {
        fun initializeRepositoryOnInit(url: String) : List<Topic> {
            Log.i("WebJSONRepository", "Trying to make webjson")
            val topicList = emptyList<Topic>().toMutableList()
            try {
                Log.i("WebJSONRepository", "inside try catch")
                val readerOrWhatever = URL(url).openStream()
                Log.i("WebJSONRepository", "before declaring reader")
                val reader = JsonReader(InputStreamReader(readerOrWhatever, "UTF-8"))
                /*val reader = JsonReader(FileReader(
                    File(
                        Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        .toString() + "/SampleFile.json")
                ))*/
                reader.beginArray()
                val topicList = emptyList<Topic>().toMutableList()
                Log.i("WebJSONRepository", "About to enter while")
                while (reader.hasNext() && reader.peek().toString() != "END_ARRAY")
                {
                    Log.i("WebJSONRepository", "Iterating in while")
                    reader.beginObject()
                    var title = reader.nextName();
                    title = reader.nextString()
                    var description = reader.nextName()
                    description = reader.nextString()
                    var myQuestions = getQuestionFromReader(reader)
                    val myTopic = Topic(title, description, description, myQuestions)
                    topicList.add(myTopic)
                    reader.endObject()
                }
                reader.endArray()
                reader.close()
                Log.i("WebJSONRepository", "Allegedly made it")
                return topicList
            }
            catch (e: IOException) {
                Log.e("WebJSONRepository", e.stackTraceToString())
            }

            //val file = File("/storage/emulated/0/Download/questions.json") //File(filePath)
            //val reader = JsonReader(FileReader(file))
            //val directoryContents = file.list().joinToString(", ")
            Log.i("WebJSONRepository", "Made a webjson")
            return topicList
        }

        private fun getQuestionFromReader(reader: JsonReader): List<Quiz> {
            reader.nextName()
            reader.beginArray()
            val questionList = emptyList<Quiz>().toMutableList()
            while(reader.peek().toString() != "END_ARRAY")
            {
                reader.beginObject()
                reader.nextName()
                val questionText = reader.nextString()
                reader.nextName()
                var answer = reader.nextInt() - 1
                reader.nextName()
                reader.beginArray()
                var myAnswerList = emptyList<String>().toMutableList()
                myAnswerList.add(reader.nextString())
                myAnswerList.add(reader.nextString())
                myAnswerList.add(reader.nextString())
                myAnswerList.add(reader.nextString())
                reader.endArray()
                reader.endObject()
                val resultQuestion = Quiz(questionText, myAnswerList, answer)
                questionList.add(resultQuestion)
            }
            reader.endArray()
            return questionList;
        }
    }

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