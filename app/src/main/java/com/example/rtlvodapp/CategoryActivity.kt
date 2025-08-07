package com.example.rtlvodapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rtlvodapp.model.VideoItem

class CategoryActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryTitle: TextView
    private lateinit var backButton: ImageView  // New: The go home door

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)  // Uses updated layout

        categoryTitle = findViewById(R.id.categoryTitle)
        recyclerView = findViewById(R.id.videoRecyclerView)
        backButton = findViewById(R.id.backButton)  // New: Find the back button

        recyclerView.layoutManager = GridLayoutManager(this, 4)  // 4x4 grid

        val categoryName = intent.getStringExtra("categoryName") ?: "Unknown"
        val videos = intent.getParcelableArrayListExtra<VideoItem>("videos") ?: arrayListOf()

        categoryTitle.text = categoryName
        val adapter = VideoAdapter(this, videos)  // Reuse the same card builder
        recyclerView.adapter = adapter

        // New: Tap back button—go home
        backButton.setOnClickListener {
            finish()  // Closes this room and returns to VideoListActivity
        }
    }
}