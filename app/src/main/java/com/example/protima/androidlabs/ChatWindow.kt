package com.example.protima.androidlabs

import android.app.Activity
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.*
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.protima.androidlabs.R.id.message

class ChatWindow : Activity() {


    val ACTIVITY_NAME = "ChatWindow"

    var numItems = 100

    var messageList = ArrayList<String>()

    var myList = null

    var messageClicked = 0

    lateinit var results: Cursor
    lateinit var dhHelper: chatDatabaseHelper
    lateinit var db: SQLiteDatabase
    lateinit var theAdapter:ChatAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_window)


        var fragmentLocation = findViewById<FrameLayout>(R.id.fragment_location)//return null on phone, not null on tablet
        var amITablet = (fragmentLocation !=null)


         dhHelper = chatDatabaseHelper()
         db = dhHelper.writableDatabase


       results = db.query(dhHelper.TABLE_NAME, arrayOf(dhHelper.KEY_MESSAGE, dhHelper.KEY_ID),
                null, null, null, null, null, null
        )

        var numRow = results.getCount()//number of rows saved
        val numColumns = results.getColumnCount()

        Log.i(ACTIVITY_NAME, numColumns.toString())

        for(i in  0..numColumns-1){
           val columnName = results.getColumnName(i)
            Log.i("columnName" ,columnName)
        }

        results.moveToFirst()// read from the first row
        var myList = findViewById<ListView>(R.id.myList)
        var editText = findViewById<EditText>(R.id.editText)
        var doneButton = findViewById<Button>(R.id.doneButton)

        val keyIndex = results.getColumnIndex(dhHelper.KEY_MESSAGE)
        while (!results.isAfterLast) {
            var thisMessage = results.getString(keyIndex)
            messageList.add(thisMessage) //preload message from database
            results.moveToNext()

        }


            Log.i(ACTIVITY_NAME, "In onCreate()")

            theAdapter = ChatAdapter(this)


            doneButton?.setOnClickListener(View.OnClickListener {

                Log.i(ACTIVITY_NAME, "CLICK SEND")
                var usedMsg = (editText.getText()).toString()


                var newRow = ContentValues()
                newRow.put( dhHelper.KEY_MESSAGE , usedMsg)

                db.insert(dhHelper.TABLE_NAME, "", newRow)// insert new row
                results = db.query(dhHelper.TABLE_NAME, arrayOf(dhHelper.KEY_MESSAGE, dhHelper.KEY_ID),
                        null, null, null, null, null, null)



                messageList.add(usedMsg)
                theAdapter.notifyDataSetChanged()
                editText.setText("")
            })

            myList.setAdapter(theAdapter);


            myList.setOnItemClickListener { parent, view, position, id ->
           //you have clicked a chat message

            var infoToPass = Bundle()
            infoToPass.putString("Message",messageList.get(position))
            infoToPass.putLong("ID", id)

                messageClicked=position

            if(amITablet)//table version
            {

                var MyNewFragment = MessageFragment()

                MyNewFragment.parent = this
                MyNewFragment.amITablet= true

                var loadFragment = getFragmentManager().beginTransaction()//this is a fragment transaction
                loadFragment.replace(R.id.fragment_location,MyNewFragment)
                MyNewFragment.arguments = infoToPass
                loadFragment.commit()
        }else{
             var detailActivity =Intent( this,MessageDetails::class.java)
                detailActivity.putExtras(infoToPass)
                startActivityForResult(detailActivity,35)
            }
        }// phone


    }

    fun deleteMessage(id:Long){
        //delete the message with id
        db.delete(TABLE_NAME, "_id=$id", null)

        results = db.query(dhHelper.TABLE_NAME, arrayOf(dhHelper.KEY_MESSAGE, dhHelper.KEY_ID),
                null, null, null, null, null, null)

        messageList.removeAt(messageClicked)
                theAdapter.notifyDataSetChanged()

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
            results.moveToPosition(position)

            var id = results.getInt(results.getColumnIndex("_id")).toLong()
            return id
        }
    }

    val DATABASE_NAME = "AFileName.db"
    val VERSION_NUM = 3

    val TABLE_NAME ="chatMessages"

    inner class chatDatabaseHelper : SQLiteOpenHelper(this@ChatWindow, DATABASE_NAME, null, VERSION_NUM)
    {
        val KEY_ID = "_id"

        val TABLE_NAME ="chatMessages"
        val KEY_MESSAGE = "Messages"
        override fun onCreate(db: SQLiteDatabase) {
           // db.execSQL("CREATE TABLE " + TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_MESSAGE+" TEXT)"

                    db.execSQL("CREATE TABLE  $TABLE_NAME (_id INTEGER PRIMARY KEY AUTOINCREMENT, $KEY_MESSAGE TEXT)"



                    )

        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME)
            onCreate(db)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
       if((requestCode == 35)&& (resultCode == Activity.RESULT_OK)){
           var num = (data!!.getLongExtra("ID",0))
           deleteMessage(num)
       }
    }

}
