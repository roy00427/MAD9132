package com.example.protima.androidlabs

import android.app.Activity
import android.os.Bundle

class MessageDetails : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_details)

        var MyNewFragment = MessageFragment()
        var infoToPass = intent.extras
        MyNewFragment.amITablet = false

        var loadFragment = getFragmentManager().beginTransaction()//this is a fragment transaction
        loadFragment.replace(R.id.fragment_location,MyNewFragment)
        MyNewFragment.arguments = infoToPass
        loadFragment.commit()


    }
}
