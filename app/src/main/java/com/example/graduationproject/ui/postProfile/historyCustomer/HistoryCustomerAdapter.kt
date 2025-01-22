package com.example.graduationproject.ui.postProfile.historyCustomer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.graduationproject.api.model.post.Data
import com.example.graduationproject.databinding.ItemHistoryBinding
import com.example.graduationproject.databinding.ItemOrderBinding
import com.example.graduationproject.ui.postProfile.historySeller.HistorySellerAdapter

class HistoryCustomerAdapter (
    private var posts: List<Data>,
    private var profiles: List<com.example.graduationproject.api.model.profile.Data>
) : RecyclerView.Adapter<HistoryCustomerAdapter.HistoryViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryCustomerAdapter.HistoryViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryCustomerAdapter.HistoryViewHolder, position: Int) {
        val profile = profiles[position]
        val post = posts.getOrNull(position) // Get the corresponding post data
        holder.bind(post, profile)
    }

    override fun getItemCount(): Int = profiles.size


    fun updatePosts(newPosts: List<Data>) {
        posts = newPosts
        notifyDataSetChanged()
    }

    fun updateProfiles(newProfiles: List<com.example.graduationproject.api.model.profile.Data>) {
        profiles = newProfiles
        notifyDataSetChanged()
    }

    inner class HistoryViewHolder(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Data?, profile: com.example.graduationproject.api.model.profile.Data?) {

            // Bind post data if available
            post?.let {
                binding.quantityNum.text = it.quantity
                binding.priceNum.text = it.price

                Glide.with(binding.imagePost.context)
                    .load(it.image)
                    .into(binding.imagePost)
            }

            // Bind profile data if available
            profile?.let {
                binding.city.text = it.city
                binding.gov.text = it.governorate
                binding.name.text = it.name
                Glide.with(binding.PersonalImage.context)
                    .load(it.image)
                    .into(binding.PersonalImage)
            }
        }

    }

}