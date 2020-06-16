package com.telespecialists.telecare.ui.fragments

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
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


class ScheduleFragment : Fragment() {
    private var npiNumber: String? = null
    private var mCtx: Context? = null
    private var startTime: AppCompatTextView? = null
    private var endTime: AppCompatTextView? = null
    private var calenderHeader: AppCompatTextView? = null
    private var card: CardView? = null
    private lateinit var eventsCalendar: CompactCalendarView
    private var progressDialog: ProgressDialog? = null
    private var startdate: String? = null
    private var enddate: String? = null


    private fun progressDialog() {
        try {
            progressDialog = ProgressDialog(mCtx)
            progressDialog!!.setMessage("Loading...")
            progressDialog!!.setCanceledOnTouchOutside(false)
            progressDialog!!.show()
        } catch (e: Exception) {
        }
    }


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
        eventsCalendar = v.findViewById(R.id.compactcalendar_view)
        startTime = v.findViewById(R.id.startTime)
        endTime = v.findViewById(R.id.endTime)
        calenderHeader = v.findViewById(R.id.calenderHeader)
        card = v.findViewById(R.id.card)
        progressDialog()
        generateTokenxVolley()
        eventsCalendar.setUseThreeLetterAbbreviation(false);
        updateHeader()
        return v
    }

    private fun updateHeader() {
        val dateFormatForMonth = SimpleDateFormat("MMM - yyyy", Locale.getDefault())
        calenderHeader!!.text = dateFormatForMonth.format(eventsCalendar.firstDayOfCurrentMonth)
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
        try {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.YEAR, 0)
            calendar[Calendar.DAY_OF_YEAR] = 1
            val YearFirstDay = calendar.time
            calendar[Calendar.MONTH] = 11
            calendar[Calendar.DAY_OF_MONTH] = 31
            val YearLastDay = calendar.time
            val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            startdate = formatter.format(YearFirstDay)
            enddate = formatter.format(YearLastDay)
        } catch (e: Exception) {

        }


        val myRequestQueue = Volley.newRequestQueue(mCtx)
        val builder = Uri.Builder()
        builder.scheme("http")
            .authority("uat.strokealert911.com")
            .appendPath("api")
            .appendPath("physician")
            .appendPath("schedule")
            .appendQueryParameter("npi", npiNumber!!)
            .appendQueryParameter("start_date", startdate + "T00:00:00")
            .appendQueryParameter("end_date", enddate + "T00:00:00")

        val myUrl: String = builder.build().toString()
        val myStringRequest = object :
            StringRequest(
                Method.GET, myUrl,
                com.android.volley.Response.Listener { response ->
                    Log.e("response", response.toString())
                    val model: Schedulee =
                        Gson().fromJson(response.toString(), Schedulee::class.java)
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
            val date: Date =
                dateFormat.parse(item.startTime) //You will get date object relative to server/client timezone wherever it is parsed
            val formatter: DateFormat =
                SimpleDateFormat("dd-MM-yyyy") //If you need time just put specific format for time like 'HH:mm:ss'
            val dateStr: String = formatter.format(date)
            val finaldate = formatter.parse(dateStr) as Date
            val startDate = finaldate.time
            val ev1 = Event(
                Color.BLUE,
                startDate,
                item.startTime + "," + item.endTime
            )
            eventsCalendar.addEvent(ev1)

        }
        try {
            val formater: DateFormat = SimpleDateFormat("yyyy-MM-dd 00:00:00 'T'")

            val dateStr: String = formater.format(Calendar.getInstance().time)
            val finaldate = formater.parse(dateStr) as Date
            updateEventLabels(finaldate)
            progressDialog!!.dismiss()
        } catch (e: Exception) {
        }




        eventsCalendar.setListener(object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date) {
                updateEventLabels(dateClicked)

            }

            override fun onMonthScroll(firstDayOfNewMonth: Date) {
                updateHeader()
            }
        })


    }

    private fun updateEventLabels(dateClicked: Date) {
        Log.e("current", "" + dateClicked)

        val events: List<Event> = eventsCalendar.getEvents(dateClicked)
        if (events.isNotEmpty()) {
            events.forEach { i ->

                try {
                    val string = i.data.toString()
                    val parts = string.split(",".toRegex()).toTypedArray()
                    val first = parts[0]
                    val last = parts[1]
                    Log.e("first", first)
                    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    val date1: Date =
                        dateFormat.parse(first) //You will get date object relative to server/client timezone wherever it is parsed
                    Log.e("date1", "" + date1)

                    val date2: Date =
                        dateFormat.parse(last) //You will get date object relative to server/client timezone wherever it is parsed
                    val formatter: DateFormat =
                        SimpleDateFormat("hh:mm a, dd MMM yyyy") //If you need time just put specific format for time like 'HH:mm:ss'
                    val dateStr: String = formatter.format(date1)
                    Log.e("dateStr", "" + dateStr)
                    val dateStr2: String = formatter.format(date2)

                    startTime!!.text = "Start Time : $dateStr"
                    endTime!!.text = "End Time : $dateStr2"
                } catch (e: Exception) {
                }
            }
        } else {
            startTime!!.text = "Start Time : No Schedule"
            endTime!!.text = "End Time : No Schedule"
        }
    }


}
