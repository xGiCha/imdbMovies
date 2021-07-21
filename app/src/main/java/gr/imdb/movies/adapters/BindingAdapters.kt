package gr.imdb.movies.adapters

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import gr.imdb.movies.R
import gr.imdb.movies.models.Genre
import gr.imdb.movies.util.Constants
import gr.imdb.movies.util.formatDate
import gr.imdb.movies.util.isDark
import gr.imdb.movies.util.loadImage
import java.util.*


object BindingAdapters {

    private var isDark = false

    @BindingAdapter("ratingV")
    @JvmStatic
    fun RatingBar.ratingV(ratingV: Double) {
        val value = ratingV.toFloat() / 2
        rating = value
    }

    @BindingAdapter("date")
    @JvmStatic
    fun TextView.date(date: String?) {
        text = if (!date.isNullOrEmpty()) {
            formatDate(date)
        } else {
            ""
        }
    }

    @BindingAdapter("image")
    @JvmStatic
    fun ImageView.image(image: String?) {
        val url = Constants.IMDB_URL + image
        loadImage(context, url, this )
    }

    @BindingAdapter("imageRaw")
    @JvmStatic
    fun ImageView.imageRaw(imageRaw: String?) {

        val url = "https://image.tmdb.org/t/p/w500$imageRaw"

        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.blank)
                .into(this);

    }

    @BindingAdapter("genre")
    @JvmStatic
    fun TextView.genre(genre: List<Genre>?) {
        var i = 0
        var genres = ""
        genre?.forEach {

            genres = if (genre.size == 1)
                it.name
            else {
                if (genre.size - 1 != i)
                    genres + it.name + ", "
                else
                    genres + it.name
            }
            i++
        }
        text = genres
    }
}