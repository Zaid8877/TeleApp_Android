package com.telespecialists.telecare.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.telespecialists.telecare.R
import com.telespecialists.telecare.ui.fragments.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (fragment_container != null) {
            if (savedInstanceState != null) {
                return;
            }
            addFragment(HomeFragment())
        }

        home.setOnClickListener {
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
