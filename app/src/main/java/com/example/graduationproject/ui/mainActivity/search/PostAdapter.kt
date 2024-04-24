package com.example.graduationproject.ui.mainActivity.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.graduationproject.R
import com.example.graduationproject.api.model.search.DataItem

class PostAdapter(private var posts: List<DataItem?>?) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts?.get(position)
        post?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int {
        return posts?.size ?: 0
    }

    fun updatePosts(newPosts: List<DataItem?>) {
        posts = newPosts
        notifyDataSetChanged()
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Bind views here and update them with data
        fun bind(post: DataItem) {
            // Update views with post data
            itemView.apply {
                // Find views by their IDs
                val imageView = findViewById<ImageView>(R.id.image_post)
                val quantityTextView = findViewById<TextView>(R.id.quantity_num)
                val priceTextView = findViewById<TextView>(R.id.price_num)
                val descriptionTextView = findViewById<TextView>(R.id.content)

                // Set data to views
                post.image?.let { imageUrl ->
                    // Load image using any image loading library like Picasso, Glide, Coil, etc.
                    // Example with Glide:
                    Glide.with(context)
                        .load(imageUrl)
                        .into(imageView)
                }
                quantityTextView.text = post.quantity ?: ""
                priceTextView.text = post.price ?: ""
                descriptionTextView.text = post.description ?: ""
            }
        }
    }
}
