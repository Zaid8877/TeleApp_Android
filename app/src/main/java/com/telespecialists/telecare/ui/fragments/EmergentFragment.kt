package com.telespecialists.telecare.ui.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs
import com.telespecialists.telecare.R
import com.telespecialists.telecare.adapter.CasesAdapter
import com.telespecialists.telecare.data.Case
import com.telespecialists.telecare.data.Cases
import com.telespecialists.telecare.utils.Constants
import kotlinx.android.synthetic.main.fragment_layout.*
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 */
class EmergentFragment : Fragment() {
    private var id: String? = null
    private var mContext: Context? = null
    private var casesList: RecyclerView? = null
    private var casesList2: RecyclerView? = null
    private var search: AppCompatImageView? = null
    private var seachBar: AppCompatEditText? = null
    private var adapter: CasesAdapter? = null
    private var adapter2: CasesAdapter? = null
    private var isScrolling: Boolean? = false
    private var isScrolling2: Boolean? = false
    private var currentItems: Int? = null
    private var currentItems2: Int? = null
    private var totalItems: Int? = null
    private var totalItems2: Int? = null
    private var scrollOutItems: Int? = null
    private var scrollOutItems2: Int? = null
    private var manager: LinearLayoutManager? = null
    private var manager2: LinearLayoutManager? = null
    private var progressBar: ProgressBar? = null
    private var swipe: SwipeRefreshLayout? = null
    private var swipe2: SwipeRefreshLayout? = null
    private var list: MutableList<Case> = ArrayList()
    private var list2: MutableList<Case> = ArrayList()

    private var PAGESIZE: Int? = 10
    private var PAGESIZE2: Int? = 10
    private var SKIP: Int? = 0
    private var SKIP2: Int? = 0
    private var searchTAG: String? = null
    private var searchView: SearchView? = null
    private var queryTextListener: SearchView.OnQueryTextListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        val searchItem = menu.findItem(R.id.search)

        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
        }
        if (searchView != null) {
            val imvClose: ImageView =
                searchView!!.findViewById(androidx.appcompat.R.id.search_close_btn)
            imvClose.setImageResource(R.drawable.ic_baseline_close_24)
            queryTextListener = object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    searchTAG = newText.toString()
                    if (!searchTAG.isNullOrEmpty()) {
                        swipe2!!.visibility = View.VISIBLE
                        swipe2!!.isRefreshing = true
                        list.clear()
                        list2.clear()
                        adapter!!.notifyDataSetChanged()
                        adapter2!!.notifyDataSetChanged()
                        generateTokenxVolley2()
                    } else {
                        swipe!!.visibility = View.VISIBLE
                        swipe!!.isRefreshing = true
                        list.clear()
                        list2.clear()
                        adapter!!.notifyDataSetChanged()
                        adapter2!!.notifyDataSetChanged()
                        generateTokenxVolley()
                    }
                    return true
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    Log.i("onQueryTextSubmit", query)
                    return true
                }
            }
            searchView!!.setOnQueryTextListener(queryTextListener)
        }
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search ->                 // Not implemented here
                return false
            else -> {
            }
        }
        searchView!!.setOnQueryTextListener(queryTextListener)
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_layout, container, false)
        id = Prefs.getString(Constants.ID, "")
        casesList = v.findViewById(R.id.casesList)
        casesList2 = v.findViewById(R.id.casesList2)
        search = v.findViewById(R.id.search)
        seachBar = v.findViewById(R.id.seachBar)
        swipe = v.findViewById(R.id.swipe)
        swipe2 = v.findViewById(R.id.swipe2)
        swipe!!.isRefreshing = true
        progressBar = v.findViewById(R.id.progressBar)
        manager = LinearLayoutManager(mContext)
        manager2 = LinearLayoutManager(mContext)
        casesList!!.layoutManager = manager
        casesList2!!.layoutManager = manager2
        adapter = CasesAdapter(mContext!!, list)
        casesList!!.adapter = adapter
        adapter2 = CasesAdapter(mContext!!, list2)
        casesList2!!.adapter = adapter2
        generateTokenxVolley()
        listerners()
        return v
    }

    private fun listerners() {
        swipe!!.setOnRefreshListener {
            list.clear()
            list2.clear()
            adapter!!.notifyDataSetChanged()
            adapter2!!.notifyDataSetChanged()
            swipe!!.isRefreshing = true
            SKIP = 0
            generateTokenxVolley()

        }

        swipe2!!.setOnRefreshListener {
            list.clear()
            list2.clear()
            adapter!!.notifyDataSetChanged()
            adapter2!!.notifyDataSetChanged()
            swipe2!!.isRefreshing = true
            SKIP2 = 0
            generateTokenxVolley2()

        }

        /* search!!.setOnClickListener {
             if (seachBar!!.visibility == View.VISIBLE) {
                 seachBar!!.visibility = View.GONE
                 toolbarTitle.visibility = View.VISIBLE
             } else {
                 seachBar!!.visibility = View.VISIBLE
                 toolbarTitle.visibility = View.GONE
             }
         }*/

        seachBar!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchTAG = s.toString()
                if (!searchTAG.isNullOrEmpty()){
                    swipe2!!.visibility = View.VISIBLE
                    swipe2!!.isRefreshing = true
                    list.clear()
                    list2.clear()
                    adapter!!.notifyDataSetChanged()
                    adapter2!!.notifyDataSetChanged()
                    generateTokenxVolley2()
                }else{
                    swipe!!.visibility = View.VISIBLE
                    swipe!!.isRefreshing = true
                    list.clear()
                    list2.clear()
                    adapter!!.notifyDataSetChanged()
                    adapter2!!.notifyDataSetChanged()
                    generateTokenxVolley()
                }



            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

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

        casesList2!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling2 = true

                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentItems2 = manager2!!.childCount
                totalItems2 = manager2!!.itemCount
                scrollOutItems2 = manager2!!.findFirstCompletelyVisibleItemPosition()
                if (isScrolling2!! && (currentItems2!! + scrollOutItems2!! == totalItems2)) {
                    isScrolling2 = false
                    progressBar!!.visibility = View.VISIBLE
                    SKIP2 = SKIP2!!.plus(PAGESIZE2!!)
                    generateTokenxVolley2()
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
    private fun generateTokenxVolley2() {
        val myRequestQueue = Volley.newRequestQueue(mContext)
        val url = "http://uat.strokealert911.com/api/token"
        val myStringRequest = object :
            StringRequest(
                Method.POST, url,
                com.android.volley.Response.Listener { response ->

                    val obj = JSONObject(response)
                    val token = obj.getString("access_token")
                    Log.e("token", token)
                    getData2Volley(token)
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
            .appendPath("cases")
            .appendPath("list")
            .appendPath("by-physicain")
            .appendQueryParameter("phyId", id!!)
            .appendQueryParameter("pageSize", PAGESIZE!!.toString())
            .appendQueryParameter("skip", SKIP!!.toString())
            .appendQueryParameter("caseType", "9,10,11,12")

        val myUrl: String = builder.build().toString()
        val myStringRequest = object :
            StringRequest(
                Method.GET, myUrl,
                com.android.volley.Response.Listener { response ->
                    val model: Cases = Gson().fromJson(response.toString(), Cases::class.java)
                    list.addAll(model.cases)
                    if (list.isNotEmpty()) {
                        swipe!!.visibility = View.VISIBLE
                        swipe2!!.visibility = View.GONE
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
    private fun getData2Volley(token: String) {
        val myRequestQueue = Volley.newRequestQueue(mContext)
        val builder = Uri.Builder()
        builder.scheme("http")
            .authority("uat.strokealert911.com")
            .appendPath("api")
            .appendPath("cases")
            .appendPath("list")
            .appendPath("by-status")
            .appendQueryParameter("SearchText", searchTAG!!)
            .appendQueryParameter("phyId", id!!)
            .appendQueryParameter("pageSize", PAGESIZE2!!.toString())
            .appendQueryParameter("skip", SKIP2!!.toString())
            .appendQueryParameter("caseType", "9,10,11,12")


        val myUrl: String = builder.build().toString()
        val myStringRequest = object :
            StringRequest(
                Method.GET, myUrl,
                com.android.volley.Response.Listener { response ->
                    val model: Cases = Gson().fromJson(response.toString(), Cases::class.java)
                    list2.addAll(model.cases)
                    if (!list2.isNullOrEmpty()) {
                        swipe!!.visibility = View.GONE
                        swipe2!!.visibility = View.VISIBLE
                        adapter2!!.notifyDataSetChanged()
                        progressBar!!.visibility = View.GONE
                        swipe2!!.isRefreshing = false
                        swipe!!.isRefreshing = false
                        Log.e("SHAH", list2.toString())
                    } else {
                        progressBar!!.visibility = View.GONE
                        swipe!!.isRefreshing = false
                        swipe2!!.isRefreshing = false
                    }


                }, com.android.volley.Response.ErrorListener
                { error ->
                    progressBar!!.visibility = View.GONE
                    swipe2!!.isRefreshing = false
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

