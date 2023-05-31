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
    private val apiKey = "1fe5620d00d4ae469ec6acb333c71aca"
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
        recyclerView.adapter = MovieAdapter(this@SearchActivity, listMovies)


        searchView = findViewById(R.id.searchView)
        val searchButton = findViewById<Button>(R.id.SearchButton)
        searchButton.setOnClickListener {
            val query = searchView.query.toString()
            performSearch(query)
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
            val moviesResult = service.getMovies("1fe5620d00d4ae469ec6acb333c71aca",query,1)
            Log.d("Movies : ", moviesResult.toString())

            val gson = Gson()
            val jsonString =
                moviesResult.toString() // Convertir la réponse en une chaîne de caractères
            val jsonObject = JSONArray(jsonString)
            val moviesType = object : TypeToken<List<Movie>>() {}.type
            val movies: ArrayList<Movie> = gson.fromJson(jsonObject.toString(), moviesType)

            //ATTENTION LE ARRIVAL POINT EST NULL


            /*val drivesType = object : TypeToken<List<Drive>>() {}.type
            val drives: ArrayList<Drive> = gson.fromJson(drivesResult.toString(), drivesType)*/

            Log.d("Results2: ", moviesResult.toString())


            if (moviesResult != null && moviesResult.isNotEmpty()) {
                moviesResult.map {
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
                Log.d("Size of Drive List: ", moviesResult.size.toString())
                Log.d("liste course: ", moviesResult.toString())
            }
        }

        // On attend que la coroutine soit terminée et on récupère la valeur calculée
        val result = myGlobalVar.await()

        println(result) // Affiche
    }

}