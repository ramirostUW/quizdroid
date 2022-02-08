package edu.washington.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import edu.washington.quizdroid.repository.Topic

class Second_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        val app = this.application as QuizApp

        val title = findViewById(R.id.topicTitle) as TextView
        val myTopic = app.currentTopic as Topic//intent.getStringExtra("topic").toString()
        title.text = myTopic.title//getTopicTitle();
        val description = findViewById(R.id.description) as TextView
        description.text = myTopic.longDescription;

        val button = findViewById(R.id.takeQuiz) as Button
        button.setOnClickListener {
            val toQuestionPage = Intent(this, Third_Activity::class.java)
            val app = this.application as QuizApp
            app.quizIterator = myTopic.questions.iterator()
            app.correctSoFar = 0;
            startActivity(toQuestionPage)
            //Launch second activity
        }
    }
}