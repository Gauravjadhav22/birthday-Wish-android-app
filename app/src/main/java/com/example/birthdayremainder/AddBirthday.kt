package com.example.birthdayremainder

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class AddBirthday : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_birthday)
        val addbtn: Button = findViewById(R.id.Add_button)
        addbtn.setOnClickListener{

        if (checkForInternet(this)) {

            val name = findViewById<EditText>(R.id.fname).text.toString()
            val contact = findViewById<EditText>(R.id.mono).text.toString()
            val dob = findViewById<EditText>(R.id.dob).text.toString()


            val url = "https://birthday-remainder-nodejs.herokuapp.com/api/v1/getbirthday"

            //RequestQueue initialized
            val  mRequestQueue = Volley.newRequestQueue(this)

            //String Request initialized
            val   mStringRequest = object : StringRequest(Request.Method.POST, url, Response.Listener { response ->
                Toast.makeText(this, "birthday has added", Toast.LENGTH_SHORT).show()
                val intent =Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()

            }, Response.ErrorListener { error ->
                Log.i("This is the error", "Error :" + error.toString())
                Toast.makeText( this,"Something went wrong", Toast.LENGTH_SHORT).show()
            }) {
                override fun getBodyContentType(): String {
                    return "application/json"
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val params2 = HashMap<String, String>()
                    params2.put ("name", name)
                    params2.put("dob", dob)
                    params2.put("contact", contact)

                    return JSONObject(params2 as Map<*, *>).toString().toByteArray()
                }

            }
            mRequestQueue!!.add(mStringRequest!!)



        }
        else {
            Toast.makeText(this, "Not Connected to Internet", Toast.LENGTH_SHORT).show()
        }
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


}