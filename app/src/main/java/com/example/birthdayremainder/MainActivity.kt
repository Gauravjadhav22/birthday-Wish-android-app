package com.example.birthdayremainder

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    private lateinit var res:String
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
                Log.d("here is the request",response.toString())
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

        //String Request initialized
        val  jsonObjectRequest= object : JsonObjectRequest(Request.Method.GET, url, null,{response ->
            Log.d("theresponse",response.toString())



//            val jsonArrayItems= response.getJSONArray("arr")
            res= response.getJSONArray("arr").toString()
            val jsonArray= JSONArray(res)
            val list = ArrayList<PersonData>()
             var heightL:Int=0;
                Log.d("theresponse 2",jsonArray.toString())

                var i=0




            while(i<jsonArray.length()){
                val jsonObject= jsonArray.getJSONObject(i)
                list.add(
                    PersonData(
                        jsonObject.getString("name"),
                        jsonObject.getString("contact"),
                        jsonObject.getString("age"),
                    )

                )

                i++;
                heightL+=600

            }


            var  listView = findViewById<ListView>(R.id.listView)
            val adapter= ListAdapter(this@MainActivity, list)

            Log.d("here it is",list.toString())
            listView.adapter=adapter
            val params = listView.layoutParams
            params.height=heightL
            listView.setLayoutParams(params);
            listView.setOnItemClickListener{ parent, view, position, id ->

                val element = adapter.getItem(position) // The item that was clicked
                val jsonObject= jsonArray.getJSONObject(element as Int)
                val arr =  listOf(  jsonObject.getString("name").toString(),jsonObject.getString("contact").toString(),jsonObject.getString("age").toString())





                Log.d("fucking list",arr.toString())
                val contact="+91${arr[1]}"
                val message:String="happy birthday to you🥳🥳:)"

                val uri = Uri.parse("smsto:$contact")
                val i = Intent(Intent.ACTION_SENDTO, uri)
                intent.type = "text/plain"
                i.putExtra(Intent.EXTRA_TEXT, message)
                intent.setPackage("com.whatsapp")
                if(intent.resolveActivity(this.packageManager)!=null){
                    startActivity(Intent.createChooser(i,"whish them happy birthday"))
                }else{
                    Toast.makeText(this,"Whatsapp is not installed",Toast.LENGTH_SHORT).show()
                }





                Log.d("fucking response",jsonObject.toString())
            }









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

