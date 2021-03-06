package gr.imdb.movies.viewmodels

import android.app.Application
import android.provider.MediaStore
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import gr.imdb.movies.pageListSourceFiles.MoviesDataSourceFactory
import gr.imdb.movies.data.Repository
import gr.imdb.movies.database.MoviesEntity
import gr.imdb.movies.models.Movie
import gr.imdb.movies.models.EnitityMovie
import gr.imdb.movies.models.MovieEn
import gr.imdb.movies.models.Review
import gr.imdb.movies.models.Video.Video
import gr.imdb.movies.pageListSourceFiles.MovieListDataSourceFactory
import gr.imdb.movies.util.Constants.Companion.API_KEY
import gr.imdb.movies.util.NetworkResult
import gr.imdb.movies.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieViewModel @ViewModelInject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    /** RETROFIT */
    var movieById: MutableLiveData<NetworkResult<MovieEn>> = MutableLiveData()
    var reviewsById: SingleLiveEvent<NetworkResult<Review>> = SingleLiveEvent()
    var videoById: SingleLiveEvent<NetworkResult<Video>> = SingleLiveEvent()
    var rvPosition: SingleLiveEvent<Int> = SingleLiveEvent()

    var movieList: LiveData<PagedList<Movie>>? = null

    private val config = PagedList.Config.Builder()
            .setPageSize(20)
            .setInitialLoadSizeHint(20)
            .setEnablePlaceholders(false)
            .build()

    init {
        movieList = initializedMoviePagedListBuilder(config).build()
    }

    fun getReviewsById(movieId: Int){
        viewModelScope.launch(Dispatchers.Default) {
            runCatching {
                repository.remote.getReviewsById(movieId)
            }.onFailure {
                it.printStackTrace()
            }.onSuccess {response ->
                if(response.isSuccessful){
                    response.body()?.let {
                        reviewsById.postValue(NetworkResult.Success(it))
                    }
                }else{
                    reviewsById.postValue(NetworkResult.Error("something went wrong"))
                }
            }
        }
    }

    fun getVideoById(movieId: Int){
        viewModelScope.launch(Dispatchers.Default) {
            runCatching {
                repository.remote.getVideoById(movieId)
            }.onFailure {
                it.printStackTrace()
            }.onSuccess {response ->
                if(response.isSuccessful){
                    response.body()?.let {
                        videoById.postValue(NetworkResult.Success(it))
                    }
                }else{
                    videoById.postValue(NetworkResult.Error("something went wrong"))
                }
            }
        }
    }

    fun getMovieById(movieId: Int){
        viewModelScope.launch(Dispatchers.Default) {
            runCatching {
                repository.remote.getMovieById(movieId)
            }.onFailure {
                it.printStackTrace()
            }.onSuccess {response ->
                if(response.isSuccessful){
                    response.body()?.let {
                        movieById.postValue(NetworkResult.Success(it))
                    }
                }else{
                    movieById.postValue(NetworkResult.Error("something went wrong"))
                }
            }
        }
    }

    fun getMovieList(mode: Int, searchQuery: String):  LiveData<PagedList<Movie>>?{
        return  initializedPagedListBuilder(config, mode, searchQuery).build()
    }

    fun popularMovies() = movieList

    private fun initializedPagedListBuilder(config: PagedList.Config, mode: Int, searchQuery: String): LivePagedListBuilder<Int, Movie> {
        val dataSourceFactory = MoviesDataSourceFactory(viewModelScope, repository, mode, searchQuery)
        return LivePagedListBuilder<Int, Movie>(dataSourceFactory, config)
    }

    private fun initializedMoviePagedListBuilder(config: PagedList.Config): LivePagedListBuilder<Int, Movie> {
        val dataSourceFactory = MovieListDataSourceFactory(viewModelScope, repository)
        return LivePagedListBuilder<Int, Movie>(dataSourceFactory, config)
    }

    /** ROOM DATABASE */
    val readMovies: LiveData<List<MoviesEntity>> = repository.local.readMovies().asLiveData()

    fun insertMovies(moviesEntity: MoviesEntity) =
            viewModelScope.launch(Dispatchers.IO) {
                repository.local.insertMovies(moviesEntity)
            }

    fun deleteMovie(moviesEntity: MoviesEntity) =
            viewModelScope.launch(Dispatchers.IO) {
                repository.local.deleteMovie(moviesEntity)
            }
}