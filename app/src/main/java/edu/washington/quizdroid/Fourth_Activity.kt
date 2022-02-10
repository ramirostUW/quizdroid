package edu.washington.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import edu.washington.quizdroid.repository.Quiz
import edu.washington.quizdroid.repository.Topic

class Fourth_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fourth)
        val app = this.application as QuizApp

        val currentQuestion = (this.application as QuizApp).currentQuestion as Quiz
        var correctSoFar = app.correctSoFar
        val correctAnswer = currentQuestion.answers.get(currentQuestion.correctAnswer)//intent.getStringExtra("correctAnswer").toString()
        val userAnswer = app.userAnswer
        val isLastQuestion = (!(app.quizIterator as Iterator<Quiz>).hasNext())

        val button = findViewById(R.id.nextQuestion) as Button

        val providedAnswerDisplay = findViewById(R.id.providedAnswer) as TextView
        providedAnswerDisplay.text = userAnswer

        val correctAnswerDisplay = findViewById(R.id.correctAnswer) as TextView
        correctAnswerDisplay.text = correctAnswer

        if(correctAnswer.equals(userAnswer)) {
            correctSoFar += 1;
            app.correctSoFar +=1;
        }

        val gradeDisplay = findViewById(R.id.currentGrade) as TextView
        gradeDisplay.text = "You have " + correctSoFar + " out of " +
                ((app.currentTopic as Topic).questions.indexOf(currentQuestion) + 1)+
                " correct."
        if(!isLastQuestion) { button.setOnClickListener {
            val toQuestionPage = Intent(this, Third_Activity::class.java)
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