 package com.example.health_and_fitness.Activitys

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.health_and_fitness.R
import com.example.health_and_fitness.Medicine_Remainder.Activitys.Add_Medicine
import com.example.health_and_fitness.Medicine_Remainder.Activitys.Edit_Medicine
import com.example.health_and_fitness.Medicine_Remainder.Database.RemainderDatabase
import com.example.health_and_fitness.Medicine_Remainder.MedicineRemainder_Adapter
import com.example.health_and_fitness.Medicine_Remainder.Model.Reminder
import com.google.android.material.floatingactionbutton.FloatingActionButton


class Medicine_Remainder : AppCompatActivity(),MedicineRemainder_Adapter.ItemClickListener {

    private lateinit var recyclerview_medicine: RecyclerView
    private lateinit var no_remainer_messsage: TextView
    private lateinit var add_medicine_remainder: FloatingActionButton



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicine_remainder)
        /// Enable the home button (back button)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        val colorDrawable = ColorDrawable(Color.parseColor("#000000"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
        supportActionBar?.title = "Medicine Remainder"

        recyclerview_medicine = findViewById(R.id.recyclerview_medicine)
        no_remainer_messsage = findViewById(R.id.no_remainer_messsage)
        add_medicine_remainder = findViewById(R.id.add_medicine_remainder)


        add_medicine_remainder.setOnClickListener {
            val intent = Intent(this, Add_Medicine::class.java)
            startActivity(intent)
        }
        refreshList()
        recyclerview_medicine.layoutManager = LinearLayoutManager(this)
        val remainderDatabase = RemainderDatabase(this)
        val items = remainderDatabase.getAllRemainders()
        val adapter = MedicineRemainder_Adapter(items as ArrayList<Reminder>,this)
        recyclerview_medicine.adapter = adapter

    }

    override fun onItemClick(reminder: Reminder) {
        val intent = Intent(this, Edit_Medicine::class.java)
        intent.putExtra(Edit_Medicine.EXTRA_REMINDER_ID, reminder.id) // Assuming you have an 'id' field in your Reminder model
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // Handle the back button click event
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemLongClick(reminder: Reminder) {
        val dialogview = LayoutInflater.from(this).inflate(R.layout.delete_custom_dialog,null)
        val alertDialogBuilder = AlertDialog.Builder(this)
            .setView(dialogview)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btn_mremainder_delete = dialogview.findViewById<Button>(R.id.btn_mremainder_delete)
        val btn_mremainder_cancel = dialogview.findViewById<Button>(R.id.btn_mremainder_cancel)

        btn_mremainder_delete.setOnClickListener {
            val remainderDatabase = RemainderDatabase(this)
            remainderDatabase.deleteRemainder(reminder)
            refreshList()
            alertDialog.dismiss()
        }

        btn_mremainder_cancel.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun refreshList() {
        val remainderDatabase = RemainderDatabase(this)
        val items = remainderDatabase.getAllRemainders()
        val adapter = MedicineRemainder_Adapter(items as ArrayList<Reminder>, this)
        recyclerview_medicine.adapter = adapter

        if (items.isEmpty()) {
            no_remainer_messsage.visibility = View.VISIBLE
        } else {
            no_remainer_messsage.visibility = View.GONE
        }
    }
}