package edu.washington.quizdroid

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.DownloadManager
import android.app.PendingIntent
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.ConnectivityManager
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
import edu.washington.quizdroid.repository.*
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import com.google.gson.Gson
import android.provider.Settings
import android.widget.TextView
import androidx.navigation.ActivityNavigator

class MainActivity : AppCompatActivity() {

    val testMode = false;
    var downloadid: Long = 0;
    var downloadFilePath: String = "";
    val QUESTIONS_PATH: String = Environment
        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        .toString() + "/questions.json"

    inner class DownloadListener : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadid) Log.d("DOWNLOAD", "DONE")
            val temporaryRepository = VariableFileRepository(downloadFilePath)
            writeExistingRepositorytoFile(QUESTIONS_PATH, temporaryRepository)
            Files.deleteIfExists(File(downloadFilePath).toPath())
        }
    }


    inner class IntentListener : BroadcastReceiver() {
        init {
            Log.i("IntentListener", "Starting a download cycle")
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
        checkPermissionsAndRedirect()
    }

    private fun checkPermissionsAndRedirect() {
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
        try {
            startDownloadCycle()
            val btnLayout = findViewById(R.id.btnLayout) as LinearLayout
            btnLayout.removeAllViews()
            val app = this.application as QuizApp
            app.myRepository = JSONRepository()/*VariableFileRepository(
                Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString() + "/SampleFile.json"
            )*/
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
        catch (e: Exception){
            val filePath = QUESTIONS_PATH
            var file = File(filePath)
            var fileExists = file.exists()

            (findViewById(R.id.errorLayout) as LinearLayout).visibility = View.VISIBLE
            (findViewById(R.id.reloadBtn) as Button).setOnClickListener {
                checkPermissionsAndRedirect()
                (findViewById(R.id.errorLayout) as LinearLayout).visibility = View.INVISIBLE
            };
            if(!fileExists)
            {
                (findViewById(R.id.errorLabel) as TextView).setText("Questions.json does not exist!")
                (findViewById(R.id.reloadBtn) as Button).text = "Generate File"
                (findViewById(R.id.reloadBtn) as Button).setOnClickListener {
                    writeExistingRepositorytoFile()
                    checkPermissionsAndRedirect()
                    (findViewById(R.id.errorLayout) as LinearLayout).visibility = View.INVISIBLE
                }
            }
        }
    }

    fun writeExistingRepositorytoFile(destination: String = QUESTIONS_PATH,
        repo: TopicRepository = (this.application as QuizApp).myRepository){
        Log.i("quizdroidDownloads", "Generating file")
        //val repo = (this.application as QuizApp).myRepository as TopicRepository
        val gson = Gson();
        Log.i("quizdroid", "Backing up file")
        val filePath = destination
        val outputJSON = gson.toJson(JSONTopic.listJSONTopics(repo.topics))
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
        val new = DownloadListener()
        val filePath = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .toString() + "/file.f.json"
        Files.deleteIfExists(File(filePath).toPath())
        val sharedPrefs = getSharedPreferences("QuizDroid", MODE_PRIVATE)
        val fileURL = sharedPrefs.getString("jsonLocation",
            "https://tinyurl.com/2uatdz8s") as String
        Log.i("downloadFile", "trying to download: " + fileURL)
        val uri = Uri.parse(fileURL)
        val request = DownloadManager.Request(uri).setAllowedOverMetered(true).setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE).setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            "file.f.json"
        )
        val location = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadid = location.enqueue(request)
        downloadFilePath = filePath;
        registerReceiver(new, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        checkPermissionsAndRedirect()
    }

    fun startDownloadCycle () {
        if(Settings.System.getInt(contentResolver,
            Settings.Global.AIRPLANE_MODE_ON, 0) != 0){
            AlertDialog.Builder(this).setTitle("Can't refresh quiz questions under Airplane Mode")
                .setMessage("Go to settings and turn it off?").setPositiveButton("Yes",
                    { dummy,dummy2 ->startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))})
                .setNegativeButton("No", null)
                .show()
        }
        else{
            val connManager = (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
            if(connManager.activeNetwork == null || connManager.getNetworkCapabilities(connManager.activeNetwork) == null){
                AlertDialog.Builder(this).setTitle("No wifi connection")
                    .setMessage("Go to settings and connect?").setPositiveButton("Yes",
                        { dummy,dummy2 ->startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))})
                    .setNegativeButton("No", null)
                    .show()
            }
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
}