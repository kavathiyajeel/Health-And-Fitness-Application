package com.example.health_and_fitness.Medicine_Remainder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.health_and_fitness.Medicine_Remainder.Model.Reminder
import com.example.health_and_fitness.R
import kotlinx.coroutines.NonDisposableHandle.parent

class MedicineRemainder_Adapter(private val items: ArrayList<Reminder>,private val itemClickListener: ItemClickListener,) :
    RecyclerView.Adapter<MedicineRemainder_Adapter.MedicineRemainderViewHolder>() {


    class MedicineRemainderViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val card_txt_mnane = itemview.findViewById<TextView>(R.id.card_txt_mnane)
        val card_txt_dateday = itemview.findViewById<TextView>(R.id.card_txt_dateday)
        val card_txt_time = itemview.findViewById<TextView>(R.id.card_txt_time)
        val medicine_relativelayout =
            itemview.findViewById<RelativeLayout>(R.id.medicine_relativelayout)
        /*val card_btn_mdelete = itemview.findViewById<ImageView>(R.id.card_btn_mdelete)*/
        val card_txt_title = itemview.findViewById<TextView>(R.id.card_txt_title)
        val card_txt_repeatinfo = itemview.findViewById<TextView>(R.id.card_txt_repeatinfo)
        val card_btn_notifications = itemview.findViewById<ImageView>(R.id.card_btn_notifications)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineRemainderViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_medicine, parent, false)
        return MedicineRemainderViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MedicineRemainderViewHolder, position: Int) {
        val mremainder = items[position]
        holder.card_txt_mnane.text = mremainder.title
        holder.card_txt_dateday.text = (mremainder.date)
        holder.card_txt_time.text = (mremainder.time)
        holder.card_txt_title.text = mremainder.title?.substring(0, 1)!!.toUpperCase()


        if (mremainder.repeat == "true"){
            holder.card_txt_repeatinfo.text =
                ("Every " + mremainder.repeatNo + " " + mremainder.repeatType)
        }else{
            holder.card_txt_repeatinfo.text = "Repeat Off"
        }

        if (mremainder.active == "true"){
            holder.card_btn_notifications.setImageResource(R.drawable.ic_on_notification1)
        }else{
            holder.card_btn_notifications.setImageResource(R.drawable.ic_off_notification1)
        }

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(mremainder)
        }

        holder.itemView.setOnLongClickListener {
            itemClickListener.onItemLongClick(mremainder)
            true // Return true to indicate that the long click is consumed
        }
    }

    interface ItemClickListener {
        fun onItemClick(reminder: Reminder)
        fun onItemLongClick(reminder: Reminder)
    }
}