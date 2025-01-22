package com.example.graduationproject

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.graduationproject.api.model.post.savePost.DataItem
import com.example.graduationproject.api.model.profile.Data
import com.example.graduationproject.databinding.ItemSaveBinding
import com.example.graduationproject.ui.anotherUserProfile.AnotherCustomerProfileActivity
import com.example.graduationproject.ui.anotherUserProfile.AnotherSellerProfileActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone


class SavedPostsAdapter(private val onItemClick: (DataItem?) -> Unit) :
    RecyclerView.Adapter<SavedPostsAdapter.PostViewHolder>() {

    var savedPosts: List<DataItem?> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val userDataMap = mutableMapOf<Int, Data>()
    fun addUserData(userId: Int, userData: Data) {
        userDataMap[userId] = userData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemSaveBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(savedPosts[position], userDataMap[savedPosts[position]?.userId])
        holder.itemView.setOnClickListener {
            onItemClick(savedPosts[position])
        }
    }

    override fun getItemCount(): Int = savedPosts.size

    inner class PostViewHolder(private val binding: ItemSaveBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(post: DataItem?, userData: Data?) {


            post?.let { postData ->

                binding.nameUser.setOnClickListener {
                    openUserProfile(userData)
                }

                binding.imageProfile.setOnClickListener {
                    openUserProfile(userData)
                }
            }


            // Bind post data to UI elements
            binding.type.text = post?.material
            binding.time.text = getHoursAndMinutesWithAmPm(post?.createdAt)
            binding.date.text = getFormattedDate(post?.createdAt)

            // Load image using Glide library
            post?.image?.let { imageUrl ->
                Glide.with(binding.root.context)
                    .load(imageUrl)
                    .into(binding.imageProduct)
            }

            // Bind user data
            userData?.let {
                binding.nameUser.text = it.name
                it.image?.let { imageUrl ->
                    Glide.with(binding.root.context)
                        .load(imageUrl)
                        .into(binding.imageProfile)
                }
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


        private fun getHoursAndMinutesWithAmPm(timestamp: String?): String {
            if (timestamp.isNullOrEmpty()) return ""

            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")

            val time = sdf.parse(timestamp)?.time ?: return ""

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = time

            val hours = calendar.get(Calendar.HOUR)
            val minutes = calendar.get(Calendar.MINUTE)
            val amPm = calendar.get(Calendar.AM_PM)

            val amPmString = if (amPm == Calendar.AM) "AM" else "PM"

            return String.format("%02d:%02d %s", hours, minutes, amPmString)
        }

        private fun getFormattedDate(timestamp: String?): String {
            if (timestamp.isNullOrEmpty()) return ""

            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")

            val time = sdf.parse(timestamp)?.time ?: return ""

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = time

            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH) + 1 // الشهر يبدأ من 0، لذلك نضيف 1
            val year = calendar.get(Calendar.YEAR)

            return "$dayOfMonth/$month/$year"
        }
    }
}