package gr.imdb.movies.data

import gr.imdb.movies.database.MoviesDao
import gr.imdb.movies.database.MoviesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val moviesDao: MoviesDao
) {

    fun readMovies(): Flow<List<MoviesEntity>> {
        return moviesDao.readMovies()
    }

    suspend fun insertMovies(moviesEntity: MoviesEntity) {
        moviesDao.insertMovies(moviesEntity)
    }

    suspend fun deleteMovie(moviesEntity: MoviesEntity) {
        moviesDao.deleteMovie(moviesEntity)
    }

}