package com.example.graduationproject.ui.postProfile

import android.app.AlertDialog
import android.content.Intent
import com.example.graduationproject.ui.mainActivity.fragment.home.HomePostViewModel
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.graduationproject.databinding.ItemPostBinding
import com.example.graduationproject.api.model.post.postHome.DataItem
import com.example.graduationproject.api.model.profile.Data
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import com.example.graduationproject.R
import com.example.graduationproject.databinding.DialogPostBinding
import com.example.graduationproject.databinding.DialogPostGeneralBinding
import com.example.graduationproject.ui.login.TokenManager
import com.example.graduationproject.ui.mainActivity.fragment.home.EditPostActivity
import java.text.SimpleDateFormat
import java.util.*

class PostsProfileAdapter(
    private val tokenManager: TokenManager,
    private val homePostViewModel: HomePostViewModel
) : RecyclerView.Adapter<PostsProfileAdapter.PostViewHolder>() {

    var posts: List<DataItem?> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val userDataMap = mutableMapOf<Int, Data>()

    // Function to add user data to the map
    fun addUserData(userId: Int, userData: Data) {
        userDataMap[userId] = userData
    }

    // Function to clear adapter data
    fun clearData() {
        posts = emptyList()
        userDataMap.clear()
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
            // ...

            binding.orderBtn.visibility = View.INVISIBLE
//            binding.cardPost.height

            val currentUserId = tokenManager.getUserId()
            val postUserId =
                post?.userId ?: -1 // Using -1 as a default value for clarity, adjust as needed
            if (currentUserId == postUserId) {
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
                    val postId = post?.id ?: return@setOnClickListener
                    val accessToken = tokenManager.getToken() ?: return@setOnClickListener
                    if (post?.userId == id) {
                        Log.d("PostAdapter", "post user id : ${post?.userId}, user data id: $id")
                        // Navigate to dialog_post
                        navigateToDialogPost(post)
                    } else {
                        // Navigate to dialog_post_general
                        Log.d("PostAdapter", "post user id : ${post?.userId}, user data id: $id")
                        navigateToDialogPostGeneral(post)
                    }
                }


                binding.orderBtn.setBackgroundResource(R.drawable.rectangle_btn_post)
                binding.orderBtn.backgroundTintList = null

//                binding.chatBtn.setBackgroundResource(R.drawable.rectangle_btn_post)
//                binding.chatBtn.backgroundTintList = null


            } else {
                // Hide or handle the case where the post doesn't belong to the current user
            }
        }


        private fun navigateToDialogPost(post: DataItem?) {
            val dialogBinding =
                DialogPostBinding.inflate(LayoutInflater.from(binding.root.context))
            val alertDialogBuilder = AlertDialog.Builder(binding.root.context)
                .setTitle("")
                .setView(dialogBinding.root)

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()

            dialogBinding.save.setOnClickListener {
                val postId = post?.id ?: return@setOnClickListener
                val accessToken = tokenManager.getToken() ?: return@setOnClickListener
                homePostViewModel.savePost(accessToken, postId.toString())
                alertDialog.dismiss()
            }

            dialogBinding.copy.setOnClickListener {
                alertDialog.dismiss()
            }

            dialogBinding.edit.setOnClickListener {
                tokenManager.savePostId(post?.id ?: 0)
                val intent = Intent(binding.root.context, EditPostActivity::class.java)
                intent.putExtra("POST_DATA", post)
                binding.root.context.startActivity(intent)
                alertDialog.dismiss()
            }

            dialogBinding.delete.setOnClickListener {
                val id = post?.id ?: return@setOnClickListener
                val accessToken = tokenManager.getToken() ?: return@setOnClickListener
                homePostViewModel.deletePost(accessToken, id)
                alertDialog.dismiss()
            }

        }

        private fun navigateToDialogPostGeneral(post: DataItem?) {
            val dialogBinding =
                DialogPostGeneralBinding.inflate(LayoutInflater.from(binding.root.context))
            val alertDialogBuilder = AlertDialog.Builder(binding.root.context)
                .setTitle("")
                .setView(dialogBinding.root)

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()

            dialogBinding.save.setOnClickListener {
                val postId = post?.id ?: return@setOnClickListener
                val accessToken = tokenManager.getToken() ?: return@setOnClickListener
                homePostViewModel.savePost(accessToken, postId.toString())
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