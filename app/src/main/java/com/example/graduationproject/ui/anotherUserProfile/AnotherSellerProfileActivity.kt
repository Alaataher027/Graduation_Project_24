package com.example.graduationproject.ui.anotherUserProfile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.graduationproject.R
import com.example.graduationproject.api.model.profile.Data
import com.example.graduationproject.databinding.ActivityAnotherSellerProfileBinding

class AnotherSellerProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnotherSellerProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnotherSellerProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Receive the Parcelable user data
        val userData = intent.getParcelableExtra<Data>("USER_DATA")

        // Check if userData is not null
        userData?.let { user ->
            // Set the title of the action bar to user's name
            supportActionBar?.title = user.name

            // Set user data to views
            binding.name.text = user.name ?: ""
            binding.nameSeller.text = user.name ?: ""
            binding.phoneSellertextView.text = user.phoneNumber ?: ""
            binding.governorateSeller.text = user.governorate ?: ""
            binding.citySeller.text = user.city ?: ""
            binding.streetSeller.text = user.street ?: ""
            binding.quarterSeller.text = user.residentialQuarter ?: ""
            binding.typeUser.text = user.userType ?: ""

            // Load the user's profile image if available
            user.image?.let { imageUrl ->
                Glide.with(this)
                    .load(imageUrl)
                    .into(binding.imageProfile)
            } ?: run {
                binding.imageProfile.setImageResource(R.drawable.image_profile)
            }

            // Set click listener for posts button
            binding.postsBtn.setOnClickListener {
                val intent = Intent(this, UserPostsActivity::class.java)
                intent.putExtra("USER_ID", user.id)
                startActivity(intent)
            }

            // Set click listener for history button
            binding.historyBtn.setOnClickListener {
                val intent = Intent(this, HistoryUserSellerActivity::class.java)
                intent.putExtra("USER_ID", user.id)
                startActivity(intent)
            }
        }
    }

    fun onBackPressed(view: View) {
        super.onBackPressed()
        // Optionally add additional functionality here
    }
}
