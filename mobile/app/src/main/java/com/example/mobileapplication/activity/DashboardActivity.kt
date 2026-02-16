package com.example.mobileapplication.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileapplication.R
import com.example.mobileapplication.utils.TokenManager

class DashboardActivity : AppCompatActivity() {

    private lateinit var tokenManager: TokenManager
    private lateinit var dashboardLink: TextView
    private lateinit var profileLink: TextView
    private lateinit var logoutButton: Button
    private lateinit var viewProfileButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tokenManager = TokenManager(this)

        if (tokenManager.getToken() == null) {
            navigateToLogin()
            return
        }

        setContentView(R.layout.activity_dashboard)

        dashboardLink = findViewById(R.id.dashboardLink)
        profileLink = findViewById(R.id.profileLink)
        logoutButton = findViewById(R.id.logoutButton)
        viewProfileButton = findViewById(R.id.viewProfileButton)

        profileLink.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        viewProfileButton.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        logoutButton.setOnClickListener {
            tokenManager.clearToken()
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}