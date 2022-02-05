package edu.washington.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class Fourth_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fourth)

        val myTopic = intent.getStringExtra("topic").toString()
        val currentQuestion = intent.getIntExtra("currentQuestion", -1).toInt()
        var correctSoFar = intent.getIntExtra("correctSoFar", -1).toInt()
        val correctAnswer = intent.getStringExtra("correctAnswer").toString()
        val userAnswer = intent.getStringExtra("userAnswer").toString()
        val isLastQuestion = intent.getBooleanExtra("isLastQuestion", true)

        val button = findViewById(R.id.nextQuestion) as Button

        val providedAnswerDisplay = findViewById(R.id.providedAnswer) as TextView
        providedAnswerDisplay.text = userAnswer

        val correctAnswerDisplay = findViewById(R.id.correctAnswer) as TextView
        correctAnswerDisplay.text = correctAnswer

        if(correctAnswer.equals(userAnswer)) {
            correctSoFar += 1;
            (findViewById(R.id.correctAnswerLabel) as TextView).visibility = View.INVISIBLE
            correctAnswerDisplay.text = "You got it right!"
        }

        val gradeDisplay = findViewById(R.id.currentGrade) as TextView
        gradeDisplay.text = "You have " + correctSoFar + " out of " + currentQuestion + " correct."
        if(!isLastQuestion) { button.setOnClickListener {
            val toQuestionPage = Intent(this, Third_Activity::class.java)
            toQuestionPage.putExtra("topic", myTopic)
            toQuestionPage.putExtra("correctSoFar", correctSoFar)
            toQuestionPage.putExtra("currentQuestion", currentQuestion + 1)
            startActivity(toQuestionPage)
            //Launch second activity

        }}

        if(isLastQuestion) {
            button.text = "Finish"
            button.setOnClickListener {
                val toHomePage = Intent(this, MainActivity::class.java)
                startActivity(toHomePage)
            }
        }
    }
}