package edu.washington.quizdroid

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log

class TestActivity : AppCompatActivity() {
    var downloadid: Long = 0
    inner class DownloadListener : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadid) Log.d("DOWNLOAD", "DONE")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val new = DownloadListener()
        val uri = Uri.parse("https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf")
        val url = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf"
        val request = DownloadManager.Request(uri).setDescription("DummyFile").setTitle("Dummy").setAllowedOverMetered(true).setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE).setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            url.substring(url.lastIndexOf("/") + 1)
        )

        val location = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadid = location.enqueue(request)

        registerReceiver(new, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }
}