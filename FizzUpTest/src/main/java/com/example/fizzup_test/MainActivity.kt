package com.example.fizzup_test

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class MainActivity : AppCompatActivity() {

    val myList: ArrayList<listItem> = ArrayList<listItem>()
    val adapter: BaseAdapter2? = null
    var list: ListView? = null

   // override fun onBackPressed() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val titre = findViewById<TextView>(R.id.titre)
        list = findViewById<View>(R.id.list) as ListView?


        val dialog = ProgressDialog.show(this@MainActivity, "", "Please wait")
        val timer = Timer()
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                dialog.dismiss()
            }
        }
        timer.schedule(task, 10000)

            val requestQueue = Volley.newRequestQueue(this)
            val url = "https://s3-us-west-1.amazonaws.com/fizzup/files/public/sample.json"

            // Request a string response from the provided URL.
            // val stringRequest = StringRequest(Request.Method.GET, url,
            //   Response.Listener<String> { response ->

            val request =
                JsonObjectRequest(Request.Method.GET, url, null, Response.Listener { response ->
                    try {
                        val jsonArray = response.getJSONArray("data")
                        for (i in 0 until jsonArray.length()) {
                            val data = jsonArray.getJSONObject(i)
                            val image = data.getString("image_url")
                            val name = data.getString("name")
                            //titre.setText(name)
                            myList.add(listItem(image, name))
                        }
                        val adapter = BaseAdapter2(this@MainActivity, myList)
                        list!!.adapter = adapter
                        dialog.dismiss()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener { error -> error.printStackTrace() })
            requestQueue?.add(request)
        }


// Add the request to the RequestQueue.
        //queue.add(stringRequest)


        /*
        val params = Bundle()
        params.putString("fields", "score")
        val request = GraphRequest(AccessToken.getCurrentAccessToken(), "/145..../scores",
            null, HttpMethod.GET, object : Callback() {
                fun onCompleted(response: GraphResponse?) {
                    val error: FacebookRequestError = response.getError()
                    Log.d("BBB", "response: " + response.toString())
                    if (error == null) {
                        if (response != null) {
                            try {
                                val graphObject: JSONObject = response.getJSONObject()
                                val data = graphObject.getJSONArray("data")
                                val length = data.length()
                                for (i in 0 until length) {
                                    dialog.dismiss()
                                    val oneUser = data.optJSONObject(i)
                                    val userObj = oneUser.optJSONObject("user")
                                    val name = userObj.getString("name")
                                    val score = oneUser.getInt("score")
                                    myList.add(listItem(name, score))
                                }
                                val adapter = BaseAdapter2(this@activity_main, myList)
                                list!!.adapter = adapter
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                    } else if (error != null) {
                        Toast.makeText(
                            getApplicationContext(),
                            "Connect to Internet or Facebook ", Toast.LENGTH_SHORT
                        )
                            .show()
                        dialog.dismiss()
                    }
                }
            })
        request.setParameters(params)
        request.executeAsync()*/
    }
