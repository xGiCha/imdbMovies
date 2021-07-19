package gr.imdb.movies.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import gr.imdb.movies.R
import gr.imdb.movies.adapters.MovieAdapter
import gr.imdb.movies.database.MoviesEntity
import gr.imdb.movies.pageListSourceFiles.MoviesDataSource
import gr.imdb.movies.util.hideKeyboard
import gr.imdb.movies.viewmodels.MovieViewModel
import kotlinx.android.synthetic.main.fragment_movies_home.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesHomeFragment : Fragment() {

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var adapter: MovieAdapter
    private var listener: GenericInterface? = null
    private lateinit var moviesEntity: MoviesEntity
    private var movieFavoriteList = mutableListOf<MoviesEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieViewModel = ViewModelProvider(requireActivity()).get(MovieViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is GenericInterface) {
            listener = context
        } else {
            throw RuntimeException("$context must implement GenericInterface")
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setUpObservers()
        setUpListeners()
    }

    private fun stopSwipeProgress() {
        if (homeSwipeRefreshLayout.isRefreshing) {
            homeSwipeRefreshLayout.isRefreshing = false
        }
    }

    private fun setUpListeners() {

        homeSwipeRefreshLayout.setOnRefreshListener {
            homeSwipeRefreshLayout.isRefreshing = true
            getMovieList(1, "")
            listener?.clearEditText()
            hideKeyboard()
        }
    }

    fun getMovieList(mode: Int, searchQuery: String) {
        movieViewModel.getMovieList(mode, searchQuery)?.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            stopSwipeProgress()
        })
    }

    private fun setUpObservers() {
        getMovieList(1, "")

        readFromDB()
    }

    private fun init() {
        adapter = MovieAdapter(requireContext(), { id, title, movie ->
            findNavController().navigate(MoviesHomeFragmentDirections.actionMoviesHomeFragmentToMoviesDetailsFragment(id, movie))
            hideKeyboard()
            listener?.removeCallBacks()
        }, { movie ->
            moviesEntity = MoviesEntity(movie)
            if (!movie.isFavorite) {
                movieViewModel.deleteMovie(moviesEntity)
            } else {
                moviesEntity.favoriteSelected = movie.isFavorite
                movieViewModel.insertMovies(moviesEntity)
            }
        })
        moviesRV.layoutManager = LinearLayoutManager(requireContext())
        moviesRV.adapter = adapter


        MoviesDataSource.listMoviesCallback = { moviesList ->
            for (i in movieFavoriteList.indices) {
                for (l in moviesList.indices) {
                    if (movieFavoriteList[i].movie.id == moviesList[l].id) {
                        moviesList[l].isFavorite = movieFavoriteList[i].favoriteSelected
                        break
                    }
                }
            }

            when (moviesList.size) {
                0 -> {
                    noMoviesFoundTxtV.visibility = View.VISIBLE
                }
                else -> {
                    noMoviesFoundTxtV.visibility = View.GONE
                }
            }
        }
    }

    fun readFromDB() {
        lifecycleScope.launch {
            movieViewModel.readMovies.observe(viewLifecycleOwner, Observer {
                movieFavoriteList = it.toMutableList()
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        listener = null
    }

    override fun onResume() {
        super.onResume()
        listener?.clearEditText()
    }
}