package com.telespecialists.telecare.ui.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.telespecialists.telecare.R
import com.telespecialists.telecare.adapter.RapidMailAdapter
import com.telespecialists.telecare.data.CaseX
import com.telespecialists.telecare.data.RapidMails
import com.telespecialists.telecare.utils.Constants
import kotlinx.android.synthetic.main.fragment_layout.*
import org.json.JSONObject


class HomeFragment : Fragment() {
    private var appBar: RelativeLayout? = null
    private var mContext: Context? = null
    private var casesList: RecyclerView? = null
    private var search: AppCompatImageView? = null
    private var seachBar: AppCompatEditText? = null
    private var adapter: RapidMailAdapter? = null
    private var isScrolling: Boolean? = false
    private var currentItems: Int? = null
    private var totalItems: Int? = null
    private var scrollOutItems: Int? = null
    private var manager: LinearLayoutManager? = null
    private var progressBar: ProgressBar? = null
    private var swipe: SwipeRefreshLayout? = null
    private var list: MutableList<CaseX> = ArrayList()
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
        appBar = v.findViewById(R.id.appBar)
        appBar!!.visibility = View.GONE
        casesList = v.findViewById(R.id.casesList)
        search = v.findViewById(R.id.search)
        seachBar = v.findViewById(R.id.seachBar)
        swipe = v.findViewById(R.id.swipe)
        swipe!!.isRefreshing = true
        progressBar = v.findViewById(R.id.progressBar)
        manager = LinearLayoutManager(mContext)
        casesList!!.layoutManager = manager
        adapter = RapidMailAdapter(mContext!!, list)
        casesList!!.adapter = adapter
        generateTokenxVolley()
        listerners()
        return v
    }

    private fun listerners() {
        swipe!!.setOnRefreshListener {
            list.clear()
            adapter!!.notifyDataSetChanged()
            swipe!!.isRefreshing = true
            SKIP = 0
            generateTokenxVolley()
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

                if (isScrolling!! && (currentItems!! + scrollOutItems!! == totalItems)) {
                    isScrolling = false
                    progressBar!!.visibility = View.VISIBLE
                    SKIP = SKIP!!.plus(PAGESIZE!!)
                    generateTokenxVolley()
                }
            }

        })
    }


    private fun generateTokenxVolley() {
        val myRequestQueue = Volley.newRequestQueue(mContext)
        val url = "http://uat.strokealert911.com/api/token"

        val myStringRequest = object :
            StringRequest(
                Method.POST, url,
                com.android.volley.Response.Listener { response ->

                    val obj = JSONObject(response)
                    val token = obj.getString("access_token")
                    Log.e("token", token)
                    getDataVolley(token)
                }, com.android.volley.Response.ErrorListener
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

    private fun getDataVolley(token: String) {
        val myRequestQueue = Volley.newRequestQueue(mContext)
        val builder = Uri.Builder()
        builder.scheme("http")
            .authority("uat.strokealert911.com")
            .appendPath("api")
            .appendPath("RapidsMails")
            .appendPath("list")
            .appendPath("RapidsMails")
            .appendQueryParameter("pageSize", PAGESIZE!!.toString())
            .appendQueryParameter("skip", SKIP!!.toString())

        val myUrl: String = builder.build().toString()
        val myStringRequest = object :
            StringRequest(
                Method.GET, myUrl,
                com.android.volley.Response.Listener { response ->
                    val model: RapidMails =
                        Gson().fromJson(response.toString(), RapidMails::class.java)
                    list.addAll(model.cases)
                    if (list.isNotEmpty()) {
                        adapter!!.notifyDataSetChanged()
                        progressBar!!.visibility = View.GONE
                        swipe!!.isRefreshing = false
                    } else {
                        progressBar!!.visibility = View.GONE
                        swipe!!.isRefreshing = false
                    }


                }, com.android.volley.Response.ErrorListener
                { error ->
                    progressBar!!.visibility = View.GONE
                    swipe!!.isRefreshing = false
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


}
