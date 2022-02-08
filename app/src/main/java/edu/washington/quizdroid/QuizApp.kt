package edu.washington.quizdroid

import android.app.Application
import android.util.Log
import edu.washington.quizdroid.repository.HardCodedRepository

class QuizApp : Application() {
    val myRepository = createRepository();

    override fun onCreate() {
        super.onCreate()
        Log.i("QuizApp", "OnStart event fired")
    }

    private fun createRepository(): HardCodedRepository{
        return HardCodedRepository();
    }
}