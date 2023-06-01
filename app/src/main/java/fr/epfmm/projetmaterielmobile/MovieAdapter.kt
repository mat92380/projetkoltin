package fr.epfmm.projetmaterielmobile

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class MovieViewHolder(view: View) : ViewHolder(view)

class MovieAdapter(val activity: Context, val movieList: List<Movie>) :
    RecyclerView.Adapter<MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        Log.d("PopularMovie inside Adapter: ", movieList.toString())
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.movieview, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int = movieList.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {

        val movie = movieList[position]

        val view = holder.itemView

        val titre = view.findViewById<TextView>(R.id.textViewTitle)
        val release = view.findViewById<TextView>(R.id.textViewReleaseYear)
        val note = view.findViewById<TextView>(R.id.textViewVoteAverage)

        titre.text = movie.title
        release.text = "Date de sortie : " + movie.release_date
        note.text = "Note : " +movie.vote_average.toString() + "/10"

        view.setOnClickListener {
            val intent = Intent(activity, MovieDetailsActivity::class.java)
            intent.putExtra("movie", movie)
//            intent.putExtra("date", movie.release_date)
//            intent.putExtra("title", movie.title)
//            intent.putExtra("overview", movie.overview)
//            intent.putExtra("language", movie.original_language)
//            intent.putExtra("note", movie.vote_average)
            activity.startActivity(intent)
        }
    }

}




