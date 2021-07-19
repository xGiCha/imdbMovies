package gr.imdb.movies.pageListSourceFiles

import androidx.paging.PageKeyedDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import gr.imdb.movies.data.Repository
import gr.imdb.movies.models.Movie
import gr.imdb.movies.util.Constants.Companion.API_KEY
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
                    1 ->{
                        repository.remote.getPopularMovies(API_KEY, 1)
                    }
                    2->{
                        repository.remote.searchMovies(API_KEY,1, searchQuery)
                    }
                    3->{
                        repository.remote.getSimilarMoviesById(searchQuery.toInt(), API_KEY, 1)
                    }
                    else -> {
                        repository.remote.getPopularMovies(API_KEY, 1)
                    }
                }

            }.onFailure {
                it.printStackTrace()
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    callback.onResult(response.body()?.results?.toMutableList() ?: listOf(), null, 1)
                    listSizeCallback?.invoke(listSize)
                    when(mode){
                        1,2 ->{
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
                    1 ->{
                        repository.remote.getPopularMovies(API_KEY, params.key)
                    }
                    2->{
                        repository.remote.searchMovies(API_KEY,params.key, searchQuery)
                    }
                    3->{
                        repository.remote.getSimilarMoviesById(searchQuery.toInt(), API_KEY, 1)
                    }
                    else -> {
                        repository.remote.getPopularMovies(API_KEY, params.key)
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
                    1 ->{
                        repository.remote.getPopularMovies(API_KEY, params.key)
                    }
                    2->{
                        repository.remote.searchMovies(API_KEY,params.key, searchQuery)
                    }
                    3->{
                        repository.remote.getSimilarMoviesById(searchQuery.toInt(), API_KEY, 1)
                    }
                    else -> {
                        repository.remote.getPopularMovies(API_KEY, params.key)
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