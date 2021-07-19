package gr.imdb.movies.data

import gr.imdb.movies.models.EnitityMovie
import gr.imdb.movies.models.Movie
import gr.imdb.movies.models.MovieEn
import gr.imdb.movies.models.Review
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val moviesApi: MoviesApi
) {

    suspend fun getPopularMovies(api_key: String, page: Int): Response<EnitityMovie>{
        return moviesApi.getPopularMovies(api_key, page)
    }

    suspend fun searchMovies(api_key: String, page: Int, query: String): Response<EnitityMovie>{
        return moviesApi.searchMovies(api_key, page, query)
    }

    suspend fun getMovieById(movie_id: Int, api_key: String): Response<MovieEn>{
        return moviesApi.getMovieById(movie_id, api_key)
    }

    suspend fun getReviewsById(movie_id: Int, api_key: String): Response<Review>{
        return moviesApi.getReviewsById(movie_id, api_key)
    }

    suspend fun getSimilarMoviesById(movie_id: Int, api_key: String, page: Int): Response<EnitityMovie>{
        return moviesApi.getSimilarMoviesById(movie_id, api_key, page)
    }
}