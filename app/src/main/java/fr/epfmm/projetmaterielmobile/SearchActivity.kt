package fr.epfmm.projetmaterielmobile

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Query
import java.io.IOException

class SearchActivity : AppCompatActivity() {
    private val apiKey = "72f72c971953b37f3f4171633505e5a1"
    lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private var listMovies: ArrayList<Movie> = arrayListOf()




    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        recyclerView = findViewById<RecyclerView>(R.id.list_movies_recyclerview)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView.layoutManager =
            LinearLayoutManager(this, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = GridLayoutManager(this, GridLayoutManager.VERTICAL)
        recyclerView.adapter = MovieAdapter(this@SearchActivity, listMovies)


        searchView = findViewById(R.id.searchView)
        val searchButton = findViewById<Button>(R.id.SearchButton)
        searchButton.setOnClickListener {
            val query = searchView.query.toString()
            performSearch(query)
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    fun performSearch(query: String) = runBlocking {
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
            val moviesResult = service.getMovies(apiKey,query,1)

            Log.d("Movies : ", moviesResult.toString())

            /*val gson = Gson()
            val jsonString =
                moviesResult.toString() // Convertir la réponse en une chaîne de caractères
            *//*val movieResponse = gson.fromJson(jsonString, Movie.MovieResponse::class.java)*//*
            val jsonObject = JSONArray(jsonString)
            val moviesType = object : TypeToken<List<Movie>>() {}.type
            val movies: List<Movie> = gson.fromJson(jsonString, moviesType)*/
            /*val movies = movieResponse.results*/

            //ATTENTION LE ARRIVAL POINT EST NULL


            /*val drivesType = object : TypeToken<List<Drive>>() {}.type
            val drives: ArrayList<Drive> = gson.fromJson(drivesResult.toString(), drivesType)*/

            /*Log.d("Results2: ", movies.toString())*/


            /*if (movies != null && movies.isNotEmpty()) {
                movies.map {*/
            if ( moviesResult!=null) {
                moviesResult.results.map {
                    listMovies.add(
                        Movie(
                            it.adult,
                            it.overview,
                            it.releaseDate,
                            it.id,
                            it.originalTitle,
                            it.originalLanguage,
                            it.title,
                            it.popularity,
                            it.voteCount,
                            it.voteAverage,
                            it.release_date
                        )
                    )
                }

                //Log.d("Size of Drive List: ", moviesResult.size.toString())
                Log.d("liste course: ", listMovies.toString())
            }
        }

        // On attend que la coroutine soit terminée et on récupère la valeur calculée
        val result = myGlobalVar.await()

        println(result) // Affiche
    }

}