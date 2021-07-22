package gr.imdb.movies.models.Video


import com.google.gson.annotations.SerializedName

data class Video(
    @SerializedName("id")
    val id: Int,
    @SerializedName("results")
    val results: List<VideoEntity>
)