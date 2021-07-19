package gr.imdb.movies.pageListSourceFiles

import androidx.paging.DataSource
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityRetainedScoped
import gr.imdb.movies.data.Repository
import gr.imdb.movies.models.Movie
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class MoviesDataSourceFactory @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val repository: Repository,
    private val mode: Int,
    private val searchQuery: String
) : DataSource.Factory<Int, Movie>(){

    override fun create(): DataSource<Int, Movie> {
        return MoviesDataSource(coroutineScope, repository, mode, searchQuery)
    }
}