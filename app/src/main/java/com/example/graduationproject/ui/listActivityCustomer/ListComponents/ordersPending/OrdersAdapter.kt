package com.example.graduationproject.ui.listActivityCustomer.ListComponents.ordersPending

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.graduationproject.R
import com.example.graduationproject.api.model.profile.Data
import com.example.graduationproject.api.model.post.Data as PostData
import com.example.graduationproject.databinding.ItemOrderBinding
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class OrdersAdapter(
    private var profiles: List<Data>,
    private var posts: List<PostData>
) : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun getItemCount(): Int = profiles.size

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val profile = profiles[position]
        val post = posts.getOrNull(position) // Get the corresponding post data
        holder.bind(profile, post)
    }

    fun updateProfiles(newProfiles: List<Data>) {
        profiles = newProfiles
        notifyDataSetChanged()
    }

    fun updatePosts(newPosts: List<PostData>) {
        posts = newPosts
        notifyDataSetChanged()
    }

    inner class OrderViewHolder(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val nameTextView = binding.name
        private val personalImageView = binding.PersonalImage
        private val quantityTextView =
            binding.quantityNum // Assuming this is the TextView for quantity

        fun bind(profile: Data, post: PostData?) {
            binding.city.text = profile.city
            binding.gov.text = profile.governorate
            nameTextView.text = profile.name
            Glide.with(personalImageView.context)
                .load(profile.image)
                .into(personalImageView)

            // Bind post data if available
            post?.let {
                quantityTextView.text = it.quantity
                binding.priceNum.text = it.price

                Glide.with(binding.imagePost.context)
                    .load(it.image)
                    .into(binding.imagePost)

                // You can bind other post data similarly
            }

        }

    }
}
