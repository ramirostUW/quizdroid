package edu.washington.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class Second_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val title = findViewById(R.id.topicTitle) as TextView
        val myTopic = intent.getStringExtra("topic").toString()
        title.text = myTopic//getTopicTitle();
        val description = findViewById(R.id.description) as TextView
        description.text = getTopicDescription(myTopic);

        val button = findViewById(R.id.takeQuiz) as Button
        button.setOnClickListener {
            val toQuestionPage = Intent(this, Third_Activity::class.java)
            toQuestionPage.putExtra("topic", myTopic)
            toQuestionPage.putExtra("correctSoFar", 0)
            toQuestionPage.putExtra("currentQuestion", 1)
            startActivity(toQuestionPage)
            //Launch second activity
        }
    }

    fun getTopicDescription(topic : String): String {
        val myDescriptions = LinkedHashMap<String, String>()
        myDescriptions["Math"] = "Take our math quiz to see if your Math Skills add up!";
        myDescriptions["Physics"] = "Take our physics quiz to see if your knowledge is a Force to be reckoned with!";
        myDescriptions["Marvel SuperHeroes"] = "Take our super quiz on superheroes!";
        return myDescriptions[topic] as String //"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus nec egestas tortor, in accumsan justo. Suspendisse sagittis tortor ligula, ut viverra turpis elementum ut. Nunc rutrum vitae dui nec placerat. Praesent iaculis risus sed dolor placerat porta. Suspendisse hendrerit placerat augue, eu ornare odio fringilla sed. Sed gravida sapien sed dui dictum rhoncus. Pellentesque in lobortis est. Proin consequat, ipsum pellentesque maximus dictum, massa mi rhoncus nulla, ac viverra massa tortor id lacus. ";
    }

    fun getTopicTitle(): String {
        return "Lorem ipsum dolor ";
    }
}