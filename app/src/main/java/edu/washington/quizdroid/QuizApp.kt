package edu.washington.quizdroid

import android.app.Application
import android.util.Log
import edu.washington.quizdroid.repository.HardCodedRepository
import edu.washington.quizdroid.repository.Quiz
import edu.washington.quizdroid.repository.Topic

public class QuizApp : Application() {
    public val myRepository = HardCodedRepository();
    public var quizIterator : Iterator<Quiz>? = null;
    public var currentQuestion: Quiz? = null;
    public var userAnswer: String? = null;
    public var currentTopic: Topic? = null;
    public var correctSoFar = -1;

    public override fun onCreate() {
        super.onCreate()
        Log.i("QuizApp", "OnStart event fired")
    }
}