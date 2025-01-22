package com.example.graduationproject.ui.anotherUserProfile

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.graduationproject.R
import com.example.graduationproject.api.model.post.postHome.DataItem
import com.example.graduationproject.api.model.profile.Data
import com.example.graduationproject.databinding.DialogConfirmOrderBinding
import com.example.graduationproject.databinding.DialogPostBinding
import com.example.graduationproject.databinding.DialogPostGeneralBinding
import com.example.graduationproject.databinding.ItemPostBinding
import com.example.graduationproject.ui.login.TokenManager
import com.example.graduationproject.ui.mainActivity.fragment.home.EditPostActivity
import com.example.graduationproject.ui.mainActivity.fragment.home.HomePostViewModel
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*

class UserPostsAdapter(
    private val tokenManager: TokenManager,
    private val homePostViewModel: HomePostViewModel,
    private val posts: List<DataItem>,
    private val userData: Data,
    private val context: Context
) : RecyclerView.Adapter<UserPostsAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    inner class PostViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val profileImage: CircleImageView = binding.PersonalImage
        private val name: TextView = binding.name

        fun bind(post: DataItem) {
            binding.apply {
                binding.orderBtn.setBackgroundResource(R.drawable.order_post_btn)

                // Load user profile image
                Glide.with(itemView.context)
                    .load(userData.image)
                    .into(profileImage)

                // Set user name
                name.text = userData.name

                // Display post image
                Glide.with(itemView.context)
                    .load(post.image)
                    .into(imagePost)

                // Set post details
                time.text = getTimeAgo(post.createdAt)
                quantityNum.text = post.quantity
                priceNum.text = post.price
                content.text = post.description

                // Handle list post button click
                listPostBtn.setOnClickListener {
                    val id: Int = tokenManager.getUserId()
                    if (post.userId == id) {
                        navigateToDialogPost(post)
                    } else {
                        navigateToDialogPostGeneral(post)
                    }
                }

                // Handle order button click
                orderBtn.setOnClickListener {
                    showConfirmOrderDialog(post)
                }

                // Disable order button conditionally
                val userLoginedType = tokenManager.getUserType()
                if (userLoginedType == "Seller" || post.userId == tokenManager.getUserId()) {
                    orderBtn.isEnabled = false
                } else {
                    orderBtn.isEnabled = true
                }
            }
        }
    }

    private fun navigateToDialogPost(post: DataItem?) {
        val dialogBinding = DialogPostBinding.inflate(LayoutInflater.from(context))
        val alertDialogBuilder = AlertDialog.Builder(context)
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

        dialogBinding.edit.setOnClickListener {
            tokenManager.savePostId(post?.id ?: 0)
            val intent = Intent(context, EditPostActivity::class.java)
            intent.putExtra("POST_DATA", post)
            context.startActivity(intent)
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
        val dialogBinding = DialogPostGeneralBinding.inflate(LayoutInflater.from(context))
        val alertDialogBuilder = AlertDialog.Builder(context)
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
    }

    private fun showConfirmOrderDialog(post: DataItem?) {
        val dialogBinding =
            DialogConfirmOrderBinding.inflate(LayoutInflater.from(context))
        val alertDialogBuilder = AlertDialog.Builder(context)
            .setView(dialogBinding.root)

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

        dialogBinding.noBtn.setOnClickListener {
            alertDialog.dismiss()
        }

        dialogBinding.yesBtn.setOnClickListener {
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
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
            alertDialog.dismiss()
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
