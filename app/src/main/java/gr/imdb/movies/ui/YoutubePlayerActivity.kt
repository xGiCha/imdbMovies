package gr.imdb.movies.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import gr.imdb.movies.R
import gr.imdb.movies.util.Constants.Companion.VIDEO_ID
import gr.imdb.movies.util.Constants.Companion.YOUTUBE_API_KEY
import kotlinx.android.synthetic.main.activity_youtube_player.*

class YoutubePlayerActivity : YouTubeBaseActivity() {

    private  var videoId = ""
    lateinit var youtubePlayerInit: YouTubePlayer.OnInitializedListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_player)
        videoId = intent.getStringExtra(VIDEO_ID).toString()
        initUI()
    }

    private fun initUI() {
        youtubePlayerInit = object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, youtubePlayer: YouTubePlayer?, p2: Boolean) {
                youtubePlayer?.loadVideo(videoId)
            }

            override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
                Toast.makeText(applicationContext, "Something went wrong !! ", Toast.LENGTH_SHORT).show()
            }
        }
        youtubePlayerContainer.initialize(YOUTUBE_API_KEY, youtubePlayerInit)
    }
}