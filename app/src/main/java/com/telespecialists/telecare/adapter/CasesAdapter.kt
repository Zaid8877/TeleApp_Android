package com.telespecialists.telecare.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.telespecialists.telecare.R
import com.telespecialists.telecare.adapter.CasesAdapter.MyViewHolder
import com.telespecialists.telecare.data.Case
import com.telespecialists.telecare.ui.activities.DetailsActivity
import java.util.*

internal class CasesAdapter(private val mCtx: Context, list: MutableList<Case>) :
    RecyclerView.Adapter<MyViewHolder>(),
    Filterable {
    private var list: MutableList<Case> = ArrayList()
    private var copylist: List<Case>? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View =
            LayoutInflater.from(mCtx).inflate(R.layout.cases_item, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val cases = list[position]
        holder.mainTitle.text = "Case # ${cases.caseNumber}"
        holder.miniTitle.text = cases.facility.name
        holder.tagTitle.text = cases.caseType.name
        holder.statusBtn.text = cases.caseStatus.name
        holder.itemView.setOnClickListener {
            val i = Intent(mCtx, DetailsActivity::class.java)
            i.putExtra("id", cases.id.toString())
            i.putExtra("case_type", cases.caseType.name)
            mCtx.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    internal inner class MyViewHolder(itemView: View) :
        ViewHolder(itemView) {
        val mainTitle: AppCompatTextView = itemView.findViewById(R.id.mainTitle)
        val miniTitle: AppCompatTextView = itemView.findViewById(R.id.miniTitle)
        val tagTitle: AppCompatTextView = itemView.findViewById(R.id.tagTitle)
        val statusBtn: AppCompatTextView = itemView.findViewById(R.id.statusBtn)
    }

    init {
        this.list = list
        copylist = ArrayList(list)

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val filteredList: MutableList<Case> =
                    ArrayList<Case>()
                if (constraint.isEmpty()) {
                    filteredList.addAll(copylist!!)
                } else {
                    val filterPattern =
                        constraint.toString().toLowerCase().trim { it <= ' ' }
                    for (item in copylist!!) {
                        if (item.caseNumber.toString().toLowerCase().contains(filterPattern) ||
                            item.facility.name.toLowerCase().contains(filterPattern) ||
                            item.caseType.name.toLowerCase().contains(filterPattern)
                        ) {
                            filteredList.add(item)
                        }
                    }
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(
                constraint: CharSequence,
                results: FilterResults
            ) {
                list.clear()
                list.addAll(results.values as List<Case>)
                notifyDataSetChanged()
            }
        }
    }
}