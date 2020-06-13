package com.telespecialists.telecare.ui.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.pixplicity.easyprefs.library.Prefs
import com.telespecialists.telecare.R
import com.telespecialists.telecare.data.Login
import com.telespecialists.telecare.data.TokenX
import com.telespecialists.telecare.retro.RetroClient
import com.telespecialists.telecare.retro.RetroServices
import com.telespecialists.telecare.utils.Constants
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {

    private var email: String? = null
    private var password: String? = null
    private var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //val id = "df79577f-3d6c-40f2-a752-85dc1ce21ca1"
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
                generateTokenxVolley()
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
        val url = "http://uat.strokealert911.com/api/physician/Auth"

        val myStringRequest = object :
            StringRequest(Method.POST, url,
                Response.Listener { response ->
                    Log.e("error", response.toString())

                }, Response.ErrorListener
                { error ->
                    Log.e("error", "" + error.toString())
                }) {
            override fun getParams(): Map<String, String> {
                val MyData: MutableMap<String, String> =
                    HashMap()
                MyData["user_name"] = email!!
                MyData["password"] = password!!
                return MyData
            }

            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> =
                    HashMap()
                params["Authorization"] = "bearer $token"
                return params
            }
        }


        myRequestQueue.add(myStringRequest)

    }

    private fun generateTokenxRetrofit() {
        val map: HashMap<String, Any> = HashMap()
        map["username"] = "telecare-api-001"
        map["password"] = "dGVsZWNhcmUtYXBpLTAwMTpOYXRpb25hbCQwOnZjYTZ0MXkhdzdeZW4wdTg0eDNt"
        map["grant_type"] = "password"
        Log.e("map", map.toString())
        val service: RetroServices =
            RetroClient.createService(RetroServices::class.java)

        val call = service.getToken(map)
        call.enqueue(object : retrofit2.Callback<TokenX> {
            override fun onResponse(
                call: retrofit2.Call<TokenX>,
                response: retrofit2.Response<TokenX>
            ) {
                if (response.isSuccessful) {
                    try {
                        val token = response.body()!!.accessToken
                        if (token.isNotEmpty()) {
                            Prefs.putString(Constants.SAVED_TOKEN, token)
                            postData()
                        }
                    } catch (e: Exception) {
                    }
                } else {
                    try {
                        Log.e("token: ", "token failed")
                    } catch (e: Exception) {
                    }

                }
            }

            override fun onFailure(call: retrofit2.Call<TokenX>, t: Throwable) {
                try {
                    Log.e("token: ", "token failed: " + t.message)
                } catch (e: Exception) {
                }


            }
        })
    }

    private fun postData() {
        val map: HashMap<String, Any> = HashMap()
        map["user_name"] = email!!
        map["password"] = password!!
        val service: RetroServices =
            RetroClient.createServiceWithToken(RetroServices::class.java)

        val call = service.login(map)
        call.enqueue(object : retrofit2.Callback<Login> {
            override fun onResponse(
                call: retrofit2.Call<Login>,
                response: retrofit2.Response<Login>
            ) {
                if (response.isSuccessful) {
                    try {
                        Log.e("data", response.toString())
                    } catch (e: Exception) {
                    }
                } else {
                    try {
                        Log.e("token: ", "token failed")
                    } catch (e: Exception) {
                    }

                }
            }

            override fun onFailure(call: retrofit2.Call<Login>, t: Throwable) {
                try {
                    Log.e("token: ", "token failed: " + t.message)
                } catch (e: Exception) {
                }


            }
        })
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
