package fr.epfmm.projetmaterielmobile

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieDetailsActivity : AppCompatActivity() {

    private val apiKey = "72f72c971953b37f3f4171633505e5a1"
    val extras = intent.extras
    val movieExtra = extras?.get("movie") as? Movie
    private var listMovies: ArrayList<Movie> = arrayListOf()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_details_activity)


        val title = findViewById<TextView>(R.id.moviedetails_title_textview)
        val resume = findViewById<TextView>(R.id.moviedetails_resume_textview)
        val date = findViewById<TextView>(R.id.moviedetails_date_textview)
        val language = findViewById<TextView>(R.id.moviedetails_language_textview)
        val note = findViewById<TextView>(R.id.moviedetails_note_textview)
        val imageView = findViewById<ImageView>(R.id.detailsmovie_view_imageview)

        title.text = movieExtra?.title
        resume.text = movieExtra?.overview
        date.text = movieExtra?.release_date
        language.text = movieExtra?.original_language
        note.text = movieExtra?.vote_average.toString()


    }

    fun Recommandations(id : Int) = runBlocking{
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
            val moviesResult = service.getRecommandatedMovies(apiKey,movieExtra?.id)

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
                            it.vote_average
                        )
                    )
                }
                Log.d("liste course: ", listMovies.toString())
            }
        }

        val result = myGlobalVar.await()

        println(result) // Affiche
    }

}