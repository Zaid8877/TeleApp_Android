package com.telespecialists.telecare.ui.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.telespecialists.telecare.R
import com.telespecialists.telecare.data.Login
import com.telespecialists.telecare.data.TokenX
import com.telespecialists.telecare.retro.RetrofitClient
import com.telespecialists.telecare.retro.RetrofitServices
import com.telespecialists.telecare.utils.Constants
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    private var email: String? = null
    private var password: String? = null
    private var progressDialog: ProgressDialog? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = Prefs.getString(Constants.ID, "0")
        if (id != "0"){
            try {
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                finish()
            } catch (e: Exception) {
            }
        }
        setContentView(R.layout.activity_login)
        initStatusBar()
        progressDialog()
    }




    private fun initStatusBar() {
        val w: Window = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    fun userLogin(view: View) {
        email = userName.text.toString().trim()
        password = userPassword.text.toString().trim()
        when {
            TextUtils.isEmpty(email) -> {
                userName.isFocusable = true
                userName.error = "Enter email address!"
            }
            TextUtils.isEmpty(password) -> {
                userPassword.isFocusable = true
                userPassword.error = "Enter email address!"
            }
            else -> {
                try {
                    progressDialog!!.show()
                    generateToken()
                } catch (e: Exception) {
                }
            }
        }
    }

    private fun progressDialog() {
        try {
            progressDialog = ProgressDialog(this)
            progressDialog!!.setMessage("Authenticating...")
            progressDialog!!.setCanceledOnTouchOutside(false)
        } catch (e: Exception) {
        }
    }

    private fun generateToken() {
        val map: HashMap<String?, Any?> = HashMap()
        map["username"] = Constants.USER_NAME
        map["password"] = Constants.USER_PASSWORD
        map["grant_type"] = Constants.GRANT_TYPE

        Log.e("data", map.toString())

        val service: RetrofitServices = RetrofitClient.createServiceToken(RetrofitServices::class.java)
        val call = service.getToken(map)
        call.enqueue(object : Callback<TokenX> {
            override fun onResponse(
                call: Call<TokenX>,
                response: Response<TokenX>
            ) {
                if (response.isSuccessful) {
                    try {
                        val token = response.body()!!.accessToken
                        if (token.isNotEmpty()) {
                            Log.e("token: ", token)
                            Prefs.putString(Constants.SAVED_TOKEN, token)
                            Log.e("token", Prefs.getString(Constants.SAVED_TOKEN, ""))

                            postData()
                        }
                    } catch (e: Exception) {
                    }
                } else {
                    Log.e("token: ", "token failed")

                }
            }

            override fun onFailure(call: Call<TokenX>, t: Throwable) {
                Log.e("token: ", "token failed: "+t.message)


            }
        })
    }

    private fun postData() {
        val map: HashMap<String?, Any?> = HashMap()
        map["user_name"] = email
        map["password"] = password

        Log.e("data", map.toString())
        val service: RetrofitServices = RetrofitClient.createServiceWithToken(RetrofitServices::class.java)
        val call = service.login(map)
        call.enqueue(object : Callback<Login> {
            override fun onResponse(
                call: Call<Login>,
                response: Response<Login>
            ) {
                if (response.isSuccessful) {
                    try {
                        progressDialog!!.dismiss()
                        if (response.body() != null){
                            val id = response.body()!!.id
                            val nPINumber = response.body()!!.nPINumber
                            saveData(id, nPINumber)
                        }
                    } catch (e: Exception) {
                    }

                } else {
                    try {
                        progressDialog!!.dismiss()
                        Toast.makeText(this@LoginActivity, "Unable to login", Toast.LENGTH_LONG).show()
                    } catch (e: Exception) {
                    }
                }
            }

            override fun onFailure(call: Call<Login>, t: Throwable) {
                try {
                    Toast.makeText(
                        this@LoginActivity,
                        "Unable to login ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    progressDialog!!.dismiss()
                } catch (e: Exception) {
                }


            }
        })
    }

    private fun saveData(id: String?, nPINumber: String?) {
        try {
            Prefs.putString(Constants.ID, id)
            Prefs.putString(Constants.NPI_NUMBER, nPINumber)
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        } catch (e: Exception) {
        }
    }


}
