package com.example.memesharing

import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {

    var currentImageUrl:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme()
    }

    //using volley ->(String request) to (json request)-> glide
    private fun loadMeme() {
        //create for progressBar
        findViewById<ProgressBar>(R.id.progressBar).visibility=View.VISIBLE

        //val imageView = findViewById<ImageView>(R.id.imageView)
// Instantiate the RequestQueue.

       //इसको अब use नही कर रहे क्यू की हम singleton pattern use कर रहे है
       // val queue = Volley.newRequestQueue(this)


        currentImageUrl = "https://meme-api.com/gimme"
// Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, currentImageUrl, null,
            { response ->
                val memeUrl = response.getString("url")
                Glide.with(this).load(memeUrl).listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        findViewById<ProgressBar>(R.id.progressBar).visibility=View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                       findViewById<ProgressBar>(R.id.progressBar).visibility=View.GONE
                        return false
                    } })
                    .into(findViewById(R.id.imageView))
            },
            {

            })

// Add the request to the RequestQueue.

    //Due to singleton now we can't use queue like this
    //queue.add(jsonObjectRequest)

        //simple word में बोले तो इसका singleton का मतलब है की पुरे program में single volley insstance का use करना है अब उसे use कर के चाहे जितना volley request लीजिये उसका use कर के
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun shareMeme(view: View) {
        val intent=Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey,Checkout this cool meme I got form my app $currentImageUrl")
        val chooser=Intent.createChooser(intent, "Share this meme using...")
        startActivity(chooser)
    }

    fun nextMeme(view: View) {
        //यहाँ बस हमें loadMeme function को कॉल करना है दुबारा और next meme के लिए
        loadMeme()
    }
}