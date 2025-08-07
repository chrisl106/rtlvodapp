package com.example.rtlvodapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide  // Magic paintbrush for pictures
import com.example.rtlvodapp.model.VideoItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class VideoAdapter(private val context: Context, private val videos: List<VideoItem>) :
    RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val videoTitle: TextView = itemView.findViewById(R.id.videoTitle)
        val videoThumbnail: ImageView = itemView.findViewById(R.id.videoThumbnail)  // The picture spot
        val videoDescription: TextView = itemView.findViewById(R.id.videoDescription)  // Description spot
        val videoDate: TextView = itemView.findViewById(R.id.videoDate)  // Date spot
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videos[position]
        holder.videoTitle.text = video.title

        // Stick the picture with magic paintbrush!
        val thumbnailUrl = if (video.thumbnailPath != null) {
            "https://rtlvod.com/" + video.thumbnailPath
        } else {
            ""  // No picture? Empty
        }
        Glide.with(context)
            .load(thumbnailUrl)
            .placeholder(android.R.color.darker_gray)  // Gray while loading
            .error(android.R.color.black)  // Black if no picture
            .into(holder.videoThumbnail)

        // Set description
        holder.videoDescription.text = video.description

        // Format date properly
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())  // Matches ISO format
        val displayFormat = SimpleDateFormat("'Posted: 'yyyy-MM-dd", Locale.getDefault())  // Fixed: Quoted 'Posted: ' so it's literal text
        holder.videoDate.text = if (video.date.isNotEmpty()) {
            try {
                val date = dateFormat.parse(video.date)  // Parse the ISO date
                date?.let { displayFormat.format(it) } ?: "No date"
            } catch (e: Exception) {
                "Invalid date"  // Handle bad dates
            }
        } else {
            "No date"
        }

        // Dance when highlighted! Grow bigger on focus (TV remote) or tap (phone)
        holder.itemView.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // Grow dance: Scale up 1.1 times
                holder.itemView.animate().scaleX(1.1f).scaleY(1.1f).setDuration(200).start()
            } else {
                // Back to normal
                holder.itemView.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
            }
        }

        // Also grow a tiny bit on tap for phone touch
        holder.itemView.setOnClickListener {
            holder.itemView.animate().scaleX(1.05f).scaleY(1.05f).setDuration(100).withEndAction {
                holder.itemView.scaleX = 1.0f
                holder.itemView.scaleY = 1.0f
                val fullUrl = "https://rtlvod.com/videos/" + video.filePath
                val intent = Intent(context, VideoPlayerActivity::class.java)
                intent.putExtra("videoUrl", fullUrl)
                context.startActivity(intent)
            }.start()
        }
    }

    override fun getItemCount(): Int = videos.size
}