package com.example.graduationproject.ui.postProfile.historySeller

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.graduationproject.api.model.post.Data
import com.example.graduationproject.api.model.profile.Data as ProfileData
import com.example.graduationproject.databinding.ItemHistoryBinding

class HistorySellerAdapter(
    private var posts: List<Data>,
    private var profiles: List<ProfileData>
) : RecyclerView.Adapter<HistorySellerAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val profile = profiles[position]
        val post = posts.getOrNull(position) // Get the corresponding post data
        holder.bind(post, profile)
    }

    override fun getItemCount(): Int = profiles.size

//    fun updateHistories(newHistories: List<DataItemS>) {
//        histories = newHistories
//        notifyDataSetChanged()
//    }

    fun updatePosts(newPosts: List<Data>) {
        posts = newPosts
        notifyDataSetChanged()
    }

    fun updateProfiles(newProfiles: List<ProfileData>) {
        profiles = newProfiles
        notifyDataSetChanged()
    }

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Data?, profile: ProfileData?) {
            // Log the data
//            Log.d("HistoryViewHolder", "Binding history: $history, post: $post, profile: $profile")

            // Bind post data if available
            post?.let {
                binding.quantityNum.text = it.quantity
                binding.priceNum.text = it.price
                binding.content.text = it.description

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
