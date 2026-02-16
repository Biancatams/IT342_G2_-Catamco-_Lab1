package com.example.mobileapplication.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mobileapplication.R
import com.example.mobileapplication.api.RetrofitClient
import com.example.mobileapplication.model.UserResponse
import com.example.mobileapplication.utils.TokenManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class ProfileActivity : AppCompatActivity() {

    private lateinit var tokenManager: TokenManager
    private lateinit var profileAvatar: TextView
    private lateinit var profileName: TextView
    private lateinit var profileUsername: TextView
    private lateinit var fullNameValue: TextView
    private lateinit var usernameValue: TextView
    private lateinit var emailValue: TextView
    private lateinit var memberSinceValue: TextView
    private lateinit var dashboardLink: TextView
    private lateinit var profileLink: TextView
    private lateinit var logoutButton: Button
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tokenManager = TokenManager(this)

        if (tokenManager.getToken() == null) {
            navigateToLogin()
            return
        }

        setContentView(R.layout.activity_profile)

        profileAvatar = findViewById(R.id.profileAvatar)
        profileName = findViewById(R.id.profileName)
        profileUsername = findViewById(R.id.profileUsername)
        fullNameValue = findViewById(R.id.fullNameValue)
        usernameValue = findViewById(R.id.usernameValue)
        emailValue = findViewById(R.id.emailValue)
        memberSinceValue = findViewById(R.id.memberSinceValue)
        dashboardLink = findViewById(R.id.dashboardLink)
        profileLink = findViewById(R.id.profileLink)
        logoutButton = findViewById(R.id.logoutButton)
        backButton = findViewById(R.id.backButton)

        dashboardLink.setOnClickListener {
            finish()
        }

        backButton.setOnClickListener {
            finish()
        }

        logoutButton.setOnClickListener {
            tokenManager.clearToken()
            navigateToLogin()
        }

        loadUserProfile()
    }

    private fun loadUserProfile() {
        lifecycleScope.launch {
            try {
                val token = tokenManager.getToken()!!
                val response = RetrofitClient.apiService.getCurrentUser("Bearer $token")

                if (response.isSuccessful && response.body() != null) {
                    displayUserData(response.body()!!)
                } else {
                    tokenManager.clearToken()
                    navigateToLogin()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ProfileActivity, "Failed to load profile", Toast.LENGTH_SHORT).show()
                tokenManager.clearToken()
                navigateToLogin()
            }
        }
    }

    private fun displayUserData(user: UserResponse) {
        val initials = "${user.firstName.first()}${user.lastName.first()}".uppercase()
        profileAvatar.text = initials
        profileName.text = "${user.firstName} ${user.lastName}"
        profileUsername.text = "@${user.username}"
        fullNameValue.text = "${user.firstName} ${user.lastName}"
        usernameValue.text = "@${user.username}"
        emailValue.text = user.emailAddress
        memberSinceValue.text = formatDate(user.createdAt)
    }

    private fun formatDate(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
            val outputFormat = SimpleDateFormat("MMMM d, yyyy", Locale.US)
            val date = inputFormat.parse(dateString)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            dateString
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}