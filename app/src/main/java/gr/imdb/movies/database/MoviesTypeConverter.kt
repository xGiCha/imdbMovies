package gr.imdb.movies.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import gr.imdb.movies.models.Movie

class MoviesTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun movieToString(movie: Movie): String {
        return gson.toJson(movie)
    }

    @TypeConverter
    fun stringToMovie(data: String): Movie {
        val listType = object : TypeToken<Movie>() {}.type
        return gson.fromJson(data, listType)
    }

}