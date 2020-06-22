package com.telespecialists.telecare.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.telespecialists.telecare.R
import com.telespecialists.telecare.data.CaseX
import com.telespecialists.telecare.ui.activities.MailDetailsActivity
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class RapidMailAdapter(private val mCtx: Context, list: MutableList<CaseX>) :
    RecyclerView.Adapter<RapidMailAdapter.MyViewHolder>() {
    private var list: MutableList<CaseX> = ArrayList()
    private var copylist: List<CaseX>? = null

    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val fromTitle: AppCompatTextView = itemView.findViewById(R.id.fromTitle)
        val htmlTitle: AppCompatTextView = itemView.findViewById(R.id.htmlTitle)
        val dateTitle: AppCompatTextView = itemView.findViewById(R.id.dateTitle)
    }

    init {
        this.list = list
        copylist = ArrayList(list)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val casesx = list[position]
        try {

            if (!casesx.isRead) {
                holder.fromTitle.setTypeface(holder.fromTitle.getTypeface(), Typeface.BOLD);
                holder.htmlTitle.setTypeface(holder.fromTitle.getTypeface(), Typeface.BOLD);
                holder.dateTitle.setTypeface(holder.fromTitle.getTypeface(), Typeface.BOLD);
            }
            holder.fromTitle.text = "From : ${casesx.from}"
            holder.htmlTitle.text = casesx.subject
            val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val date1: Date = dateFormat.parse(casesx.date)
            val formatter: DateFormat = SimpleDateFormat("hh:mm a, dd MMM yyyy")
            val dateStr: String = formatter.format(date1)
            holder.dateTitle.text = dateStr
        } catch (e: Exception) {
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(mCtx, MailDetailsActivity::class.java)
            intent.putExtra("casex", casesx.body)
            mCtx.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View =
            LayoutInflater.from(mCtx).inflate(R.layout.mail_item, parent, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size

    }


}