package gr.imdb.movies.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import gr.imdb.movies.databinding.SimilarMovieItemBinding
import gr.imdb.movies.models.Movie

class SimilarMovieAdapter(
    val context: Context,
    val callback: (movie: Movie) -> Unit)

    :  PagedListAdapter<Movie, SimilarMovieAdapter.ViewHolder>(DiffCallback()) {

    private lateinit var parentContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        parentContext = parent.context
        val binding: SimilarMovieItemBinding = SimilarMovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, position) }
    }


    inner class ViewHolder(val binding: SimilarMovieItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Movie, position: Int) {
            binding.item = item

            binding.cardContainer.setOnClickListener {
                callback.invoke(item)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Movie>() {
        // your DiffCallback implementation
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id && oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.title == newItem.title && oldItem.id == newItem.id
        }
    }
}