package edu.washington.quizdroid

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import edu.washington.quizdroid.repository.JSONRepository


class MainActivity : AppCompatActivity() {

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
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_GRANTED) {
            doFileStuff()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                0
            )
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
        val btnLayout = findViewById(R.id.btnLayout) as LinearLayout
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
}