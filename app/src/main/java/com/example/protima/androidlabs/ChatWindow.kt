package com.example.protima.androidlabs

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.*
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.content.Context

class ChatWindow : Activity() {


    val ACTIVITY_NAME = "ChatWindow"

    var numItems = 100

    var messageList = ArrayList<String>()

   var myList = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_window)

        var myList = findViewById<ListView>(R.id.myList)
        var editText = findViewById<EditText>(R.id.editText)
        var doneButton = findViewById<Button>(R.id.doneButton)

        Log.i(ACTIVITY_NAME,"In onCreate()")

        var theAdapter = ChatAdapter( this )


        doneButton?.setOnClickListener(View.OnClickListener{

            Log.i(ACTIVITY_NAME,"CLICK SEND")
            var usedMsg= (editText.getText()).toString()

            messageList.add(usedMsg)
            theAdapter.notifyDataSetChanged()
            editText.setText("")
        })

        myList.setAdapter(theAdapter);
    }



    inner class ChatAdapter(val ctx : Context):ArrayAdapter<String>(ctx,0){

        override fun getCount():Int{
            return messageList.size
        }

        override fun getItem(position:Int):String{
            return messageList.get(position)
        }

        override fun getView(position : Int, convertView: View?, parent : ViewGroup) : View?{

            var inflater = LayoutInflater.from(parent.getContext())//get an inflater

            var thisRow = null as View?


            if(position%2 == 0) {
             thisRow=inflater.inflate(R.layout.chat_row_outgoing, null)
            } else
           { thisRow=inflater.inflate(R.layout.chat_row_incoming, null)}

            var textView = thisRow.findViewById<TextView>(R.id.message_text)
            textView.setText(getItem(position))
            return thisRow

        }

        override fun getItemId(position:Int):Long{
            return 0
        }
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

}
