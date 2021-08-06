package gr.imdb.movies.pageListSourceFiles

import androidx.paging.PageKeyedDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import gr.imdb.movies.data.Repository
import gr.imdb.movies.models.Movie
import gr.imdb.movies.util.Constants.Companion.API_KEY
import gr.imdb.movies.util.Constants.Companion.MOVIES_BY_ID
import gr.imdb.movies.util.Constants.Companion.POPULAR_MOVIES
import gr.imdb.movies.util.Constants.Companion.SEARCH_MOVIES
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MoviesDataSource @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val repository: Repository,
    private val mode: Int,
    private val searchQuery: String

) : PageKeyedDataSource<Int, Movie>() {

    companion object {
        var listSize = 0
        var listSizeCallback :((Int) -> Unit)? = null
        var listMoviesCallback :((List<Movie>) -> Unit)? = null
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Movie>) {
        coroutineScope.launch {
            kotlin.runCatching {
                when(mode){
                    POPULAR_MOVIES ->{
                        repository.remote.getPopularMovies( 1)
                    }
                    SEARCH_MOVIES->{
                        repository.remote.searchMovies(1, searchQuery)
                    }
                    MOVIES_BY_ID->{
                        repository.remote.getSimilarMoviesById(searchQuery.toInt(), 1)
                    }
                    else -> {
                        repository.remote.getPopularMovies( 1)
                    }
                }

            }.onFailure {
                it.printStackTrace()
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    callback.onResult(response.body()?.results?.toMutableList() ?: listOf(), null, 1)
                    listSizeCallback?.invoke(listSize)
                    when(mode){
                        POPULAR_MOVIES, SEARCH_MOVIES ->{
                            listMoviesCallback?.invoke(response.body()?.results?.toMutableList() ?: listOf())
                        }
                    }
                } else {
                    listSizeCallback?.invoke(-1)
                }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        coroutineScope.launch {
            kotlin.runCatching {
                when(mode){
                    POPULAR_MOVIES ->{
                        repository.remote.getPopularMovies( params.key - 1)
                    }
                    SEARCH_MOVIES->{
                        repository.remote.searchMovies(params.key - 1, searchQuery)
                    }
                    MOVIES_BY_ID->{
                        repository.remote.getSimilarMoviesById(searchQuery.toInt(),  params.key - 1)
                    }
                    else -> {
                        repository.remote.getPopularMovies( params.key - 1)
                    }
                }
            }.onFailure {
                it.printStackTrace()
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    callback.onResult(response.body()?.results?.toMutableList() ?: listOf(), params.key - 1)
                    listSize = response.body()?.totalPages ?: 0
                }
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        coroutineScope.launch {
            kotlin.runCatching {
                when(mode){
                    POPULAR_MOVIES ->{
                        repository.remote.getPopularMovies( params.key + 1)
                    }
                    SEARCH_MOVIES->{
                        repository.remote.searchMovies(params.key + 1, searchQuery)
                    }
                    MOVIES_BY_ID->{
                        repository.remote.getSimilarMoviesById(searchQuery.toInt(), params.key + 1)
                    }
                    else -> {
                        repository.remote.getPopularMovies( params.key + 1)
                    }
                }
            }.onFailure {
                it.printStackTrace()
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    callback.onResult(response.body()?.results?.toMutableList() ?: listOf(), params.key + 1)
                    listSize = response.body()?.totalPages ?: 0
                }
            }
        }
    }
}