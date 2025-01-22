package com.example.graduationproject.ui.mainActivity.search

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.graduationproject.R
import com.example.graduationproject.api.model.profile.Data
import com.example.graduationproject.api.model.search.DataItem
import com.example.graduationproject.databinding.DialogConfirmOrderBinding
import com.example.graduationproject.databinding.ItemPostBinding
import com.example.graduationproject.ui.login.TokenManager

class PostAdapter(
    private var posts: List<DataItem?>?,
    private val tokenManager: TokenManager,
    private val homePostViewModel: SearchViewModel
) :
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

            binding.orderBtn.setOnClickListener {
                showConfirmOrderDialog(post)
            }
        }

        private fun showConfirmOrderDialog(post: DataItem?) {
            val dialogBinding =
                DialogConfirmOrderBinding.inflate(LayoutInflater.from(binding.root.context))
            val alertDialogBuilder = AlertDialog.Builder(binding.root.context)
                .setView(dialogBinding.root)

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()

            dialogBinding.noBtn.setOnClickListener {
                alertDialog.dismiss()
            }

            dialogBinding.yesBtn.setOnClickListener {
                binding.orderBtn.setBackgroundResource(R.drawable.rec_trans)
                // Handle the order confirmation

                val postId = post?.id ?: return@setOnClickListener
                val accessToken = tokenManager.getToken() ?: return@setOnClickListener
                val buyerId = tokenManager.getUserId()
                homePostViewModel.orderPost(
                    accessToken,
                    postId.toString(),
                    buyerId.toString()
                ) { message ->
                    Toast.makeText(binding.root.context, message, Toast.LENGTH_SHORT).show()
                }
                alertDialog.dismiss()
            }
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