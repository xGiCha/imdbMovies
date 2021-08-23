package gr.imdb.movies.pageListSourceFiles

import android.content.Entity
import androidx.paging.PageKeyedDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import gr.imdb.movies.data.Repository
import gr.imdb.movies.models.EnitityMovie
import gr.imdb.movies.models.Movie
import gr.imdb.movies.util.Constants.Companion.API_KEY
import gr.imdb.movies.util.Constants.Companion.MOVIES_BY_ID
import gr.imdb.movies.util.Constants.Companion.POPULAR_MOVIES
import gr.imdb.movies.util.Constants.Companion.SEARCH_MOVIES
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class MovieListDataSource @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val repository: Repository

) : PageKeyedDataSource<Int, Movie>() {

    companion object {
        var listMoviesCallback :((List<Movie>) -> Unit)? = null
    }

    private val page = 1

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Movie>) {
        coroutineScope.launch {
            kotlin.runCatching {
                selectApi(page)
            }.onFailure {
                it.printStackTrace()
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    callback.onResult(response.body()?.results?.toMutableList() ?: listOf(), null, 1)
                    listMoviesCallback?.invoke(response.body()?.results?.toMutableList() ?: listOf())
                }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        coroutineScope.launch {
            kotlin.runCatching {
                selectApi(params.key - page)
            }.onFailure {
                it.printStackTrace()
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    callback.onResult(response.body()?.results?.toMutableList() ?: listOf(), params.key - 1)
                }
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        coroutineScope.launch {
            kotlin.runCatching {
                selectApi(params.key + page)
            }.onFailure {
                it.printStackTrace()
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    callback.onResult(response.body()?.results?.toMutableList() ?: listOf(), params.key + 1)
                }
            }
        }
    }

    private suspend fun selectApi(page: Int): Response<EnitityMovie>{
        return repository.remote.getPopularMovies(page)
    }
}