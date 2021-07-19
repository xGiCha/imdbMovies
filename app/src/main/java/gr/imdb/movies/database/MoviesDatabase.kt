package gr.imdb.movies.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import gr.imdb.movies.models.Movie

@Database(
    entities = [MoviesEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(MoviesTypeConverter::class)
abstract class MoviesDatabase: RoomDatabase() {

    abstract fun moviesDao(): MoviesDao

}