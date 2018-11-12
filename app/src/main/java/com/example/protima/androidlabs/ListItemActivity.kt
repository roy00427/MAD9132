package com.example.protima.androidlabs

import android.app.Activity
import android.os.Bundle
import android.widget.ImageButton
import android.view.View
import android.content.Intent
import android.provider.MediaStore
import android.graphics.Bitmap
import android.util.Log
import android.widget.Switch
import android.app.AlertDialog
import android.widget.Toast
import android.widget.CheckBox
import kotlinx.android.synthetic.main.activity_list_item.*



class ListItemActivity : Activity() {
    val REQUEST_IMAGE_CAPTURE = 1
    val ACTIVITY_NAME="ListItemActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_item)
        Log.i(ACTIVITY_NAME,"In onCreate()")

        var imageButton = findViewById<ImageButton>(R.id.imageButton)
        imageButton?.setOnClickListener(View.OnClickListener{

            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(packageManager)?.also {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }

        })
        var switchButton = findViewById<Switch>(R.id.switch1)
        switchButton.setOnCheckedChangeListener { anyname, isChecked ->

            if (isChecked) {
                val text = "Switch is On"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(this , text, duration)
                toast.show()
            } else {
                val text = "Switch is Off"
                val duration = Toast.LENGTH_LONG
                val toast = Toast.makeText(this , text, duration)
                toast.show()
            }
        }

        var checkBoxButton=findViewById<CheckBox>(R.id.checkBox)
        checkBoxButton.setOnCheckedChangeListener{_, isChecked ->
            if(isChecked){
                val builder = AlertDialog.Builder(this)
                builder.setMessage(R.string.dialog_message)
                        .setTitle(R.string.dialog_title)
                        .setPositiveButton(R.string.ok, { dialog, id ->

                            // User clicked OK button
                            val resultIntent = Intent( )
                            resultIntent.putExtra("Response", "Here is my response")
                            setResult(Activity.RESULT_OK, resultIntent)
                            finish()

                        })
                        .setNegativeButton(R.string.cancel, {dialog, id ->
                            // User cancelled the dialog
                        })
                        .show()


            }
        }

    }

    //This gets called after onCreate()

    override fun onStart()
    {
        super.onStart()
        Log.i(ACTIVITY_NAME, "In onStart()")
    }


    //This gets called after onStart()

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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data.extras.get("data") as Bitmap
            imageButton.setImageBitmap(imageBitmap)
        }


    }
}
