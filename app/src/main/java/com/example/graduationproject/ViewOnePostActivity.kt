package com.example.graduationproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.AlertDialog
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.graduationproject.api.model.post.Data
import com.example.graduationproject.databinding.ActivityViewOnePostBinding
import com.example.graduationproject.databinding.DialogConfirmOrderBinding
import com.example.graduationproject.ui.login.TokenManager
import com.example.graduationproject.ui.mainActivity.fragment.home.HomePostViewModel
import com.example.graduationproject.ui.mainActivity.fragment.home.UserDataHomeViewModel
import com.example.graduationproject.ui.anotherUserProfile.HomePostViewModelFactory

class ViewOnePostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewOnePostBinding
    private lateinit var tokenManager: TokenManager
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var userDataViewModel: UserDataHomeViewModel
    private lateinit var viewModel: SavedPostsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewOnePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tokenManager = TokenManager(this)
        swipeRefreshLayout = binding.swipeRefreshLayout // Initialize swipeRefreshLayout
        userDataViewModel = ViewModelProvider(this).get(UserDataHomeViewModel::class.java)
        viewModel = ViewModelProvider(this, SavedPostsViewModelFactory(tokenManager)).get(
            SavedPostsViewModel::class.java
        )

        val postId = intent.getIntExtra("POST_ID", -1)
        if (postId != -1) {
            fetchPostData(postId)
        }

        binding.buttonBack.setOnClickListener {
            // Handle the click event, for example, navigate back one step
            onBackPressed()
        }

        binding.orderBtn.setBackgroundResource(R.drawable.order_post_btn)

        val userLoginedType = tokenManager.getUserType()
        if (userLoginedType == "Seller") {
            binding.orderBtn.isEnabled = false
        } else {
            binding.orderBtn.isEnabled = true
        }

        swipeRefreshLayout.setOnRefreshListener {
            fetchPostData(postId)
        }

        binding.orderBtn.setOnClickListener {
            val post =
                viewModel.postData.value // Assuming the post data is stored in postData LiveData
            post?.let { showConfirmOrderDialog(it) }
        }
    }

    private fun fetchPostData(postId: Int) {
        val accessToken = tokenManager.getToken() ?: ""

        viewModel.getPost(accessToken, postId)
        viewModel.postData.observe(this) { post ->
            post?.let {
                binding.poster.text = it.material
                binding.quantityNum.text = it.quantity
                binding.priceNum.text = it.price
                binding.content.text = it.description

                // Check if the activity is still valid before loading the image
                if (!isDestroyed && !isFinishing) {
                    Glide.with(this)
                        .load(it.image)
                        .into(binding.imagePost)
                }

                // Fetch user data
                it.userId?.let { userId ->
                    userDataViewModel.getData(accessToken, userId,
                        onDataLoaded = { userData ->
                            // Bind user data to UI elements
                            userData?.let { user ->
                                binding.name.text = user.name
                                binding.gov.text = user.governorate
                                binding.city.text = user.city

                                // Check if the activity is still valid before loading the image
                                if (!isDestroyed && !isFinishing) {
                                    Glide.with(this)
                                        .load(user.image)
                                        .into(binding.PersonalImage)
                                }
                            }
                        },
                        onError = { error ->
                            // Handle error fetching user data
                        }
                    )
                }

                if (tokenManager.getUserId() == it.userId) {
                    binding.orderBtn.isEnabled = false
                } else {
                    binding.orderBtn.isEnabled = true
                }
            }
        }

        swipeRefreshLayout.isRefreshing = false
    }


    private fun showConfirmOrderDialog(post: Data) {
        val dialogBinding = DialogConfirmOrderBinding.inflate(LayoutInflater.from(this))
        val alertDialogBuilder = AlertDialog.Builder(this)
            .setView(dialogBinding.root)

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

        dialogBinding.noBtn.setOnClickListener {
            alertDialog.dismiss()
        }

        dialogBinding.yesBtn.setOnClickListener {
            binding.orderBtn.setBackgroundResource(R.drawable.rec_gray_pending)
            binding.orderBtn.text = "waiting for a reply:"
            binding.iconOrder.visibility = View.INVISIBLE
            val drawable = ContextCompat.getDrawable(this, R.drawable.wait_ic)
            binding.orderBtn.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)

            // Handle the order confirmation
            val postId = post.id
            val accessToken = tokenManager.getToken() ?: return@setOnClickListener
            val buyerId = tokenManager.getUserId()
            val homePostViewModel =
                ViewModelProvider(this, HomePostViewModelFactory(tokenManager)).get(
                    HomePostViewModel::class.java
                )
            homePostViewModel.orderPost(
                accessToken,
                postId.toString(),
                buyerId.toString()
            ) { message ->
                // Ensure the toast is shown on the main thread
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
            alertDialog.dismiss()
        }
    }
}
