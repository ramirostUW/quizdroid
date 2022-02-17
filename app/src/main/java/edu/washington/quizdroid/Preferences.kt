package edu.washington.quizdroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.app.AlarmManager
import android.app.DownloadManager
import android.app.PendingIntent
import android.content.*
import android.net.Uri
import android.os.Environment
import com.google.gson.Gson
import edu.washington.quizdroid.repository.HardCodedRepository
import edu.washington.quizdroid.repository.JSONRepository
import edu.washington.quizdroid.repository.JSONTopic
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files

class Preferences : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)
        val urlEdit = findViewById(R.id.urlEdit) as EditText
        val timerEdit = findViewById(R.id.dwnldTimerEdit) as EditText
        val sharedPrefs = getSharedPreferences("QuizDroid", MODE_PRIVATE)
        urlEdit.setText(sharedPrefs.getString("jsonLocation",
            "https://tinyurl.com/2uatdz8s"))
        timerEdit.setText(sharedPrefs.getString("downloadTimer", "60"))

        (findViewById(R.id.prefBtn) as Button).setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            sharedPrefs.edit().putString("jsonLocation", urlEdit.text.toString()).apply()
            sharedPrefs.edit().putString("downloadTimer", timerEdit.text.toString()).apply()
            startActivity(intent)
        }
    }
}