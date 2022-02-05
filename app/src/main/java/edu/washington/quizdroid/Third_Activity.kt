package edu.washington.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView

class Third_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        val myTopic = intent.getStringExtra("topic").toString()
        val currentQuestion = intent.getIntExtra("currentQuestion", -1).toInt()
        val correctSoFar = intent.getIntExtra("correctSoFar", -1).toInt()

        val questionToDisplay = getQuestion(myTopic, currentQuestion);

        val optionA = findViewById(R.id.optionA) as RadioButton
        optionA.text = questionToDisplay["A"]
        val optionB = findViewById(R.id.optionB) as RadioButton
        optionB.text = questionToDisplay["B"]
        val optionC = findViewById(R.id.optionC) as RadioButton
        optionC.text = questionToDisplay["C"]
        val optionD = findViewById(R.id.optionD) as RadioButton
        optionD.text = questionToDisplay["D"]
        val question = findViewById(R.id.question) as TextView
        question.text = "" + questionToDisplay["question"]

        val button = findViewById(R.id.submit) as Button

        val answersDisplay = findViewById(R.id.answerOptions) as RadioGroup

        answersDisplay.setOnCheckedChangeListener {
                radioGroup, i ->  button.visibility = View.VISIBLE
        }

        button.setOnClickListener {
            val toAnswerPage = Intent(this, Fourth_Activity::class.java)
            toAnswerPage.putExtra("topic", myTopic)
            toAnswerPage.putExtra("correctSoFar", correctSoFar)
            toAnswerPage.putExtra("currentQuestion", currentQuestion)
            toAnswerPage.putExtra("userAnswer",
                (findViewById(answersDisplay.checkedRadioButtonId) as RadioButton).text.toString())
            toAnswerPage.putExtra("correctAnswer",
                questionToDisplay[questionToDisplay["correctAnswer"]])
            toAnswerPage.putExtra("isLastQuestion",
                questionToDisplay["maxQuestions"].equals("" + currentQuestion))
            startActivity(toAnswerPage)
            //Launch second activity
        }
    }

    fun getQuestion (myTopic:String, currentQuestion : Int): Map<String, String> {
        if (myTopic.equals("Math")) {return getMathQuestion(currentQuestion)}
        else if (myTopic.equals("Physics")) {return getPhysicsQuestion(currentQuestion)}
        else {return getMarvelQuestion(currentQuestion)}
    }

    fun getMathQuestion (currentQuestion : Int): Map<String, String> {
        val question1 = LinkedHashMap<String, String>()
        question1["question"] = "What is 1 + 1?"
        question1["A"] = "one"
        question1["B"] = "two"
        question1["C"] = "three"
        question1["D"] = "four"
        question1["correctAnswer"] = "B";

        val question2 = LinkedHashMap<String, String>()
        question2["question"] = "What is the answer to 2 + 1?"
        question2["A"] = "one"
        question2["B"] = "two"
        question2["C"] = "three"
        question2["D"] = "four"
        question2["correctAnswer"] = "C";

        val question3 = LinkedHashMap<String, String>()
        question3["question"] = "Why don't you tell me what is 3 + 1?"
        question3["A"] = "one"
        question3["B"] = "two"
        question3["C"] = "three"
        question3["D"] = "four"
        question3["correctAnswer"] = "D";

        val collectionOfQuestions = arrayOf(question1, question2, question3)

        val returnVal = collectionOfQuestions[currentQuestion - 1];
        returnVal["maxQuestions"] = "" + collectionOfQuestions.size

        return returnVal;

    }

    fun getMarvelQuestion (currentQuestion : Int): Map<String, String> {
        val question1 = LinkedHashMap<String, String>()
        question1["question"] = "How many Guardians of the Galaxy movies have been released?"
        question1["A"] = "one"
        question1["B"] = "two"
        question1["C"] = "three"
        question1["D"] = "four"
        question1["correctAnswer"] = "B";

        val question2 = LinkedHashMap<String, String>()
        question2["question"] = "How many Spiderman movies are in the MCU?"
        question2["A"] = "one"
        question2["B"] = "two"
        question2["C"] = "three"
        question2["D"] = "four"
        question2["correctAnswer"] = "C";

        val question3 = LinkedHashMap<String, String>()
        question3["question"] = "How many Avengers movies have been released?"
        question3["A"] = "one"
        question3["B"] = "two"
        question3["C"] = "three"
        question3["D"] = "four"
        question3["correctAnswer"] = "D";

        val collectionOfQuestions = arrayOf(question1, question2, question3)

        val returnVal = collectionOfQuestions[currentQuestion - 1];
        returnVal["maxQuestions"] = "" + collectionOfQuestions.size

        return returnVal;

    }

    fun getPhysicsQuestion (currentQuestion : Int): Map<String, String> {
        val question1 = LinkedHashMap<String, String>()
        question1["question"] = "If Force is 6 Newtons, and mass is 3 kg, acceleration is _ m/s^2."
        question1["A"] = "one"
        question1["B"] = "two"
        question1["C"] = "three"
        question1["D"] = "four"
        question1["correctAnswer"] = "B";

        val question2 = LinkedHashMap<String, String>()
        question2["question"] = "If Force is 9 Newtons, and mass is 3 kg, acceleration is _ m/s^2."
        question2["A"] = "one"
        question2["B"] = "two"
        question2["C"] = "three"
        question2["D"] = "four"
        question2["correctAnswer"] = "C";

        val question3 = LinkedHashMap<String, String>()
        question3["question"] = "If Force is 12 Newtons, and mass is 3 kg, acceleration is _ m/s^2."
        question3["A"] = "one"
        question3["B"] = "two"
        question3["C"] = "three"
        question3["D"] = "four"
        question3["correctAnswer"] = "D";

        val collectionOfQuestions = arrayOf(question1, question2, question3)

        val returnVal = collectionOfQuestions[currentQuestion - 1];
        returnVal["maxQuestions"] = "" + collectionOfQuestions.size

        return returnVal;

    }
}
