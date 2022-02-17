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
    inner class IntentListener : BroadcastReceiver() {
        init {
            Log.i("IntentListener", "I was created")
        }
        override fun onReceive(p0: Context?, intent: Intent?) {
            Toast.makeText(p0, "Starting download of new questions" , Toast.LENGTH_LONG).show()
            downloadFile()
            Log.i("IntentListener", "Inside Intent Lister")
        }
    }


    var downloadid: Long = 0
    inner class DownloadListener : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadid) Log.d("DOWNLOAD", "DONE")
        }
    }
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
            startDownloadCycle()
            startActivity(intent)
        }
    }

    fun startDownloadCycle () {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val sharedPrefs = getSharedPreferences("QuizDroid", MODE_PRIVATE)
        val time = sharedPrefs.getString("downloadTimer", "60").toString().toInt() * 1000 * 60

        val receiver = IntentListener()
        val intFilter = IntentFilter()
        intFilter.addAction("HELLO")
        registerReceiver(receiver, intFilter)
        val intent = Intent("HELLO")
        val pendingIntent =
            PendingIntent.getBroadcast(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.cancel(pendingIntent)
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time.toLong(),
            time.toLong(), pendingIntent)
    }

    fun downloadFile(){
        try{
            val filePath = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString() + "/questions.json"
            val isolatedFilePath = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString() + "/tempFile.json"
            val myFile = File(filePath)
            val isolatedFile = File(isolatedFilePath)
            Files.deleteIfExists(myFile.toPath())
            val sharedPrefs = getSharedPreferences("QuizDroid", MODE_PRIVATE)
            val url = sharedPrefs.getString("jsonLocation", "https://tinyurl.com/2uatdz8s") as String;
            Log.i("quizdroid", "Downloading file from : " + url)
            val new = DownloadListener()
            val uri = Uri.parse(url)
            val request = DownloadManager.Request(uri).setDescription("DummyFile").setTitle("Dummy")
                .setAllowedOverMetered(true).setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "questions.json"
                )
            val location = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadid = location.enqueue(request)
            registerReceiver(new, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
            /*
            Files.deleteIfExists(myFile.toPath())
            val myNewFile = File(isolatedFilePath)
            myNewFile.renameTo(File(filePath))
            */
            Log.i("quizdroid", "Download successful")
            Toast.makeText(this, "Downloaded new questions!" , Toast.LENGTH_LONG).show()
        }
        catch (e: Exception){
            Log.e("quizdroid", e.stackTraceToString())
            writeExistingRepositorytoFile()
            Toast.makeText(this, "Failed to download new questions" , Toast.LENGTH_LONG).show()
        }
    }

    fun writeExistingRepositorytoFile(){
        val repo = (this.application as QuizApp).myRepository as JSONRepository
        val gson = Gson();
        Log.i("quizdroid", "Backing up file")
        val filePath = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .toString() + "/questions.json"
        val outputJSON = gson.toJson(JSONTopic.listJSONTopics(HardCodedRepository().topics))
        Log.i("quizdroid", "Generated JSON: " + outputJSON)
        try {
            val myFile = File(filePath);
            val outputStream = FileOutputStream(myFile)
            outputStream.write(outputJSON.toByteArray())
            outputStream.close()
            Log.i("quizdroid", "Backup successful, theoretically")
        } catch (e: Exception) {
            Log.e("quizdroid", e.stackTraceToString())
        }
    }
}