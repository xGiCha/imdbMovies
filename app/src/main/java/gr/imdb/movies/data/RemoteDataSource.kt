package gr.imdb.movies.data

import gr.imdb.movies.models.EnitityMovie
import gr.imdb.movies.models.Movie
import gr.imdb.movies.models.MovieEn
import gr.imdb.movies.models.Review
import gr.imdb.movies.models.Video.Video
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val moviesApi: MoviesApi
) {

    suspend fun getPopularMovies(page: Int): Response<EnitityMovie>{
        return moviesApi.getPopularMovies(page)
    }

    suspend fun searchMovies(page: Int, query: String): Response<EnitityMovie>{
        return moviesApi.searchMovies(page, query)
    }

    suspend fun getMovieById(movie_id: Int): Response<MovieEn>{
        return moviesApi.getMovieById(movie_id)
    }

    suspend fun getReviewsById(movie_id: Int): Response<Review>{
        return moviesApi.getReviewsById(movie_id)
    }

    suspend fun getSimilarMoviesById(movie_id: Int, page: Int): Response<EnitityMovie>{
        return moviesApi.getSimilarMoviesById(movie_id, page)
    }

    suspend fun getVideoById(movie_id: Int): Response<Video>{
        return moviesApi.getVideoById(movie_id)
    }
}