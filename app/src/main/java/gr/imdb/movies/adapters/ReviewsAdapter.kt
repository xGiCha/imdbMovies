package gr.imdb.movies.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.imdb.movies.R
import gr.imdb.movies.models.EntityReview
import kotlinx.android.synthetic.main.review_item.view.*

class ReviewsAdapter(
        val context: Context,
        val reviews: MutableList<EntityReview>)

    : RecyclerView.Adapter<ReviewsAdapter.ViewHolder>() {

    private lateinit var parentContext: Context

    override fun getItemCount() = reviews.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.review_item, parent, false)
    )

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: EntityReview, position: Int) {

            itemView.reviewNameTxtV.text = item.author
            itemView.bodyInfoTxtV.text = item.content
        }
    }


    fun updateItems(newItems: List<EntityReview>) {
        reviews.clear()
        reviews.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(reviews[position], position)
    }


}