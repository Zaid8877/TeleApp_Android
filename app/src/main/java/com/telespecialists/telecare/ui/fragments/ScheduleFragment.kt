package com.telespecialists.telecare.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pixplicity.easyprefs.library.Prefs
import com.telespecialists.telecare.R
import com.telespecialists.telecare.data.ScheduleeItem
import com.telespecialists.telecare.data.TokenX
import com.telespecialists.telecare.retro.RetroServices
import com.telespecialists.telecare.retro.RetrofitClient
import com.telespecialists.telecare.utils.Constants.NPI_NUMBER
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class ScheduleFragment : Fragment() {
    private var npiNumber: String? = null
    private var mCtx: Context? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mCtx = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_schedule, container, false)
        npiNumber = Prefs.getString(NPI_NUMBER, "0")

        generateToken()
        return v
    }


    private fun generateToken() {
        val map: HashMap<String, Any> = HashMap()
        map["username"] = "telecare-api-001"
        map["password"] = "dGVsZWNhcmUtYXBpLTAwMTpOYXRpb25hbCQwOnZjYTZ0MXkhdzdeZW4wdTg0eDNt"
        map["grant_type"] = "password"
        Log.e("data", map.toString())
        val apiService = RetrofitClient.getClientRetro().create(RetroServices::class.java)

        val call = apiService.getToken(map)
        call.enqueue(object : Callback<TokenX> {
            override fun onResponse(
                call: Call<TokenX>,
                response: Response<TokenX>
            ) {
                if (response.isSuccessful) {
                    try {
                        val token = response.body()!!.accessToken
                        if (token.isNotEmpty()) {
                            //Prefs.putString("bearer_token", token)
                            getData(token)
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

            override fun onFailure(call: Call<TokenX>, t: Throwable) {
                try {
                    Log.e("token: ", "token failed: " + t.message)
                } catch (e: Exception) {
                }


            }
        })
    }

    private fun getData(token: String) {
        val map: HashMap<String?, Any?> = HashMap()
        map["start_date"] = "2020-06-01T00:00"
        map["end_date"] = "2020-06-31T07:00"
        map["npi"] = npiNumber

        Log.e("data", map.toString())
        val apiService = RetrofitClient.getClientRetro().create(RetroServices::class.java)

        val call = apiService.scheduleCases("bearer $token", map)
        call.enqueue(object : Callback<List<ScheduleeItem>> {
            override fun onResponse(
                call: Call<List<ScheduleeItem>>,
                response: Response<List<ScheduleeItem>>
            ) {
                Log.e("shah", response.body().toString())

                if (response.isSuccessful) {
                    Log.e("shah", response.body().toString())

                    try {
                        if (response.body() != null) {
                            Log.e("data", response.body().toString())
                            /* list.addAll(response.body()!!.cases)
                             adapter!!.notifyDataSetChanged()
                             progressBar!!.visibility = View.GONE
                             swipe!!.isRefreshing = false*/

                        } else {
                            /* progressBar!!.visibility = View.GONE
                             swipe!!.isRefreshing = false*/
                            Log.e("shah", "body empty")

                        }
                    } catch (e: Exception) {
                        /*progressBar!!.visibility = View.GONE
                        swipe!!.isRefreshing = false*/
                        Log.e("shah", e.toString())

                    }

                } else {
                    try {
                        Log.e("shah", "response not successfull")

                        /* progressBar!!.visibility = View.GONE
                         swipe!!.isRefreshing = false
                         Toast.makeText(mContext, "Unable to login", Toast.LENGTH_LONG).show()*/
                    } catch (e: Exception) {
                        Log.e("shah", e.toString())

                    }
                }
            }

            override fun onFailure(call: Call<List<ScheduleeItem>>, t: Throwable) {
                try {
                    Log.e("shah", t.toString())

                    /* progressBar!!.visibility = View.GONE
                     swipe!!.isRefreshing = false
                     Toast.makeText(
                         mContext,
                         "Unable to login ${t.message}",
                         Toast.LENGTH_LONG
                     ).show()*/
                } catch (e: Exception) {
                }


            }
        })
    }


}
