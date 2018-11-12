package com.example.protima.androidlabs

import android.app.Activity
import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast


class StartActivity : Activity() {
    val ACTIVITY_NAME = "StartActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        Log.i(ACTIVITY_NAME,"In onCreate()")

        var button1 = findViewById(R.id.button) as? Button


        button1?.setOnClickListener( View.OnClickListener {


            val newActivity = Intent(this, ListItemActivity::class.java);

            startActivityForResult(newActivity, 50)
        })

        var button2 = findViewById(R.id.startChat) as? Button
        button2?.setOnClickListener(View.OnClickListener {

            val newActivity = Intent( this,ChatWindow::class.java);
            startActivityForResult(newActivity, 200)
        })

        var button3 = findViewById(R.id.weatherForecast) as? Button
        button3?.setOnClickListener(View.OnClickListener {

            val newActivity = Intent( this,WeatherForecast::class.java);
            startActivityForResult(newActivity, 200)
        })

        }

    override fun onStart()
    {
        super.onStart()
        Log.i(ACTIVITY_NAME, "In onStart()")
    }




    override fun onResume()
    {
        super.onResume()
        Log.i(ACTIVITY_NAME, "In OnResume()")

    }


    override fun onPause() {
        super.onPause()
        Log.i(ACTIVITY_NAME,"In onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.i(ACTIVITY_NAME,"In onStop()")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.i(ACTIVITY_NAME,"In onDestroy()")
    }

    override fun onActivityResult(request:Int, result:Int, data:Intent?)
    {
        if (request==50) {
            Log.i("ACTIVITY_NAME", "Returned to StartActivity.onActivityResult")

            if (result == Activity.RESULT_OK) {
                val messagePassed = data?.getStringExtra("Response")
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(this, messagePassed, duration)
                toast.show()
                Log.i("ACTIVITY_NAME", "Returned to StartActivity.onActivityResult")
            }
        }
    }



}
