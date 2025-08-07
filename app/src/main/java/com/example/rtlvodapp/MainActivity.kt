package com.example.rtlvodapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.rtlvodapp.model.LoginRequest
import com.example.rtlvodapp.network.ApiClient
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        usernameInput = findViewById(R.id.usernameInput)
        passwordInput = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            if (username.isNotEmpty() && password.isNotEmpty()) {
                loginUser(username, password)
            } else {
                Toast.makeText(this, "Enter both!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(username: String, password: String) {
        lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.login(LoginRequest(username, password))
                if (response.token != null) {  // Fixed: Check if token exists = success! No more "success"
                    Session.token = response.token
                    Toast.makeText(this@MainActivity, "Logged in! 🎉", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, VideoListActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val msg = response.message ?: "Login failed"
                    Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}