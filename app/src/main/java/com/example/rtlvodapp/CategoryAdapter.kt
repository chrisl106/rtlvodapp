package com.example.rtlvodapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rtlvodapp.model.VideoItem

class CategoryAdapter(private val context: Context, private val categories: Map<String, List<VideoItem>>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private val categoryList = categories.keys.toList()

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryTitle: TextView = itemView.findViewById(R.id.categoryTitle)
        val videoRecyclerView: RecyclerView = itemView.findViewById(R.id.videoRecyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        holder.categoryTitle.text = category

        val videos = categories[category] ?: emptyList()
        val adapter = VideoAdapter(context, videos)
        holder.videoRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)  // Makes it slide left-right
        holder.videoRecyclerView.adapter = adapter

        // New: Make category title clickable—open special room for browsing
        holder.categoryTitle.setOnClickListener {
            val intent = Intent(context, CategoryActivity::class.java)
            intent.putExtra("categoryName", category)
            intent.putParcelableArrayListExtra("videos", ArrayList(videos))  // Send the videos to new room
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = categoryList.size
}