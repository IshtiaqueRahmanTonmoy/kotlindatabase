package net.simplifiedcoding.firstkotlinapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class ViewArtistsActivity : AppCompatActivity() {

    private var listView: ListView? = null
    private var artistList: MutableList<Artist>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_artists)

        listView = findViewById(R.id.listViewArtists) as ListView
        artistList = mutableListOf<Artist>()
        loadArtists()
    }

    private fun loadArtists() {
        val stringRequest = StringRequest(Request.Method.GET,
                EndPoints.URL_GET_ARTIST+"abc@gmail.com",
                Response.Listener<String> { s ->
                    try {
                        val obj = JSONObject(s)
                        val array = obj.getJSONArray("result")
                        for (i in 0..array.length() - 1) {
                                val objectArtist = array.getJSONObject(i)
                                val artist = Artist(
                                        objectArtist.getString("id"),
                                        objectArtist.getString("id")
                                )
                                artistList!!.add(artist)
                                val adapter = ArtistList(this@ViewArtistsActivity, artistList!!)
                                listView!!.adapter = adapter
                            }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener { volleyError -> Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG).show() })

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add<String>(stringRequest)
    }
}
