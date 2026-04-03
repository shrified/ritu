package com.srzone.ritu.Adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.srzone.ritu.Model.OvulationData
import com.srzone.ritu.R

class HomeRecyclerAdapter(
    private val list: MutableList<OvulationData>,
    private val activity: Activity
) : RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.home_recycler_item, viewGroup, false)
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val ovulationData = this.list[i]
        
        viewHolder.iconImg.setImageResource(ovulationData.icon)
        viewHolder.headingTv.text = ovulationData.heading
        
        val daysLeftStr = activity.getString(R.string.days_left)
        viewHolder.daysLeftTv.text = "(${ovulationData.daysLeft} $daysLeftStr)"
        
        viewHolder.dateTv.text = ovulationData.date
        viewHolder.dateTv.setTextColor(ContextCompat.getColor(activity, ovulationData.color))
        viewHolder.cardView.setCardBackgroundColor(ContextCompat.getColor(activity, ovulationData.bgColor))
    }

    override fun getItemCount(): Int {
        return this.list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.findViewById(R.id.cardView)
        val dateTv: TextView = view.findViewById(R.id.dateTv)
        val daysLeftTv: TextView = view.findViewById(R.id.daysLeftTv)
        val headingTv: TextView = view.findViewById(R.id.headingTv)
        val iconImg: ImageView = view.findViewById(R.id.iconImg)
    }
}
