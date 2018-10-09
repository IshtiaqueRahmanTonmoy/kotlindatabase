package net.simplifiedcoding.firstkotlinapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    //edittext and spinner
    private var editTextArtistName: EditText? = null
    private var spinnerGenre: Spinner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //getting it from xml
        editTextArtistName = findViewById(R.id.editTextArtistName) as EditText
        spinnerGenre = findViewById(R.id.spinnerGenre) as Spinner

        //adding a click listener to button
        findViewById(R.id.buttonAddArtist).setOnClickListener { addArtist() }

        //in the second button click
        //opening the activity to display all the artist
        findViewById(R.id.buttonViewArtist).setOnClickListener {
            val intent = Intent(applicationContext, ViewArtistsActivity::class.java)
            startActivity(intent)
        }
    }

    //adding a new record to database
    private fun addArtist() {
        //getting the record values
        val name = editTextArtistName?.text.toString()
        val genre = spinnerGenre?.selectedItem.toString()

        //creating volley string request
        val stringRequest = object : StringRequest(Request.Method.POST, EndPoints.URL_ADD_ARTIST,
                Response.Listener<String> { response ->
                    try {
                        val obj = JSONObject(response)
                        Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_LONG).show()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                object : Response.ErrorListener {
                    override fun onErrorResponse(volleyError: VolleyError) {
                        Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG).show()
                    }
                }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("name", name)
                params.put("genre", genre)
                return params
            }
        }

        //adding request to queue
        VolleySingleton.instance?.addToRequestQueue(stringRequest)
    }
}
