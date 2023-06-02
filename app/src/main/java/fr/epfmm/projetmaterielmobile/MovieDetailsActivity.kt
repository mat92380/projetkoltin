package fr.epfmm.projetmaterielmobile

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieDetailsActivity : AppCompatActivity() {

    private val apiKey = "72f72c971953b37f3f4171633505e5a1"
    /*val extras = intent.extras
    val movieExtra = extras?.get("movie") as? Movie*/
    lateinit var recyclerView: RecyclerView
    private var listRecos: ArrayList<Movie> = arrayListOf()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_details_activity)
        val extras = intent.extras
        val movieExtra = extras?.get("movie") as? Movie
        Log.d("film : ",movieExtra.toString() )
        recyclerView = findViewById<RecyclerView>(R.id.recommand_movies_recyclerview)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView.layoutManager =
            LinearLayoutManager(this, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = GridLayoutManager(this, GridLayoutManager.VERTICAL)
        recyclerView.adapter = MovieAdapter(this@MovieDetailsActivity, listRecos)

        val title = findViewById<TextView>(R.id.moviedetails_title_textview)
        val resume = findViewById<TextView>(R.id.moviedetails_resume_textview)
        val date = findViewById<TextView>(R.id.moviedetails_date_textview)
        val language = findViewById<TextView>(R.id.moviedetails_language_textview)
        val note = findViewById<TextView>(R.id.moviedetails_note_textview)
        val affiche = findViewById<ImageView>(R.id.detailsmovie_view_imageview)
        val posterPath = movieExtra?.poster_path
        val baseImageUrl = "https://image.tmdb.org/t/p/"
        val posterUrl = baseImageUrl + "w600_and_h900_bestv2" + posterPath

        Picasso.get()
            .load(posterUrl)
            .into(affiche)
        title.text = movieExtra?.title
        resume.text = movieExtra?.overview
        date.text = movieExtra?.release_date
        language.text = movieExtra?.original_language
        note.text = movieExtra?.vote_average.toString()+"/10"
        Recommandations()

    }

    fun Recommandations() = runBlocking{
        val extras = intent.extras
        val movieExtra = extras?.get("movie") as? Movie
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
            val moviesResult = service.getRecommandatedMovies(movieExtra?.id,apiKey)

            Log.d("Movies : ", moviesResult.toString())

            if ( moviesResult!=null) {
                moviesResult.results.map {
                    listRecos.add(
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
                Log.d("liste reco: ", listRecos.toString())
            }
        }


        val result = myGlobalVar.await()

        println(result) // Affiche
    }

}