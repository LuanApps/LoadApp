package com.udacity

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.udacity.databinding.ActivityDetailBinding
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val action = intent.action
        if (action == "CHECK_STATUS") {
            val notificationId = intent.getIntExtra("notification_id", 0)
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(notificationId)
        }

        val fileName = intent.getStringExtra("file_name").toString()
        val isSuccess = intent.getBooleanExtra("file_status", false)

        Log.i("DetailActivity", "File Name: $fileName")
        Log.i("DetailActivity", "Success Status: $isSuccess")

        binding.contentDetail.filename.text = fileName
        binding.contentDetail.status.text = if (isSuccess) "Success" else "Failure"
    }

}
