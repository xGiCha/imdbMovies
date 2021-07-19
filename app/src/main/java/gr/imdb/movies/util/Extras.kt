package gr.imdb.movies.util

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.showKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInputFromWindow(view.windowToken, InputMethodManager.SHOW_FORCED, 0)
}

fun formatDate(date: String): String {
    val oldDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val oldDate = oldDateFormat.parse(date)
    val newDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("el", "GR"))
    return newDateFormat.format(oldDate!!)
}

fun isDark(bitmap: Bitmap): Boolean {
    var dark = false
    val darkThreshold = bitmap.width * bitmap.height * 0.45f
    var darkPixels = 0
    val pixels = IntArray(bitmap.width * bitmap.height)
    bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
    for (pixel in pixels) {
        val color = pixel
        val r: Int = Color.red(color)
        val g: Int = Color.green(color)
        val b: Int = Color.blue(color)
        val luminance = 0.299 * r + 0.0f + 0.587 * g + 0.0f + 0.114 * b + 0.0f
        if (luminance < 150) {
            darkPixels++
        }
    }
    if (darkPixels >= darkThreshold) {
        dark = true
    }
    return dark
}