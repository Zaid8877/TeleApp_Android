package com.telespecialists.telecare.ui.activities

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs
import com.telespecialists.telecare.R
import com.telespecialists.telecare.data.Login
import com.telespecialists.telecare.utils.Constants
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {

    private var email: String? = null
    private var password: String? = null
    private var progressDialog: ProgressDialog? = null
    private var data: Login? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = Prefs.getString(Constants.ID, "0")
        if (id != "0") {
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
                    generateTokenxVolley()
                } catch (e: Exception) {
                }
            }
        }
    }

    private fun generateTokenxVolley() {
        val myRequestQueue = Volley.newRequestQueue(this)
        val url = "http://uat.strokealert911.com/api/token"

        val myStringRequest = object :
            StringRequest(Method.POST, url,
                Response.Listener { response ->

                    val obj = JSONObject(response)
                    val token = obj.getString("access_token")
                    Log.e("token", token)
                    postDataVolley(token)
                }, Response.ErrorListener
                { error ->
                    Log.e("error", error.toString())
                }) {
            override fun getParams(): Map<String, String> {
                val MyData: MutableMap<String, String> =
                    HashMap()
                MyData["username"] = Constants.USER_NAME
                MyData["password"] = Constants.USER_PASSWORD
                MyData["grant_type"] = Constants.GRANT_TYPE
                return MyData
            }
        }


        myRequestQueue.add(myStringRequest)
    }

    private fun postDataVolley(token: String) {
        val myRequestQueue = Volley.newRequestQueue(this)
        val builder = Uri.Builder()
        builder.scheme("http")
            .authority("uat.strokealert911.com")
            .appendPath("api")
            .appendPath("physician")
            .appendPath("Auth")
            .appendQueryParameter("user_name", email!!)
            .appendQueryParameter("password", password!!)

        val myUrl: String = builder.build().toString()
        val myStringRequest = object :
            StringRequest(Method.POST, myUrl,
                Response.Listener { response ->
                    val model: Login = Gson().fromJson(response.toString(), Login::class.java)
                    saveData(model)
                    progressDialog!!.dismiss()

                }, Response.ErrorListener
                { error ->
                        progressDialog!!.dismiss()
                        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
                }) {

            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> =
                    HashMap()
                params["Authorization"] = "bearer $token"
                return params
            }
        }


        myRequestQueue.add(myStringRequest)

    }

    private fun saveData(model: Login) {
        try {
            Prefs.putString(Constants.ID, model.id)
            Prefs.putString(Constants.NPI_NUMBER, model.nPINumber)
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        } catch (e: Exception) {
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


}
