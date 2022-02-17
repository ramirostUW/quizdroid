package edu.washington.quizdroid

import android.app.AlarmManager
import android.app.DownloadManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.gson.Gson
import edu.washington.quizdroid.repository.*
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files


class MainActivity : AppCompatActivity() {

    val testMode = false;

    var downloadid: Long = 0

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission: ", "Granted")
            } else {
                Log.i("Permission: ", "Denied")
            }
        }
    var myMenu: Menu? = null;


    inner class DownloadListener : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadid) Log.d("DOWNLOAD", "DONE")
        }
    }


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
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_GRANTED) {
            doFileStuff()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                0
            )
        }

        if(testMode)
        {
            val testButton = findViewById(R.id.testModebtn) as Button
            testButton.visibility = View.VISIBLE;
            testButton.setOnClickListener { downloadFile() }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        /*
        menu.findItem(R.menu.menu_main).setOnMenuItemClickListener {
            startActivity(Intent(this,Preferences::class.java))
            true
        }*/
        myMenu = menu;
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this,Preferences::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.i("Hopium", "Did this work")
        doFileStuff();
    }
    fun doFileStuff(){
        startDownloadCycle()
        val btnLayout = findViewById(R.id.btnLayout) as LinearLayout
        btnLayout.removeAllViews()
        val app = this.application as QuizApp
        app.myRepository = JSONRepository()
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
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time.toLong(),
            time.toLong(), pendingIntent)
    }
}