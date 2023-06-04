package fr.epfmm.projetmaterielmobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class FavoriActivity: AppCompatActivity() {
    private val apiKey = "72f72c971953b37f3f4171633505e5a1"
    lateinit var recyclerView: RecyclerView
    private var  favoris = mutableListOf<String>()
    private var  listefav : ArrayList<Movie> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favori)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerView = findViewById<RecyclerView>(R.id.list_favorites_recyclerview)
        recyclerView.layoutManager =
            LinearLayoutManager(this, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = GridLayoutManager(this, GridLayoutManager.VERTICAL)
        recyclerView.adapter = MovieAdapter(this@FavoriActivity, listefav)
        recupererFavoris()
        Log.d("Movie search main: ", listefav.toString())
        recyclerView.adapter?.notifyDataSetChanged()
    }
    fun recupererFavoris(): List<String> {
        val file = File("/data/user/0/fr.epfmm.projetmaterielmobile/files/myDataDirectory/listefavori.txt")


        if (file.exists() && file.canRead()) {
            file.bufferedReader().useLines { lines ->
                lines.forEach { line ->
                    val id = line.trim()
                    favoris.add(id)

                }
            }
        } else {
            println("Probleme")

        }
        println("Liste favoris $favoris")
        Recupererinfos(favoris)
        return favoris
    }

    fun Recupererinfos(favoris:  List<String>)= runBlocking{
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
            for (id in favoris) {
                val movieResult = service.finById(Integer.parseInt(id), apiKey) as Movie
                listefav.add(movieResult)


            }
            Log.d("Moviename : ", listefav[1].title.toString())



            Log.d("Movie search fonction: ", listefav.toString())



        }
        val result = myGlobalVar.await()

    }

}