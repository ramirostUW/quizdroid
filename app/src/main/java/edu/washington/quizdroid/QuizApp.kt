package edu.washington.quizdroid

import android.app.Application
import android.util.Log
import edu.washington.quizdroid.repository.HardCodedRepository
import edu.washington.quizdroid.repository.Quiz
import edu.washington.quizdroid.repository.Topic

class QuizApp : Application() {
    val myRepository = createRepository();
    var quizIterator : Iterator<Quiz>? = null;
    var currentQuestion: Quiz? = null;
    var userAnswer: String? = null;
    var currentTopic: Topic? = null;
    var correctSoFar = -1;

    override fun onCreate() {
        super.onCreate()
        Log.i("QuizApp", "OnStart event fired")
    }

    private fun createRepository(): HardCodedRepository{
        return HardCodedRepository();
    }
}