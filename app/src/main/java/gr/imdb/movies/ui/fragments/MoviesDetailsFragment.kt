package gr.imdb.movies.ui.fragments

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import gr.imdb.movies.R
import gr.imdb.movies.adapters.ReviewsAdapter
import gr.imdb.movies.adapters.SimilarMovieAdapter
import gr.imdb.movies.database.MoviesEntity
import gr.imdb.movies.databinding.FragmentMoviesDetailsBinding
import gr.imdb.movies.models.EntityReview
import gr.imdb.movies.models.Movie
import gr.imdb.movies.util.Constants.Companion.IMDB_URL
import gr.imdb.movies.util.isDark
import gr.imdb.movies.util.loadImage
import gr.imdb.movies.viewmodels.MovieViewModel


class MoviesDetailsFragment() : Fragment() {

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var binding: FragmentMoviesDetailsBinding
    private lateinit var adapter: SimilarMovieAdapter
    private lateinit var adapterReview: ReviewsAdapter
    private val args: MoviesDetailsFragmentArgs by navArgs()
    private var myMovie: Movie? = null
    private lateinit var moviesEntity: MoviesEntity
    private var mReviewList: MutableList<EntityReview> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        movieViewModel = ViewModelProvider(requireActivity()).get(MovieViewModel::class.java)
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMoviesDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setUpObservers()
        setUpListeners()
        init()
    }

    private fun setUpListeners() {
        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.favoriteDetailsImg.setOnClickListener {
            myMovie?.let { movie ->
                moviesEntity = MoviesEntity(movie)
                when (movie.isFavorite) {
                    true -> {
                        movie.isFavorite = false
                        moviesEntity.favoriteSelected = movie.isFavorite
                        movieViewModel.deleteMovie(moviesEntity)
                        binding.favoriteDetailsImg.background =
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_favorite_unselect)
                    }
                    else -> {
                        movie.isFavorite = true
                        moviesEntity.favoriteSelected = movie.isFavorite
                        movieViewModel.insertMovies(moviesEntity)
                        binding.favoriteDetailsImg.background =
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_favorite_selected)
                    }
                }
            }
        }
    }

    private fun setUpObservers() {
                movieViewModel.getMovieList(3, args.movieId.toString())?.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })


        movieViewModel.movieById.observe(viewLifecycleOwner, Observer {
            binding.item = it.data
        })

        movieViewModel.reviewsById.observe(viewLifecycleOwner, Observer {
            binding.review = it.data
            mReviewList.clear()
            it.data?.results?.let {
                it.forEachIndexed { index, entityReview ->
                    when (index) {
                        0, 1 -> {
                            mReviewList.add(entityReview)
                        }
                    }
                }
            }
            mReviewList.toList().let { it1 -> adapterReview.updateItems(it1) }
            if (mReviewList.size == 0) {
                binding.reviewTxtV.reviewNameTxtV.visibility = View.VISIBLE
                binding.reviewTxtV.reviewNameTxtV.text = getString(R.string.no_reviews)
            }else{
                binding.reviewTxtV.reviewNameTxtV.visibility = View.GONE
            }
        })
    }

    private fun init() {
        args.movie.let { movie ->
            myMovie = movie
            myMovie?.let {
                movieViewModel.getMovieById(it.id)
                movieViewModel.getReviewsById(it.id)
                when (myMovie?.isFavorite) {
                    true -> {
                        binding.favoriteDetailsImg.background =
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_favorite_selected)
                    }
                    else -> {
                        binding.favoriteDetailsImg.background =
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_favorite_unselect)
                    }
                }
                it.backdropPath?.let {
                    loadImageFromUrl(it)
                }
            }
        }

        binding.similarMovies.reviewNameTxtV.visibility = View.GONE
        binding.similarMovies.bodyInfoTxtV.visibility = View.GONE

        binding.reviewTxtV.reviewNameTxtV.visibility = View.GONE
        binding.reviewTxtV.bodyInfoTxtV.visibility = View.GONE

        setUpAdapters()
    }

    fun loadImageFromUrl(image: String){
        val url = IMDB_URL + image

        loadImage(requireContext(), url, binding.movieDetailsImage)

        Picasso.with(context).load(url).into(object : com.squareup.picasso.Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                bitmap?.let {
                    if(isDark(it)){
                        binding.movieTitleTxtV.setTextColor(Color.WHITE)
                        binding.genres.setTextColor(Color.WHITE)
                    }else{
                        binding.movieTitleTxtV.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                        binding.genres.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                    }
                }
            }

            override fun onBitmapFailed(errorDrawable: Drawable?) {
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

        })
    }

    fun setUpAdapters() {
        adapter = SimilarMovieAdapter(requireContext()){movie ->
            findNavController().navigate(MoviesDetailsFragmentDirections.actionMoviesDetailsFragmentSelf(movie.id, movie))
        }
        binding.similarMoviesRV.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.similarMoviesRV.adapter = adapter

        adapterReview = ReviewsAdapter(requireContext(), mutableListOf())
        binding.reviewMoviesRV.layoutManager = LinearLayoutManager(requireContext())
        binding.reviewMoviesRV.adapter = adapterReview
    }

}