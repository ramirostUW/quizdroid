package edu.washington.quizdroid.repository

import android.content.pm.PackageManager
import android.os.Environment
import java.io.File
import android.util.JsonReader
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import java.io.FileReader
import java.util.jar.Manifest


class VariableFileRepository() : TopicRepository {


    private fun initializeRepositoryOnInit(filePath: String) : List<Topic> {

        val secondFldr = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val secondsubdir = File("$secondFldr")
        //val filePath = Environment
        //    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        //   .toString() + "/questions.json"
        val file = File("/storage/emulated/0/Download/questions.json") //File(filePath)
        val reader = JsonReader(FileReader(file))
        //val directoryContents = file.list().joinToString(", ")
        reader.beginArray()
        val topicList = emptyList<Topic>().toMutableList()
        while (reader.hasNext() && reader.peek().toString() != "END_ARRAY")
        {
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
    override val topics: List<Topic> = initializeRepositoryOnInit(
        Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
               .toString() + "/questions.json")


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