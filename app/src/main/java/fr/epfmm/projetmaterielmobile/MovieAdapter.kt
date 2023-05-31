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
        val overview = view.findViewById<TextView>(R.id.textViewOverview)

        titre.text = movie.title
//        release.text = movie.release_date
//        overview.text = movie.overview

        /*view.setOnClickListener {
            val intent = Intent(activity, MovieDetailActivity::class.java)
            intent.putExtra("release_date", movie.release_date)
            intent.putExtra("movieTitle", movie.title)
            activity.startActivity(intent)
        }*/
    }

}

/*class MovieAdapter(private val movies: List<Movie>) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    // Créez une classe interne ViewHolder pour contenir les vues des éléments de la liste
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Déclarez les vues spécifiques à chaque élément de la liste ici
        // Par exemple, vous pouvez avoir un TextView pour afficher le titre du film
        val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Créez la vue pour chaque élément de la liste en inflatant le layout de l'élément
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movieview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Récupérez l'objet Movie correspondant à la position donnée dans la liste
        val movie = movies[position]

        // Utilisez les données du film pour remplir les vues de l'élément de la liste
        holder.titleTextView.text = movie.title
    }

    override fun getItemCount(): Int {
        // Retournez le nombre total d'éléments dans la liste
        return movies.size
    }
    }*/



