package com.example.rtlvodapp

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rtlvodapp.databinding.ActivityVideoPlayerBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class VideoPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVideoPlayerBinding
    private lateinit var player: ExoPlayer
    private var playbackPosition: Long = 0  // New: Spot in the movie
    private var playWhenReady: Boolean = true  // New: Was it playing?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val videoUrl = intent.getStringExtra("videoUrl") ?: return  // Safe check

        player = ExoPlayer.Builder(this).build()
        binding.playerView.player = player

        val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))  // Uses full URL now
        player.setMediaItem(mediaItem)
        player.prepare()
        player.seekTo(playbackPosition)  // New: Jump to saved spot
        player.playWhenReady = playWhenReady  // New: Play if it was playing
        player.play()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        playbackPosition = player.currentPosition  // New: Save spot on flip
        playWhenReady = player.playWhenReady  // New: Save if playing
    }

    override fun onStop() {
        super.onStop()
        player.release()  // Always release to save power
    }
}