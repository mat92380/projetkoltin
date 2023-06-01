package fr.epfmm.projetmaterielmobile

import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String
    ) : ArrayList<Movie>

    @GET("search/movie")
    suspend fun getMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ) : /*List<Movie>*/ Movie.SearchMoviesResponse

    @GET("movie/{movie_id}/recommendations")
    suspend fun getRecommandatedMovies(
        @Query("api_key") apiKey: String,
        @Query("query") Id: Int?
    ) : Movie.SearchMoviesResponse

}