package gr.imdb.movies.pageListSourceFiles

import androidx.paging.DataSource
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityRetainedScoped
import gr.imdb.movies.data.Repository
import gr.imdb.movies.models.Movie
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class MovieListDataSourceFactory @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val repository: Repository
) : DataSource.Factory<Int, Movie>(){

    override fun create(): DataSource<Int, Movie> {
        return MovieListDataSource(coroutineScope, repository)
    }
}