package gr.imdb.movies.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import gr.imdb.movies.R
import gr.imdb.movies.databinding.MovieItemBinding
import gr.imdb.movies.models.Movie

class MovieAdapter(
    val context: Context,
    val movieId: (id: Int, title: String, movie: Movie) -> Unit,
    val favorite: (movie: Movie) -> Unit)

    :  PagedListAdapter<Movie, MovieAdapter.ViewHolder>(DiffCallback()) {

    private lateinit var parentContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        parentContext = parent.context
        val binding: MovieItemBinding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, position) }
    }


    inner class ViewHolder(val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Movie, position: Int) {
            binding.item = item

            if(item.isFavorite){
                binding.favorite.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_favorite_selected))
            }else
                binding.favorite.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_favorite_unselect))

            binding.cardContainer.setOnClickListener {
                movieId.invoke(item.id, item.title, item)
            }
            binding.favorite.setOnClickListener {
                if(item.isFavorite){
                    item.isFavorite = false
                    binding.favorite.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_favorite_unselect))
                }else{
                    item.isFavorite = true
                    binding.favorite.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_favorite_selected))
                }
                favorite.invoke(item)
            }

        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Movie>() {
        // your DiffCallback implementation
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.isFavorite == newItem.isFavorite && oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.isFavorite == newItem.isFavorite && oldItem.id == newItem.id
        }
    }
}