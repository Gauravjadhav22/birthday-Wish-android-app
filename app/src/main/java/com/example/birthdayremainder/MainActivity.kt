package com.example.birthdayremainder

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import android.widget.ListAdapter
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.serialization.json.buildJsonObject
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val author = findViewById<TextView>(R.id.author)
        val quote = findViewById<TextView>(R.id.quote)

        if (checkForInternet(this)) {

            val url = "https://api.quotable.io/random?maxLength=80"
            //RequestQueue initialized
            val  mRequestQueue = Volley.newRequestQueue(this)

            //String Request initialized
            val   mStringRequest = object : StringRequest(Request.Method.GET, url, Response.Listener { response ->

                val jsonObject= JSONObject(response)
                val name =jsonObject.getString("content").toString()
                quote.setText(name)
                val authorName =jsonObject.getString("author").toString()
                author.setText("Author- $authorName")



            }, Response.ErrorListener { error ->
                Log.i("This is the error", "Error :" + error.toString())
                Toast.makeText( this,"Something went wrong", Toast.LENGTH_SHORT).show()
            }) {
                override fun getBodyContentType(): String {
                    return "application/json"
                }



            }
            mRequestQueue!!.add(mStringRequest!!)
            getBirthdays()


        }
        else {
            Toast.makeText(this, "Not Connected to Internet", Toast.LENGTH_SHORT).show()
        }




        val addbtn: Button = findViewById(R.id.addbtn)

        addbtn.setOnClickListener {
            val intent = Intent(this,AddBirthday::class.java)
            startActivity(intent)
            finish()
        }






    }



    private fun checkForInternet(context: Context): Boolean {


        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            val network = connectivityManager.activeNetwork ?: return false


            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {

                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }




    fun getBirthdays(){
        val url = "https://birthday-remainder-nodejs.herokuapp.com/api/v1/getbirthday"
        //RequestQueue initialized
        val  mRequestQueue = Volley.newRequestQueue(this)

            val list = ArrayList<PersonData>()
        //String Request initialized
        val  jsonObjectRequest= object : JsonObjectRequest(Request.Method.GET, url, null,{response ->
            Log.d("theresponse",response.toString())


            val jsonArrayItems= response.getJSONArray("arr")
            val list = ArrayList<PersonData>()

                Log.d("theresponse 2",jsonArrayItems.toString())

                var i=0
                while(i<jsonArrayItems.length()){
                    val jsonObject= jsonArrayItems.getJSONObject(i)
                    list.add(
                        PersonData(
                            jsonObject.getString("name"),
                            jsonObject.getString("contact"),
                            jsonObject.getString("age"),
                        )

                    )
                    i++;
                }

            var  listView = findViewById<ListView>(R.id.listView)

            val adapter= ListAdapter(this@MainActivity, list)

//            Log.d("here it is",list.toString())
            listView.adapter=adapter







        }, Response.ErrorListener { error ->
            Log.i("This is the error", "Error :" + error.toString())
            Toast.makeText( this,"Something went wrong !.", Toast.LENGTH_SHORT).show()
        }) {
            override fun getBodyContentType(): String {
                return "application/json"
            }



        }
        mRequestQueue!!.add(jsonObjectRequest!!)



    }

    }

