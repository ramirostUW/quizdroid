package edu.washington.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import edu.washington.quizdroid.repository.Quiz

class Third_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        val app = this.application as QuizApp
        app.currentQuestion = (app.quizIterator as Iterator).next()
        val questionToDisplay = app.currentQuestion as Quiz

        val optionA = findViewById(R.id.optionA) as RadioButton
        optionA.text = questionToDisplay.answers.get(0)
        val optionB = findViewById(R.id.optionB) as RadioButton
        optionB.text = questionToDisplay.answers.get(1)
        val optionC = findViewById(R.id.optionC) as RadioButton
        optionC.text = questionToDisplay.answers.get(2)
        val optionD = findViewById(R.id.optionD) as RadioButton
        optionD.text = questionToDisplay.answers.get(3)
        val question = findViewById(R.id.question) as TextView
        question.text = "" + questionToDisplay.question

        val button = findViewById(R.id.submit) as Button

        val answersDisplay = findViewById(R.id.answerOptions) as RadioGroup

        answersDisplay.setOnCheckedChangeListener {
                radioGroup, i ->  button.visibility = View.VISIBLE
        }

        button.setOnClickListener {
            val toAnswerPage = Intent(this, Fourth_Activity::class.java)
             app.userAnswer = (findViewById(answersDisplay.checkedRadioButtonId) as RadioButton).text.toString()
            toAnswerPage.flags = toAnswerPage.flags or Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(toAnswerPage)
            //Launch second activity
        }
    }
}
