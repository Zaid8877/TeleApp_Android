package com.telespecialists.telecare.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.events.calendar.views.EventsCalendar
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.CompactCalendarView.CompactCalendarViewListener
import com.github.sundeepk.compactcalendarview.domain.Event
import com.pixplicity.easyprefs.library.Prefs
import com.telespecialists.telecare.R
import com.telespecialists.telecare.data.*
import com.telespecialists.telecare.retro.RetrofitClient
import com.telespecialists.telecare.retro.RetrofitServices
import com.telespecialists.telecare.utils.Constants
import com.telespecialists.telecare.utils.Constants.NPI_NUMBER
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class ScheduleFragment : Fragment(), EventsCalendar.Callback {
    private var npiNumber : String? = null
    private var mCtx : Context? = null
    private var compactcalendar_view : CompactCalendarView? = null



    override fun onAttach(context: Context) {
        super.onAttach(context)
        mCtx = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_schedule, container, false)
        compactcalendar_view = v.findViewById(R.id.compactcalendar_view)
        compactcalendar_view!!.setUseThreeLetterAbbreviation(true)
        npiNumber = Prefs.getString(NPI_NUMBER, "0")

        generateToken()
        return v
    }


    private fun generateToken() {
        val map: HashMap<String?, Any?> = HashMap()
        map["username"] = Constants.USER_NAME
        map["password"] = Constants.USER_PASSWORD
        map["grant_type"] = Constants.GRANT_TYPE

        val service: RetrofitServices =
            RetrofitClient.createServiceToken(RetrofitServices::class.java)
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
                            Prefs.putString(Constants.SAVED_TOKEN, token)
                            Log.e(
                                "token: ",
                                Prefs.getString(
                                    Constants.SAVED_TOKEN,
                                    ""
                                )
                            )

                            getData()
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

    private fun getData() {
        val map: HashMap<String?, Any?> = HashMap()
        map["start_date"] = "2020-06-01T00:00"
        map["end_date"] = "2020-06-31T07:00"
        map["npi"] = npiNumber

        Log.e("data", map.toString())
        val service: RetrofitServices = RetrofitClient.createServiceWithToken(RetrofitServices::class.java)
        val call = service.scheduleCases(map)
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

                        }else{
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





















    @SuppressLint("SimpleDateFormat")
    private fun setEvents2() {
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val date: Date = dateFormat.parse("2020-06-13T19:00:00") //You will get date object relative to server/client timezone wherever it is parsed
        val formatter: DateFormat = SimpleDateFormat("dd-MM-yyyy") //If you need time just put specific format for time like 'HH:mm:ss'
        val dateStr: String = formatter.format(date)
        val finaldate = formatter.parse(dateStr) as Date
        val startDate = finaldate.time
        Log.e("finaldate", ""+startDate)

        val ev1 = Event(
            Color.YELLOW,
            startDate,
            "Some extra data that I want to store."
        )
        compactcalendar_view!!.addEvent(ev1)
        val ev2 = Event(
            Color.YELLOW,
            1591974954000L,
            "Helloooooooooooooooo"
        )
        compactcalendar_view!!.addEvent(ev2)
        compactcalendar_view!!.setListener(object : CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date) {
                val events: List<Event> = compactcalendar_view!!.getEvents(dateClicked)
                Toast.makeText(mCtx, "${events[0].data}",Toast.LENGTH_LONG).show()
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date) {
                Toast.makeText(mCtx, "Month was scrolled to: $firstDayOfNewMonth",Toast.LENGTH_LONG).show()

            }
        })

    }


    /*private fun setEvents() {
        val calendar = Calendar.getInstance()
        calendar.set(2020, 6, 8)
        eventsCalendar.setSelectionMode(eventsCalendar.MULTIPLE_SELECTION) //set mode of Calendar
            .setToday(today) //set today's date [today: Calendar]
            .setWeekStartDay(Calendar.SUNDAY, false) //set start day of the week as you wish [startday: Int, doReset: Boolean]
            .setCurrentSelectedDate(today) //set current date and scrolls the calendar to the corresponding month of the selected date [today: Calendar]
            .setDateTextFontSize(16f) //set font size for dates
            .setMonthTitleFontSize(16f) //set font size for title of the calendar
            .setWeekHeaderFontSize(16f) //set font size for week names
            .setCallback(this) //set the callback for EventsCalendar
            .addEvent(calendar) //set events on the EventsCalendar [c: Calendar]


        eventsCalendar.build()
        *//*val events: MutableList<EventDay> = ArrayList()
        val calendar = Calendar.getInstance()*//*

        //calendar.add(2020, 6, 8)
        //events.add(EventDay(calendar, R.drawable.custom_shape))

    }*/

    override fun onDayLongPressed(selectedDate: Calendar?) {

    }

    override fun onDaySelected(selectedDate: Calendar?) {

    }

    override fun onMonthChanged(monthStartDate: Calendar?) {

    }


}
