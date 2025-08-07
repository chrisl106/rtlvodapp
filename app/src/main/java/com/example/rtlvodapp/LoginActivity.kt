package com.example.rtlvodapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rtlvodapp.model.LoginRequest
import com.example.rtlvodapp.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // New: Check the hidden pocket first—if key is there, go to videos!
        val sharedPref = getSharedPreferences("RTLVodPrefs", Context.MODE_PRIVATE)
        val savedToken = sharedPref.getString("token", null)
        if (savedToken != null) {
            Session.token = savedToken
            val intent = Intent(this, VideoListActivity::class.java)
            startActivity(intent)
            finish()  // Close login so it doesn't show
            return
        }

        setContentView(R.layout.activity_main) // Uses activity_main.xml

        val usernameField = findViewById<EditText>(R.id.usernameInput)
        val passwordField = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter username and password!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = ApiClient.apiService.login(LoginRequest(username, password)) // Uses ApiClient
                    withContext(Dispatchers.Main) {
                        if (response.token != null) {  // Check if token exists = success!
                            Session.token = response.token
                            // New: Save key in hidden pocket!
                            sharedPref.edit().putString("token", response.token).apply()
                            Toast.makeText(this@LoginActivity, "Logged in! 🎉", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@LoginActivity, VideoListActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            val msg = response.message ?: "Login failed—wrong username or password?"
                            Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    Log.e("LoginError", "Exception: ${e.message}", e)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@LoginActivity, "Network error: Check your WiFi or try later!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}