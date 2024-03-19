package com.example.graduationproject.ui.mainActivity.fragment.home

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.graduationproject.databinding.ItemPostBinding
import com.example.graduationproject.api.model.post.postHome.DataItem
import com.example.graduationproject.api.model.profile.Data
import com.example.graduationproject.databinding.DialogPostBinding


class PostAdapter() : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    var posts: List<DataItem?> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var name: Data? = null
    private var image: Data? = null

    fun setName(userData: Data) {
        this.name = userData
    }

    fun setImage(userData: Data) {
        this.image = userData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
        holder.bindUserName(name)
        holder.bindUserImage(image)
    }

    override fun getItemCount(): Int = posts.size

    class PostViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var isExpanded = false
        private var content: String? = null

        fun bindUserName(name: Data?) {
            binding.name.text = name?.name
        }

        fun bindUserImage(image: Data?) {
            image?.image?.let { imageUrl ->
                Glide.with(binding.root.context)
                    .load(imageUrl)
                    .into(binding.PersonalImage)
            }
        }

        fun bind(post: DataItem?) {
            // Bind post data to UI elements
            binding.quantityNum.text = post?.quantity
            binding.priceNum.text = post?.price
            binding.time.text = post?.createdAt

            // Load image using Glide library
            post?.image?.let { imageUrl ->
                Glide.with(binding.root.context)
                    .load(imageUrl)
                    .into(binding.imagePost)
            }

            // Set the content
            content = post?.description
            updateContent()

            // Add a click listener to handle "See More" button
            binding.content.setOnClickListener {
                // Toggle between expanded and collapsed states
                isExpanded = !isExpanded
                updateContent()
            }

            binding.listPostBtn.setOnClickListener {
                val dialogBinding = DialogPostBinding.inflate(LayoutInflater.from(binding.root.context))
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


//class PostAdapter() : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
//
////    private val userDataMap: MutableMap<Int, Data> = mutableMapOf()
//
//    var posts: List<DataItem?> = emptyList()
//        set(value) {
//            field = value
//            notifyDataSetChanged()
//        }
//
//    private var name: Data? = null
//    private var image: Data? = null
//
//
////    fun setUserData(postId: Int, userData: Data) {
////        userDataMap[postId] = userData
////        notifyDataSetChanged()
////    }
//
//    fun setName(userData: Data) {
//        this.name = userData
//    }
//
//    fun setImage(userData: Data) {
//        this.image = userData
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
//        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return PostViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
//        holder.bind(posts[position])
//        holder.bindUserName(name)
//        holder.bindUserImage(image)
//    }
//
//    override fun getItemCount(): Int = posts.size
//
//    class PostViewHolder(private val binding: ItemPostBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        private var isExpanded = false
//        private var content: String? = null
//
//        fun bindUserName(name: Data?) {
//            binding.name.text = name?.name
//
//        }
//
//        fun bindUserImage(image: Data?) {
//            image?.image?.let { imageUrl ->
//                Glide.with(binding.root.context)
//                    .load(imageUrl)
//                    .into(binding.PersonalImage)
//            }
//        }
//
//        fun bind(post: DataItem?) {
//            // Bind post data to UI elements
//            binding.quantityNum.text = post?.quantity
//            binding.priceNum.text = post?.price
//            binding.time.text = post?.createdAt
//
//            // Load image using Glide library
//            post?.image?.let { imageUrl ->
//                Glide.with(binding.root.context)
//                    .load(imageUrl)
//                    .into(binding.imagePost)
//            }
//
//            // Set the content
//            content = post?.description
//            updateContent()
//
//            // Add a click listener to handle "See More" button
//            binding.content.setOnClickListener {
//                // Toggle between expanded and collapsed states
//                isExpanded = !isExpanded
//                updateContent()
//            }
//
//            binding.listPostBtn.setOnClickListener {
//                val dialogBinding = DialogPostBinding.inflate(layoutInflater)
//                val alertDialogBuilder = AlertDialog.Builder(requireContext())
//                    .setTitle("")
//                    .setView(dialogBinding.root)
//
//                val alertDialog = alertDialogBuilder.create()
//                alertDialog.show()
//
//                dialogBinding.save.setOnClickListener {
//                    alertDialog.dismiss()
//
//                }
//
//
//                dialogBinding.copy.setOnClickListener {
//                    alertDialog.dismiss()
//                }
//
//                dialogBinding.edit.setOnClickListener {
//                    alertDialog.dismiss()
//
//
//                }
//
//                dialogBinding.delete.setOnClickListener {
//                    alertDialog.dismiss()
//                }
//
//            }
//        }
//
//        private fun updateContent() {
//            content?.let { desc ->
//                val maxLength = if (isExpanded) Int.MAX_VALUE else MAX_CONTENT_LENGTH
//                val truncatedContent = if (desc.length > maxLength) {
//                    desc.substring(0, maxLength) + if (isExpanded) "" else " .. See More"
//                } else {
//                    desc
//                }
//                binding.content.text = truncatedContent
//            }
//        }
//
//
//        companion object {
//            private const val MAX_CONTENT_LENGTH = 50 // Define your maximum length here
//        }
//    }
//}