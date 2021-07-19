package gr.imdb.movies.database

import androidx.room.*
import gr.imdb.movies.models.Movie
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(moviesEntity: MoviesEntity)

//    @Query("SELECT * FROM movies_table ORDER BY id ASC")
//    fun readMovies(): Flow<List<Movie>>

    @Query("SELECT * FROM movies_table")
    fun readMovies(): Flow<List<MoviesEntity>>

    @Delete
    suspend fun deleteMovie(moviesEntity: MoviesEntity)

}