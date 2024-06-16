package com.example.graduationproject.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.graduationproject.R
import com.example.graduationproject.api.model.profile.Data

class AnotherCustomerProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_another_customer_profile)

        // Receive the Parcelable user data
        val userData = intent.getParcelableExtra<Data>("USER_DATA")

        // Check if userData is not null
        userData?.let { user ->
            // Set the title of the action bar to user's name
            supportActionBar?.title = user.name

            // Set other views with user's data
            findViewById<TextView>(R.id.name_seller).text = user.name ?: ""
            findViewById<TextView>(R.id.phone_sellertextView).text = user.phoneNumber ?: ""
            findViewById<TextView>(R.id.governorate_seller).text = user.governorate ?: ""
            findViewById<TextView>(R.id.city_seller).text = user.city ?: ""
            findViewById<TextView>(R.id.tax_customer).text = user.tIN ?: ""

            // Optionally, you can load the user's profile image if available
            // Example assuming you have an ImageView with id "image_profile" in your layout
            // Glide.with(this).load(user.image).into(findViewById(R.id.image_profile))
        }
    }
}