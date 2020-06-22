package com.telespecialists.telecare.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.telespecialists.telecare.R
import kotlinx.android.synthetic.main.activity_mail_details.*

class MailDetailsActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mail_details)


        if (intent != null) {

            try {
                val body = intent.getStringExtra("casex")
                Log.e("shah", body)
                details.settings.javaScriptEnabled = true;
                details.loadDataWithBaseURL("", body, "text/html", "UTF-8", "");
            } catch (e: Exception) {
            }
        }

        back.setOnClickListener {
            finish()
        }
    }
}