package com.telespecialists.telecare.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs
import com.telespecialists.telecare.R
import com.telespecialists.telecare.data.Schedulee
import com.telespecialists.telecare.utils.Constants
import com.telespecialists.telecare.utils.Constants.NPI_NUMBER
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class ScheduleFragment : Fragment(){
    private var npiNumber: String? = null
    private var mCtx: Context? = null
    private lateinit var eventsCalendar: CompactCalendarView


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
        generateTokenxVolley()
        eventsCalendar = v.findViewById(R.id.compactcalendar_view)

        eventsCalendar.setUseThreeLetterAbbreviation(false);

        return v
    }

    private fun generateTokenxVolley() {
        val myRequestQueue = Volley.newRequestQueue(mCtx)
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
        val myRequestQueue = Volley.newRequestQueue(mCtx)
        val builder = Uri.Builder()
        builder.scheme("http")
            .authority("uat.strokealert911.com")
            .appendPath("api")
            .appendPath("physician")
            .appendPath("schedule")
            .appendQueryParameter("npi", npiNumber!!)
            .appendQueryParameter("start_date", "2020-06-1T00:00:00")
            .appendQueryParameter("end_date", "2020-06-30T00:00:00")

        val myUrl: String = builder.build().toString()
        val myStringRequest = object :
            StringRequest(
                Method.GET, myUrl,
                com.android.volley.Response.Listener { response ->
                    Log.e("response", response.toString())
                    val model: Schedulee = Gson().fromJson(response.toString(), Schedulee::class.java)
                    setEvents2(model)
                }, com.android.volley.Response.ErrorListener
                { error ->

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

    @SuppressLint("SimpleDateFormat")
    private fun setEvents2(model: Schedulee) {
        model.forEach { item ->
            val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val date: Date = dateFormat.parse(item.startTime) //You will get date object relative to server/client timezone wherever it is parsed
            val formatter: DateFormat = SimpleDateFormat("dd-MM-yyyy") //If you need time just put specific format for time like 'HH:mm:ss'
            val dateStr: String = formatter.format(date)
            val finaldate = formatter.parse(dateStr) as Date
            val startDate = finaldate.time
            Log.e("finaldate", ""+startDate)
            val ev1 = Event(
                Color.BLUE,
                startDate,
                "Event Description"
            )
            eventsCalendar.addEvent(ev1)
           /* val ev2 = Event(
                Color.YELLOW,
                1591974954000L,
                "Helloooooooooooooooo"
            )
            eventsCalendar.addEvent(ev2)*/

        }


        eventsCalendar.setListener(object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date) {
                val events: List<Event> = eventsCalendar.getEvents(dateClicked)
                events.forEach {i ->
                    Toast.makeText(mCtx, "${i.timeInMillis}",Toast.LENGTH_LONG).show()

                }
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date) {
                Toast.makeText(mCtx, "Month was scrolled to: $firstDayOfNewMonth",Toast.LENGTH_LONG).show()

            }
        })


    }




}
