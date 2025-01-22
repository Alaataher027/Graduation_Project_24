package com.example.graduationproject.ui.mainActivity.fragment.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Handler
import android.os.Looper
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
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.graduationproject.R
import com.example.graduationproject.databinding.DialogConfirmOrderBinding
import com.example.graduationproject.databinding.DialogPostGeneralBinding
import com.example.graduationproject.ui.anotherUserProfile.AnotherCustomerProfileActivity
import com.example.graduationproject.ui.anotherUserProfile.AnotherSellerProfileActivity
import com.example.graduationproject.ui.login.TokenManager
import java.text.SimpleDateFormat
import java.util.*

class PostAdapter(
    private val tokenManager: TokenManager,
    private val homePostViewModel: HomePostViewModel
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
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


            post?.let { postData ->

                binding.name.setOnClickListener {
                    openUserProfile(userData)
                }

                binding.PersonalImage.setOnClickListener {
                    openUserProfile(userData)
                }
            }

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
                binding.gov.text = it.governorate
                binding.city.text = it.city
                binding.name.text = it.name
                it.image?.let { imageUrl ->
                    Glide.with(binding.root.context)
                        .load(imageUrl)
                        .into(binding.PersonalImage)
                }

                var userLoginedType = tokenManager.getUserType()
                // Check if userType is "Seller"
                if (userLoginedType == "Seller") {
                    binding.orderBtn.isEnabled = false
                } else {
                    binding.orderBtn.isEnabled = true
                }


            }

            // Add a click listener to handle "See More" button
            binding.content.setOnClickListener {
                // Toggle between expanded and collapsed states
                isExpanded = !isExpanded
                updateContent()
            }

            val id: Int = tokenManager.getUserId()

            if (post?.userId == id) {
                binding.orderBtn.isEnabled = false

            }

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

            // Set the background of order button
            binding.orderBtn.setBackgroundResource(R.drawable.order_post_btn)

            // Add a click listener to handle "Order" button
            binding.orderBtn.setOnClickListener {
                showConfirmOrderDialog(post)
            }
        }


        //  function to open profile user
        private fun openUserProfile(userData: Data?) {
            userData?.let { user ->
                val intent = if (user.userType == "Seller") {
                    Intent(binding.root.context, AnotherSellerProfileActivity::class.java)
                } else {
                    Intent(binding.root.context, AnotherCustomerProfileActivity::class.java)
                }
                intent.putExtra("USER_DATA", user)
                binding.root.context.startActivity(intent)
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
                binding.orderBtn.setBackgroundResource(R.drawable.rec_gray_pending)
                binding.orderBtn.text = "waiting for a reply"
                binding.iconOrder.visibility = View.INVISIBLE
                val drawable = ContextCompat.getDrawable(binding.root.context, R.drawable.wait_ic)
                binding.orderBtn.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)

                // Handle the order confirmation
                val postId = post?.id ?: return@setOnClickListener
                val accessToken = tokenManager.getToken() ?: return@setOnClickListener
                val buyerId = tokenManager.getUserId()
                homePostViewModel.orderPost(
                    accessToken,
                    postId.toString(),
                    buyerId.toString()
                ) { message ->
                    // Ensure the toast is shown on the main thread
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(binding.root.context, message, Toast.LENGTH_SHORT).show()
                    }
                }
                alertDialog.dismiss()
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

//            dialogBinding.copy.setOnClickListener {
//                alertDialog.dismiss()
//            }

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

//            dialogBinding.copy.setOnClickListener {
//                alertDialog.dismiss()
//            }
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