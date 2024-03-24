package com.example.graduationproject.ui.mainActivity.fragment.home

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.graduationproject.databinding.ItemPostBinding
import com.example.graduationproject.api.model.post.postHome.DataItem
import com.example.graduationproject.api.model.profile.Data
import com.example.graduationproject.databinding.DialogPostBinding
import android.text.format.DateUtils
import android.util.Log
import com.example.graduationproject.R
import com.example.graduationproject.databinding.DialogPostGeneralBinding
import com.example.graduationproject.ui.login.TokenManager
import java.text.SimpleDateFormat
import java.util.*


class PostAdapter(private val tokenManager: TokenManager) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    var posts: List<DataItem?> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val userDataMap = mutableMapOf<Int, Data>()
    fun addUserData(userId: Int, userData: Data) {
        userDataMap[userId] = userData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position], userDataMap[posts[position]?.userId])

    }

    override fun getItemCount(): Int = posts.size

    inner class PostViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var isExpanded = false
        private var content: String? = null

        fun bind(post: DataItem?, userData: Data?) {
            // Bind post data to UI elements
            binding.quantityNum.text = post?.quantity
            binding.priceNum.text = post?.price
            binding.time.text = getTimeAgo(post?.createdAt)


            // Load image using Glide library
            post?.image?.let { imageUrl ->
                Glide.with(binding.root.context)
                    .load(imageUrl)
                    .into(binding.imagePost)
            }

            // Set the content
            content = post?.description
            updateContent()

            // Bind user data
            userData?.let {
                binding.name.text = it.name
                it.image?.let { imageUrl ->
                    Glide.with(binding.root.context)
                        .load(imageUrl)
                        .into(binding.PersonalImage)
                }
            }

            // Add a click listener to handle "See More" button
            binding.content.setOnClickListener {
                // Toggle between expanded and collapsed states
                isExpanded = !isExpanded
                updateContent()
            }

            val id: Int = tokenManager.getUserId()
            binding.listPostBtn.setOnClickListener {
                // Check if the user id from HomePostResponse is equals user id from ProfileResponse
                if (post?.userId == id) {
                    Log.d("PostAdapter", "post user id : ${post?.userId}, user data id: $id")
                    // Navigate to dialog_post
                    navigateToDialogPost()
                } else {
                    // Navigate to dialog_post_general
                    Log.d("PostAdapter", "post user id : ${post?.userId}, user data id: $id")
                    navigateToDialogPostGeneral()
                }
            }


            binding.orderBtn.setBackgroundResource(R.drawable.rectangle_btn_post)
            binding.orderBtn.backgroundTintList = null

            binding.chatBtn.setBackgroundResource(R.drawable.rectangle_btn_post)
            binding.chatBtn.backgroundTintList = null

        }

        private fun navigateToDialogPost() {
            val dialogBinding =
                DialogPostBinding.inflate(LayoutInflater.from(binding.root.context))
            val alertDialogBuilder = AlertDialog.Builder(binding.root.context)
                .setTitle("")
                .setView(dialogBinding.root)

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()

            dialogBinding.save.setOnClickListener {
                alertDialog.dismiss()
            }

            dialogBinding.copy.setOnClickListener {
                alertDialog.dismiss()
            }

            dialogBinding.edit.setOnClickListener {
                alertDialog.dismiss()
            }

            dialogBinding.delete.setOnClickListener {
                alertDialog.dismiss()
            }

        }

        private fun navigateToDialogPostGeneral() {
            val dialogBinding =
                DialogPostGeneralBinding.inflate(LayoutInflater.from(binding.root.context))
            val alertDialogBuilder = AlertDialog.Builder(binding.root.context)
                .setTitle("")
                .setView(dialogBinding.root)

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()

            dialogBinding.save.setOnClickListener {
                alertDialog.dismiss()
            }

            dialogBinding.copy.setOnClickListener {
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

        private fun getTimeAgo(timestamp: String?): CharSequence {
            if (timestamp.isNullOrEmpty()) return ""

            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")

            val time = sdf.parse(timestamp)?.time ?: return ""

            return DateUtils.getRelativeTimeSpanString(
                time,
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS
            )
        }


    }

    companion object {
        private const val MAX_CONTENT_LENGTH = 50 // Define your maximum length here
    }
}
