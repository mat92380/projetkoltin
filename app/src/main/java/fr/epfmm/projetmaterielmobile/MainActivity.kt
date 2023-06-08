package fr.epfmm.projetmaterielmobile

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private val apiKey = "72f72c971953b37f3f4171633505e5a1"
    lateinit var recyclerView: RecyclerView
    private var listMovies: ArrayList<Movie> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById<RecyclerView>(R.id.list_popular_recyclerview)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView.layoutManager =
            LinearLayoutManager(this, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = GridLayoutManager(this, GridLayoutManager.VERTICAL)
        recyclerView.adapter = MovieAdapter(this@MainActivity, listMovies)


        val SearchButton = findViewById<Button>(R.id.Search_Button)
        val Fav_Button = findViewById<Button>(R.id.Fav_Button)
        getPopularMovies()
        SearchButton.setOnClickListener{
            val intent = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(intent)
        }
        Fav_Button.setOnClickListener{
            val intent = Intent(this@MainActivity, FavoriActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.liste_films_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_scan -> {
                startQRScan()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startQRScan() {
        val integrator = IntentIntegrator(this)
        integrator.setPrompt("Scan a QR code")
        integrator.setOrientationLocked(false)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setBeepEnabled(true)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Log.d(TAG, "Cancelled scan")
            } else {
                val lien = result.contents
                /*val tvScanResult = findViewById<TextView>(R.id.tv_scan_result)
                tvScanResult.text = lien*/
                val movieId = lien.substringAfterLast("/movie/").substringBefore("-")
                Log.d("Movie id : ", movieId.toString())

                /*val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(tvScanResult.text.toString())
                startActivity(intent)*/
                PerformId(Integer.parseInt(movieId))
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun PerformId(movie_id: Int) = runBlocking {
        val myGlobalVar = GlobalScope.async {

            val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            val service = retrofit.create(MovieService::class.java)
            val moviesResult = service.finById(movie_id, apiKey) as Movie

            Log.d("Movie search : ", moviesResult.toString())

            Log.d("Movie details : ", moviesResult.title.toString())


            val intent = Intent(this@MainActivity, MovieDetailsActivity::class.java)
            intent.putExtra("movie", moviesResult)
            startActivity(intent)
        }
    }

    fun getPopularMovies() = runBlocking {
        val myGlobalVar = GlobalScope.async {

            val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            val service = retrofit.create(MovieService::class.java)
            val moviesResult = service.getPopularMovies(apiKey)

            Log.d("Movies : ", moviesResult.toString())

            if ( moviesResult!=null) {
                moviesResult.results.map {
                    listMovies.add(
                        Movie(
                            it.adult,
                            it.overview,
                            it.release_date,
                            it.id,
                            it.original_language,
                            it.title,
                            it.popularity,
                            it.vote_count,
                            it.vote_average,
                            it.poster_path
                        )
                    )
                }
                Log.d("liste film: ", listMovies.toString())
            }
        }

        val result = myGlobalVar.await()

        println(result) // Affiche
    }

}