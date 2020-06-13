package com.telespecialists.telecare.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.pixplicity.easyprefs.library.Prefs
import com.telespecialists.telecare.R
import com.telespecialists.telecare.adapter.CasesAdapter
import com.telespecialists.telecare.data.Case
import com.telespecialists.telecare.data.Cases
import com.telespecialists.telecare.data.TokenX
import com.telespecialists.telecare.retro.RetroServices
import com.telespecialists.telecare.retro.RetrofitClient
import com.telespecialists.telecare.utils.Constants
import kotlinx.android.synthetic.main.fragment_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class EmergentFragment : Fragment() {
    private var id: String? = null
    private var mContext: Context? = null
    private var casesList: RecyclerView? = null
    private var search: AppCompatImageView? = null
    private var seachBar: AppCompatEditText? = null
    private var adapter: CasesAdapter? = null
    private var isScrolling: Boolean? = false
    private var currentItems: Int? = null
    private var totalItems: Int? = null
    private var scrollOutItems: Int? = null
    private var manager: LinearLayoutManager? = null
    private var progressBar: ProgressBar? = null
    private var swipe: SwipeRefreshLayout? = null
    private var list: MutableList<Case> = ArrayList()

    private var PAGESIZE: Int? = 10
    private var SKIP: Int? = 0


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_layout, container, false)
        id = Prefs.getString(Constants.ID, "")
        casesList = v.findViewById(R.id.casesList)
        search = v.findViewById(R.id.search)
        seachBar = v.findViewById(R.id.seachBar)
        swipe = v.findViewById(R.id.swipe)
        swipe!!.isRefreshing = true
        progressBar = v.findViewById(R.id.progressBar)
        manager = LinearLayoutManager(mContext)
        casesList!!.layoutManager = manager
        adapter = CasesAdapter(mContext!!, list)
        casesList!!.adapter = adapter
        generateToken()
        listerners()
        return v
    }

    private fun listerners() {

        swipe!!.setOnRefreshListener {
            list.clear()
            adapter!!.notifyDataSetChanged()
            swipe!!.isRefreshing = true
            SKIP = 0
            generateToken()
        }

        search!!.setOnClickListener {
            if (seachBar!!.visibility == View.VISIBLE) {
                seachBar!!.visibility = View.GONE
                toolbarTitle.visibility = View.VISIBLE
            } else {
                seachBar!!.visibility = View.VISIBLE
                toolbarTitle.visibility = View.GONE
            }
        }

        casesList!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true

                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentItems = manager!!.childCount
                totalItems = manager!!.itemCount
                scrollOutItems = manager!!.findFirstCompletelyVisibleItemPosition()
                Log.e("totalItems", totalItems.toString())
                Log.e("currentItems", currentItems.toString())
                Log.e("scrollOutItems", scrollOutItems.toString())
                if (isScrolling!! && (currentItems!! + scrollOutItems!! == totalItems)) {
                    isScrolling = false
                    progressBar!!.visibility = View.VISIBLE
                    SKIP = SKIP!!.plus(PAGESIZE!!)
                    generateToken()
                }
            }

        })
    }

    private fun generateToken() {
        val map: HashMap<String, Any> = HashMap()
        map["username"] = "telecare-api-001"
        map["password"] = "dGVsZWNhcmUtYXBpLTAwMTpOYXRpb25hbCQwOnZjYTZ0MXkhdzdeZW4wdTg0eDNt"
        map["grant_type"] = "password"


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
        map["phyId"] = id!!
        map["pageSize"] = PAGESIZE!!
        map["skip"] = SKIP!!
        map["caseType"] = "9,10,11,12"


        Log.e("data", map.toString())
        val apiService = RetrofitClient.getClientRetro().create(RetroServices::class.java)

        val call = apiService.cases("bearer $token", map)
        call.enqueue(object : Callback<Cases> {
            override fun onResponse(
                call: Call<Cases>,
                response: Response<Cases>
            ) {
                if (response.isSuccessful) {
                    try {
                        Log.e("data", response.body()!!.cases.toString())
                        Log.e("size", "" + response.body()!!.cases.size)
                        if (response.body() != null) {
                            list.addAll(response.body()!!.cases)
                            adapter!!.notifyDataSetChanged()
                            progressBar!!.visibility = View.GONE
                            swipe!!.isRefreshing = false

                        } else {
                            progressBar!!.visibility = View.GONE
                            swipe!!.isRefreshing = false
                        }
                    } catch (e: Exception) {
                        progressBar!!.visibility = View.GONE
                        swipe!!.isRefreshing = false
                    }

                } else {
                    try {
                        progressBar!!.visibility = View.GONE
                        swipe!!.isRefreshing = false
                        Toast.makeText(mContext, "Unable to login", Toast.LENGTH_LONG).show()
                    } catch (e: Exception) {
                    }
                }
            }

            override fun onFailure(call: Call<Cases>, t: Throwable) {
                try {
                    progressBar!!.visibility = View.GONE
                    swipe!!.isRefreshing = false
                    Toast.makeText(
                        mContext,
                        "Unable to login ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                } catch (e: Exception) {
                }


            }
        })
    }


}
