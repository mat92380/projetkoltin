package fr.epfmm.projetmaterielmobile

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class SearchActivity : AppCompatActivity(){
    private val apiKey = "1fe5620d00d4ae469ec6acb333c71aca"
    private lateinit var searchView: SearchView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        searchView = findViewById(R.id.searchView)
        val searchButton = findViewById<Button>(R.id.SearchButton)
        searchButton.setOnClickListener {
            val query = searchView.query.toString()
            performSearch(query)
            }
    }
    fun parseMovies(responseBody: String?): List<Movie> {
        val movies = mutableListOf<Movie>()

        // Vérifier si la réponse JSON n'est pas nulle
        if (responseBody != null) {
            try {
                // Analyser la réponse JSON en un objet JSONObject
                val jsonObject = JSONObject(responseBody)

                // Extraire le tableau "results" contenant les informations des films
                val resultsArray = jsonObject.getJSONArray("results")

                // Parcourir les éléments du tableau et extraire les informations des films
                for (i in 0 until resultsArray.length()) {
                    val movieObject = resultsArray.getJSONObject(i)

                    // Extraire les attributs du film (par exemple, titre, date de sortie, etc.)
                    val title = movieObject.getString("title")
                    val releaseDate = movieObject.getString("release_date")
                    // ...

                    // Créer un objet Movie à partir des attributs extraits
                    val movie = Movie(title, releaseDate)
                    movies.add(movie)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        return movies
    }

    private fun performSearch(query: String) {
        val client = OkHttpClient()

        val url = HttpUrl.Builder()
            .scheme("https")
            .host("api.themoviedb.org")
            .addPathSegment("3")
            .addPathSegment("search")
            .addPathSegment("movie")
            .addQueryParameter("api_key", apiKey)
            .addQueryParameter("query", query)
            .build()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                val result = response
                Log.d("API Response", responseBody ?: "")
                // Traitez la réponse de l'API ici

                //ICI on a bien les resultats

                val movies = parseMovies(responseBody.toString())
               /* if (result.isSuccessful) {
                    result.map {
                        movies.add(
                            PopularMovie(
                                it.posterPath,
                                it.adult,
                                it.overview,
                                it.releaseDate,
                                it.genreIds,
                                it.id,
                                it.originalTitle,
                                it.originalLanguage,
                                it.title,
                                it.backdropPath,
                                it.popularity,
                                it.voteCount,
                                it.video,
                                it.voteAverage
                            )
                        )
                    }*/

            }
        })
        }
}