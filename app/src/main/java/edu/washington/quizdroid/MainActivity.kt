    package edu.washington.quizdroid

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginTop
import java.net.URI
import android.util.TypedValue
import android.view.ViewGroup


    class MainActivity : AppCompatActivity() {
    //val myTopics = arrayOf("Math", "Physics", "Marvel SuperHeroes")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnLayout = findViewById(R.id.btnLayout) as LinearLayout
        val app = this.application as QuizApp
        val myTopics = app.myRepository.getTopicNames()
        myTopics.iterator().forEach {
            var button = Button(this)
            val currentTopic = it;
            button.text = currentTopic;
            val margin = resources.getDimension(R.dimen.text_padding).toInt()
            val layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(margin, margin, margin, margin)
            button.setBackgroundColor(Color.BLUE)
            button.setTextColor(Color.WHITE)
            button.layoutParams = layoutParams
            button.setOnClickListener {
                val intent = Intent(this, Second_Activity::class.java)
                intent.putExtra("topic", currentTopic)
                app.currentTopic = app.myRepository.getTopic(currentTopic)
                intent.flags = intent.flags or Intent.FLAG_ACTIVITY_NO_HISTORY
                startActivity(intent)
                //Launch second activity
            }
            btnLayout.addView(button);
        }
    }
}