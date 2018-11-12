package com.example.protima.androidlabs

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import org.w3c.dom.Text
import android.view.View
import android.widget.ProgressBar
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.net.HttpURLConnection
import java.net.URL
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.net.MalformedURLException
import android.content.Context.MODE_PRIVATE
import java.io.FileInputStream
import java.io.FileNotFoundException


class WeatherForecast : Activity() {

     var windValue : String =""
     var currentValue : String=""
     var minValue : String=""
     var maxValue : String=""
     var iconValue : String=""


     var weatherImage : Bitmap?= null
    lateinit var image : ImageView
    lateinit var curTemp : TextView
    lateinit var minTemp : TextView
    lateinit var maxTemp : TextView
    lateinit var progressBar : ProgressBar
    lateinit var wind : TextView


    val ACTIVITY_NAME = "WeatherActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_forecast)

        image = findViewById<ImageView>(R.id.currentWeather)
        curTemp = findViewById<TextView>(R.id.currentTemperature)
        minTemp = findViewById<TextView>(R.id.minTemperature)
        maxTemp = findViewById<TextView>(R.id.maxTemperature)
        progressBar = findViewById<ProgressBar>(R.id.progress)
        wind = findViewById<TextView>(R.id.windSpeed)

        progressBar.visibility = View.VISIBLE
        var myQuery = ForecastQuery()
        myQuery.execute()
    }

    inner class ForecastQuery : AsyncTask<String, Integer, String>() {
var progress = 0

        override fun doInBackground(vararg params: String?): String {

            try {
                val url = URL("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric")

                val urlConnection = url.openConnection() as HttpURLConnection

                val stream = urlConnection.getInputStream()

                val factory = XmlPullParserFactory.newInstance()
                factory.setNamespaceAware(false)
                val xpp = factory.newPullParser()
                xpp.setInput( stream  , "UTF-8")// set the input stream

                while(xpp.eventType != XmlPullParser.END_DOCUMENT)
                {
                    when(xpp.eventType)
                    {
                        XmlPullParser.START_TAG -> {
                            if (xpp.name.equals("speed")) {
                                windValue = xpp.getAttributeValue(null, "value")
                                progress += 20

                            } else if (xpp.name.equals("temperature")) {

                                currentValue = xpp.getAttributeValue(null, "value")
                                minValue = xpp.getAttributeValue(null, "value")
                                maxValue = xpp.getAttributeValue(null, "value")
                                progress += 60


                            } else if (xpp.name.equals("weather")) {
                                iconValue = xpp.getAttributeValue(null, "icon")
                                progress += 20
                                var weatherUrl = "http://openweathermap.org/img/w/$iconValue.png"
                              if(fileExistance( "$iconValue.png")){
                                  var fis: FileInputStream? = null
                                  try {    fis = openFileInput("$iconValue.png")   }
                                  catch (e: FileNotFoundException) {    e.printStackTrace()  }
                                  weatherImage = BitmapFactory.decodeStream(fis)

                              }else {
                                  weatherImage = getImage(weatherUrl)
                                  val outputStream = openFileOutput( "$iconValue.png", Context.MODE_PRIVATE);
                                  weatherImage?.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                  outputStream.flush();
                                  outputStream.close();

                              }




                            }

                                    publishProgress()//causes android to call on progressUpdate when GUI is ready
                        }
                        XmlPullParser.TEXT -> { }
                    }


                    xpp.next()
                }
            }
            catch (e: Exception)
            {
var name = e.message
            }
            return "Done"
        }

        fun fileExistance(fname : String):Boolean{
            val file = getBaseContext().getFileStreamPath(fname)
            return file.exists()
        }


        override fun onProgressUpdate(vararg values: Integer?) { //update your GUI
            //textView.setText()
             maxTemp.setText("Max temp = $maxValue")
             minTemp.setText("Min temp = $minValue")
             curTemp.setText("Current temp = $currentValue")
             wind.setText("Wind speed = $maxValue")


            progressBar.progress = progress
        }

        fun getImage(url: URL): Bitmap? {
            var connection: HttpURLConnection? = null
            try {
                connection = url.openConnection() as HttpURLConnection
                connection.connect()
                val responseCode = connection.responseCode
                return if (responseCode == 200) {
                    BitmapFactory.decodeStream(connection.inputStream)
                } else
                    null
            } catch (e: Exception) {
                return null
            } finally {
                connection?.disconnect()
            }
        }

        fun getImage(urlString: String): Bitmap? {
            try {
                val url = URL(urlString)
                return getImage(url)
            } catch (e: MalformedURLException) {
                return null
            }

        }

        override fun onPostExecute(result: String?) {
            image.setImageBitmap(weatherImage)
            progressBar.setVisibility(View.INVISIBLE)
        }
    }
}

