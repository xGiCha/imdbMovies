package gr.imdb.movies.data

import gr.imdb.movies.models.EnitityMovie
import gr.imdb.movies.models.Movie
import gr.imdb.movies.models.MovieEn
import gr.imdb.movies.models.Review
import gr.imdb.movies.models.Video.Video
import gr.imdb.movies.models.Video.VideoEntity
import gr.imdb.movies.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {

    @GET("/3/movie/popular")
    suspend fun getPopularMovies(
            @Query("page") page: Int

    ): Response<EnitityMovie>

    @GET("/3/search/movie")
    suspend fun searchMovies(
            @Query("page") page: Int,
            @Query("query") query: String
    ): Response<EnitityMovie>

    @GET("/3/movie/{movie_id}")
    suspend fun getMovieById(
            @Path("movie_id") movie_id: Int
    ): Response<MovieEn>

    @GET("/3/movie/{movie_id}/reviews")
    suspend fun getReviewsById(
            @Path("movie_id") movie_id: Int
    ): Response<Review>

    @GET("/3/movie/{movie_id}/similar")
    suspend fun getSimilarMoviesById(
            @Path("movie_id") movie_id: Int,
            @Query("page") page: Int
    ): Response<EnitityMovie>

    @GET("/3/movie/{movie_id}/videos")
    suspend fun getVideoById(
            @Path("movie_id") movie_id: Int
    ): Response<Video>

}