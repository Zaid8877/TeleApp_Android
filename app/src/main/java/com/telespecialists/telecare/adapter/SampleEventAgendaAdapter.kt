package com.telespecialists.telecare.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.telespecialists.telecare.R
import java.text.SimpleDateFormat
import java.util.*

class SampleEventAgendaAdapter(private var context: Context)  {
   /* private var format: SimpleDateFormat = SimpleDateFormat(context.getString(R.string.header_date), Locale.ENGLISH)

    override fun getHeaderLayout() = R.layout.view_agenda_header

    override fun getHeaderItemView(view: View, day: Calendar) {
        val txtDayOfMonth = view.findViewById(R.id.view_agenda_day_of_month) as TextView
        val today = CalendarManager.instance!!.today

        if (DateHelper.sameDate(day, today)) {
            txtDayOfMonth.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
        } else {
            txtDayOfMonth.setTextColor(ContextCompat.getColor(context, R.color.text_light_color))
        }

        txtDayOfMonth.text = format.format(day.time)
    }

    override fun getEventItemView(view: View, event: CalendarEvent, position: Int) {
        val myEvent = event as MyCalendarEvent
        val myObject: SampleEvent? = myEvent.event as SampleEvent?

        if (myEvent.hasEvent()) {
            val name = view.findViewById(R.id.name) as TextView
            val description = view.findViewById(R.id.description) as TextView

            name.text = myObject?.name
            description.text = myObject?.description
        }

        view.setOnClickListener {
            Toast.makeText(view.context, "Item: ".plus(position), Toast.LENGTH_SHORT).show()
        }
    }

    override fun getEventLayout(hasEvent: Boolean): Int {
        return if (hasEvent) R.layout.view_agenda_event else R.layout.view_agenda_empty_event
    }*/
}