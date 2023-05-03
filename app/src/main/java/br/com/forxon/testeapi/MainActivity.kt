package br.com.forxon.testeapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.com.forxon.testeapi.databinding.ActivityMainBinding
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide


//Para fazer o binding da Home
private lateinit var bndMain: ActivityMainBinding
private lateinit var requestQueue: RequestQueue

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Binding da página
        bndMain = ActivityMainBinding.inflate(layoutInflater)
        val view = bndMain.root
        setContentView(view)

        //Início da config para puxar dados da API - Usa o volley
        val appnetwork = BasicNetwork(HurlStack())
        val appcache = DiskBasedCache(cacheDir, 1024 * 1024) // 1MB cap
        requestQueue = RequestQueue(appcache, appnetwork).apply {
            start()
        }

        bndMain.search.setOnClickListener(){
            var input= bndMain.userinput.text.toString()
            fetchData(input)
        }

    }

    fun fetchData( input: String){
        val url = "http://www.omdbapi.com/?t=${input}&apikey=66ee6ede"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                if(response.get("Response")=="False"){
                    bndMain.name.text = "Incorrect detail"
                }else {
                    Glide.with(this).load(response.getString("Poster")).into(bndMain.image)
                    bndMain.plot.text = response.getString("Plot")
                    bndMain.name.text = response.getString("Title")+"\n\n"+"Writer: "+response.getString("Writer")
                }
            },
            { error ->
                Log.d("vol",error.toString())
                bndMain.name.text= url+" "+error.toString()
            }
        )

        requestQueue.add(jsonObjectRequest)
    }
}