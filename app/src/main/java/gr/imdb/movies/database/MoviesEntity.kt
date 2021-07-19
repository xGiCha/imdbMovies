package gr.imdb.movies.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import gr.imdb.movies.models.Movie
import gr.imdb.movies.util.Constants.Companion.MOVIES_TABLE

@Entity(tableName = MOVIES_TABLE)
class MoviesEntity(
        var movie: Movie
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = movie.id

    var favoriteSelected: Boolean = false
}