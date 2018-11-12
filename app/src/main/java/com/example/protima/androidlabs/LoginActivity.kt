package com.example.protima.androidlabs
import android.content.Intent
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.view.View
import android.widget.EditText


class LoginActivity : Activity() {

    val ACTIVITY_NAME = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Log.i(ACTIVITY_NAME, "In onCreate()");

        val prefs = getSharedPreferences("SavedData", Context.MODE_PRIVATE)

        //Look for the value reserved under the name UserInput. If not there, return Default answer
        val userString = prefs.getString("DefaultEmail", "email@domain.com")

        val editText = findViewById(R.id.editText) as? EditText
        Log.e("In StartActivity", "string is:" + userString)
        editText?.setText(userString)

        var button1 = findViewById(R.id.login) as? Button


        //add a click handler:
        button1?.setOnClickListener( View.OnClickListener {


            val newActivity = Intent(this, StartActivity::class.java);


            val typedString = editText?.getText().toString()



            val prefs = prefs.edit()


            prefs.putString("DefaultEmail", typedString)



            prefs.commit()

            var intent = Intent(this, StartActivity::class.java)
            startActivity(intent)



        })


    }

    override fun onResume() {
        super.onResume()
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    override fun onStart() {
        super.onStart()
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    override fun onPause() {
        super.onPause()
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    override fun onStop() {
        super.onStop()
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }


}
