package com.example.graduationproject.ui.anotherUserProfile

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.graduationproject.R
import com.example.graduationproject.api.model.post.postHome.DataItem
import com.example.graduationproject.api.model.profile.Data
import com.example.graduationproject.databinding.DialogPostBinding
import com.example.graduationproject.databinding.DialogPostGeneralBinding
import com.example.graduationproject.databinding.ItemPostBinding
import com.example.graduationproject.ui.login.TokenManager
import com.example.graduationproject.ui.mainActivity.fragment.home.EditPostActivity
import com.example.graduationproject.ui.mainActivity.fragment.home.HomePostViewModel
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class UserPostsAdapter(
    private val tokenManager: TokenManager,
    private val homePostViewModel: HomePostViewModel,
    private val posts: List<DataItem>,
    private val userData: Data,
    private val context: Context  // Added context parameter
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
                gov.text = userData.governorate
                city.text = userData.city

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

                var userLoginedType = tokenManager.getUserType()
                // Check if userType is "Seller"
                if (userLoginedType == "Seller" || post.userId == tokenManager.getUserId()) {
                    binding.orderBtn.isEnabled = false
                } else {
                    binding.orderBtn.isEnabled = true
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

        dialogBinding.copy.setOnClickListener {
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

        dialogBinding.copy.setOnClickListener {
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