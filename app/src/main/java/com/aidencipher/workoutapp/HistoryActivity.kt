package com.aidencipher.workoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_bmiactivity.*
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.dialog_custom_back_confirmation.*

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        setSupportActionBar(toolbar_history_activity)

        supportActionBar?.setDisplayHomeAsUpEnabled(true) //set back button
        supportActionBar?.title = "HISTORY" // Setting a title in the action bar.

        toolbar_history_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        getAllCompletedDates()

    }

    private fun getAllCompletedDates(){
        val dbHandler = SqliteOpenHelper(this, null)
        val allCompletedDatesList = dbHandler.getAllCompletedDatesList()

        if(allCompletedDatesList.size > 0){
            tvHistory.visibility = View.VISIBLE
            rvHistory.visibility = View.VISIBLE
            tvNoDataAvailable.visibility = View.GONE

            rvHistory.layoutManager = LinearLayoutManager(this)
            val historyAdapter = HistoryAdapter(this, allCompletedDatesList)
            rvHistory.adapter = historyAdapter
        }else{
            tvHistory.visibility = View.GONE
            rvHistory.visibility = View.GONE
            tvNoDataAvailable.visibility = View.VISIBLE
        }
    }

}