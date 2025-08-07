package com.example.rtlvodapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rtlvodapp.model.VideoItem
import com.example.rtlvodapp.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.app.DownloadManager
import android.net.Uri
import android.os.Environment
import com.example.rtlvodapp.model.UpdateVersion

class VideoListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchButton: Button  // The search door button
    private lateinit var logoutButton: Button  // The bye bye door
    private var fullCategories: Map<String, List<VideoItem>> = emptyMap()  // Hidden full list for searching

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_list)

        recyclerView = findViewById(R.id.categoryRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)  // Up-down for categories

        searchButton = findViewById(R.id.searchButton)  // Find the search button
        logoutButton = findViewById(R.id.logoutButton)  // Find the button

        // New: Check for updates at start
        checkForUpdates()

        // Tap search button—open a special window with search bar
        searchButton.setOnClickListener {
            val searchDialog = AlertDialog.Builder(this).create()
            val searchView = layoutInflater.inflate(R.layout.dialog_search, null)  // The small search room
            val searchInput = searchView.findViewById<EditText>(R.id.searchInput)
            searchDialog.setView(searchView)
            searchDialog.show()

            // Magic search in the window—as you type, filter
            searchInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    val query = s.toString().lowercase()
                    if (query.isEmpty()) {
                        updateShelves(fullCategories)  // Show all if no search
                    } else {
                        val filtered = fullCategories.mapValues { entry ->
                            entry.value.filter { video ->
                                video.title.lowercase().contains(query)  // Match title
                            }
                        }.filter { it.value.isNotEmpty() }  // Hide empty shelves
                        updateShelves(filtered)
                    }
                }
            })
        }

        // Tap logout—clear pocket and go to login
        logoutButton.setOnClickListener {
            val sharedPref = getSharedPreferences("RTLVodPrefs", Context.MODE_PRIVATE)
            sharedPref.edit().remove("token").apply()  // Forget the key
            Session.token = null  // Clear memory too
            Toast.makeText(this, "Logged out! 👋", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val token = Session.token ?: ""
        if (token.isEmpty()) {
            Toast.makeText(this, "Please log in first", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiClient.apiService.getVideos("Bearer $token")
                }
                if (response.isSuccessful && response.body() != null) {
                    val videoList = response.body()!!
                    if (videoList.isEmpty()) {
                        Toast.makeText(this@VideoListActivity, "No videos found 🏉", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    // Group by category, like sorting toys
                    fullCategories = videoList.groupBy { it.category }.mapValues { entry ->
                        entry.value.sortedByDescending { it.date }  // Sort newest to oldest
                    }
                    updateShelves(fullCategories)  // Show all first
                } else {
                    Toast.makeText(this@VideoListActivity, "Failed to load videos", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("VideoList", "Error: ${e.message}")
                Toast.makeText(this@VideoListActivity, "Network error—check your internet!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Helper to update shelves
    private fun updateShelves(categories: Map<String, List<VideoItem>>) {
        val adapter = CategoryAdapter(this, categories)
        recyclerView.adapter = adapter
    }

    // New: Check for updates
    private fun checkForUpdates() {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiClient.apiService.getUpdateVersion()
                }
                val currentVersion = packageManager.getPackageInfo(packageName, 0).versionName  // Your app's version, set in build.gradle
                if (response.version > currentVersion) {
                    showUpdateDialog(response.apkUrl)
                }
            } catch (e: Exception) {
                Log.e("UpdateCheck", "Error checking updates: ${e.message}")
            }
        }
    }

    // New: Show yes/no box for update
    private fun showUpdateDialog(apkUrl: String) {
        AlertDialog.Builder(this)
            .setTitle("New Update Available!")
            .setMessage("Want to download the latest version?")
            .setPositiveButton("Yes") { _, _ ->
                downloadApk(apkUrl)
            }
            .setNegativeButton("No", null)
            .show()
    }

    // New: Download the APK
    private fun downloadApk(apkUrl: String) {
        val request = DownloadManager.Request(Uri.parse(apkUrl))
            .setTitle("RTLVod Update")
            .setDescription("Downloading new version")
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "rtlvod-update.apk")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
        Toast.makeText(this, "Downloading update—check notifications!", Toast.LENGTH_SHORT).show()
    }
}