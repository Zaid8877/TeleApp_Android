package com.telespecialists.telecare.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.telespecialists.telecare.R
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {
    private var id: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        id = intent.getStringExtra("id")
        back.setOnClickListener {
            finish()
        }
    }
}
