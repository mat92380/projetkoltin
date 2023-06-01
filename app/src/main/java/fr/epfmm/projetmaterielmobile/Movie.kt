package fr.epfmm.projetmaterielmobile

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    var adult: Boolean? = false,
    var overview: String? = "",
    var releaseDate: String? = "",
//    var genreIds: ArrayList<Int> = arrayListOf(),
    var id: Int? = 0,
    var originalTitle: String? = "",
    var originalLanguage: String? = "",
    var title: String? = "",
    var popularity: Double? = 0.0,
    var voteCount: Int? = 0,
    var voteAverage: Double? = 0.0,
    val release_date: String
) : Parcelable {
    companion object{

    }
    class SearchMoviesResponse(
        val page: Int,
        val results: List<Movie>,
        val total_results: Int,
        val total_pages: Int
    )

    override fun toString(): String {
        return "Movie(id='$id', title='$title')"
//        return "{" +
//                "adult='$adult', " +
//                "overview='$overview', " +
//                "releaseDate='$releaseDate', " +
//                "id='$id', " +
//                "originalTitle='$originalTitle', " +
//                "originalLanguage=$originalLanguage, " +
//                "title=$title, " +
//                "popularity=$popularity, " +
//                "voteCount=$voteCount, " +
//                "voteAverage=$voteAverage, " +
//                "release_date=$release_date " +
//                "}"
    }



}
