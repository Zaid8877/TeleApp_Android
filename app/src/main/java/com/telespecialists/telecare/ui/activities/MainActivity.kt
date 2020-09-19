package com.telespecialists.telecare.ui.activities

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.pixplicity.easyprefs.library.Prefs
import com.telespecialists.telecare.R
import com.telespecialists.telecare.ui.fragments.*
import com.telespecialists.telecare.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        try {
            userName.text = Prefs.getString(Constants.PHYS_NAME, "")
            userNPI.text = "NPI # ${Prefs.getString(Constants.NPI_NUMBER, "")}"
        } catch (e: Exception) {
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar);
        if (fragment_container != null) {
            if (savedInstanceState != null) {
                return;
            }
            addFragment(HomeFragment())
        }

        search.setOnClickListener {
            drawer.openDrawer(GravityCompat.START)
        }

        logout.setOnClickListener {
            logoutFunc()
        }

        home.setOnClickListener {
            toolbarTitle.text = "Cases"

            home.setColorFilter(
                ContextCompat.getColor(this, R.color.colorPrimaryDark),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            egg.setColorFilter(
                ContextCompat.getColor(this, R.color.colorGray),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            emergent.setColorFilter(
                ContextCompat.getColor(this, R.color.colorGray),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            sc.setColorFilter(
                ContextCompat.getColor(this, R.color.colorGray),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            routine.setColorFilter(
                ContextCompat.getColor(this, R.color.colorGray),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            replaceFragment(HomeFragment())
        }
        egg.setOnClickListener {
            toolbarTitle.text = "Cases"
            home.setColorFilter(
                ContextCompat.getColor(this, R.color.colorGray),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            egg.setColorFilter(
                ContextCompat.getColor(this, R.color.colorPrimaryDark),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            emergent.setColorFilter(
                ContextCompat.getColor(this, R.color.colorGray),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            sc.setColorFilter(
                ContextCompat.getColor(this, R.color.colorGray),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            routine.setColorFilter(
                ContextCompat.getColor(this, R.color.colorGray),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            replaceFragment(EGGFragment())
        }
        emergent.setOnClickListener {
            toolbarTitle.text = "Cases"

            home.setColorFilter(
                ContextCompat.getColor(this, R.color.colorGray),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            egg.setColorFilter(
                ContextCompat.getColor(this, R.color.colorGray),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            emergent.setColorFilter(
                ContextCompat.getColor(this, R.color.colorPrimaryDark),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            sc.setColorFilter(
                ContextCompat.getColor(this, R.color.colorGray),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            routine.setColorFilter(
                ContextCompat.getColor(this, R.color.colorGray),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            replaceFragment(EmergentFragment())
        }
        sc.setOnClickListener {
            toolbarTitle.text = "Schedule"
            home.setColorFilter(
                ContextCompat.getColor(this, R.color.colorGray),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            egg.setColorFilter(
                ContextCompat.getColor(this, R.color.colorGray),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            emergent.setColorFilter(
                ContextCompat.getColor(this, R.color.colorGray),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            sc.setColorFilter(
                ContextCompat.getColor(this, R.color.colorPrimaryDark),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            routine.setColorFilter(
                ContextCompat.getColor(this, R.color.colorGray),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            replaceFragment(ScheduleFragment())

        }
        routine.setOnClickListener {
            toolbarTitle.text = "Cases"

            home.setColorFilter(
                ContextCompat.getColor(this, R.color.colorGray),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            egg.setColorFilter(
                ContextCompat.getColor(this, R.color.colorGray),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            emergent.setColorFilter(
                ContextCompat.getColor(this, R.color.colorGray),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            sc.setColorFilter(
                ContextCompat.getColor(this, R.color.colorGray),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            routine.setColorFilter(
                ContextCompat.getColor(this, R.color.colorPrimaryDark),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            replaceFragment(RoutineFragment())
        }

    }

    private fun logoutFunc() {
        try {
            AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Notice!")
                .setMessage("Are you sure to Logout")
                .setPositiveButton("Yes") { dialogInterface, i ->
                    Prefs.clear().commit()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("No", null)
                .show();

        } catch (e: Exception) {
        }


    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, fragment)
            .commit()
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

}
