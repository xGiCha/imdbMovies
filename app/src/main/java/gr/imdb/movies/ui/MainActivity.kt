package gr.imdb.movies.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import gr.imdb.movies.R
import gr.imdb.movies.ui.fragments.GenericInterface
import gr.imdb.movies.ui.fragments.MoviesHomeFragment
import gr.imdb.movies.util.Constants.Companion.POPULAR_MOVIES
import gr.imdb.movies.util.Constants.Companion.SEARCH_MOVIES
import gr.imdb.movies.util.hideKeyboard
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), GenericInterface {

    private lateinit var navController: NavController
    val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.navHostFragment)

        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.moviesHomeFragment ->{
                    searchContainer.visibility = View.VISIBLE
                }
                else->{
                    searchContainer.visibility = View.GONE
                }
            }

            searchImgView.setOnClickListener {
                hideKeyboard()
                movieEditTxt.clearFocus()
            }

            val currentFragment = navHostFragment.childFragmentManager.fragments[0]
            if(currentFragment is MoviesHomeFragment) {
                movieEditTxt.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        println()
                    }

                    override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        println()
                    }

                    override fun afterTextChanged(s: Editable?) {
                        s?.toString()?.let {
                            handler.removeCallbacksAndMessages(null)
                            handler.postDelayed({
                                if (it != "") {
                                    currentFragment.getMovieList(SEARCH_MOVIES, it)

                                } else {
                                    currentFragment.getMovieList(POPULAR_MOVIES)
                                }
                            }, 600)
                        }
                    }
                })
            }
        }
    }

    override fun clearEditText() {
        movieEditTxt.clearFocus()
//        movieEditTxt.setText("")
    }

    override fun removeCallBacks() {
        handler.removeCallbacksAndMessages(null)
    }
}