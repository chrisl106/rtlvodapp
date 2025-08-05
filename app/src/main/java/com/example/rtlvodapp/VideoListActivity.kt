package com.example.rtlvodapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.rtlvodapp.model.VideoItem
import com.example.rtlvodapp.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VideoListActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var token: String
    private lateinit var videoList: List<VideoItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_list)

        listView = findViewById(R.id.videoListView)
        token = intent.getStringExtra("token") ?: ""

        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiClient.apiService.getVideos()
                }

                if (response.isSuccessful && response.body() != null) {
                    videoList = response.body()!!
                    val titles = videoList.map { it.title }

                    val adapter = ArrayAdapter(this@VideoListActivity, android.R.layout.simple_list_item_1, titles)
                    listView.adapter = adapter

                    listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                        val video = videoList[position]
                        val intent = Intent(this@VideoListActivity, VideoPlayerActivity::class.java)
                        intent.putExtra("videoUrl", video.videoUrl)
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(this@VideoListActivity, "Failed to load videos", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Log.e("VideoList", "Error: ${e.message}")
                Toast.makeText(this@VideoListActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
