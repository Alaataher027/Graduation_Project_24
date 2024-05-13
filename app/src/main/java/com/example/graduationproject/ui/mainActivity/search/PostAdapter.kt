package com.example.graduationproject.ui.mainActivity.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.graduationproject.R
import com.example.graduationproject.api.model.profile.Data
import com.example.graduationproject.api.model.search.DataItem
import com.example.graduationproject.databinding.ItemPostBinding

class PostAdapter(private var posts: List<DataItem?>?) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private val userDataMap = mutableMapOf<Int, Data>()
    fun addUserData(userId: Int, userData: Data) {
        userDataMap[userId] = userData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        posts?.get(position)?.let { holder.bind(it, userDataMap[posts!![position]?.userId]) }
    }

    override fun getItemCount(): Int {
        return posts?.size ?: 0
    }

    fun updatePosts(newPosts: List<DataItem?>) {
        posts = newPosts
        notifyDataSetChanged()
    }

    inner class PostViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isExpanded = false
        private var content: String? = null


        fun bind(post: DataItem, userData: Data?) {
            // Update views with post data
            binding.apply {
                // Set data to views
                post.image?.let { imageUrl ->
                    // Load image using Glide
                    Glide.with(root.context)
                        .load(imageUrl)
                        .into(imagePost)
                }
                quantityNum.text = post.quantity ?: ""
                priceNum.text = post.price ?: ""
                content.text = post.description ?: ""

                userData?.let {
                    gov.text = it.governorate
                    city.text = it.city
                    name.text = it.name
                    it.image?.let { imageUrl ->
                        Glide.with(root.context)
                            .load(imageUrl)
                            .into(PersonalImage)
                    }
                }

                binding.content.setOnClickListener {
                    // Toggle between expanded and collapsed states
                    isExpanded = !isExpanded
                    updateContent()
                }
            }

            binding.orderBtn.setBackgroundResource(R.drawable.order_post_btn)

        }

        private fun updateContent() {
            content?.let { desc ->
                val maxLength = if (isExpanded) Int.MAX_VALUE else MAX_CONTENT_LENGTH
                val truncatedContent = if (desc.length > maxLength) {
                    desc.substring(0, maxLength) + if (isExpanded) "" else " .. See More"
                } else {
                    desc
                }
                binding.content.text = truncatedContent
            }
        }

    }

    companion object {
        private const val MAX_CONTENT_LENGTH = 50 // Define your maximum length here
    }
}