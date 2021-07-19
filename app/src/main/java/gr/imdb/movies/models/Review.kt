package gr.imdb.movies.models


import com.google.gson.annotations.SerializedName

data class Review(
        @SerializedName("id")
    val id: Int,
        @SerializedName("page")
    val page: Int,
        @SerializedName("results")
    val results: List<EntityReview>,
        @SerializedName("total_pages")
    val totalPages: Int,
        @SerializedName("total_results")
    val totalResults: Int
)