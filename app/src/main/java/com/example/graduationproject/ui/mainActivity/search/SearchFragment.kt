package com.example.graduationproject.ui.mainActivity.search

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.R
import com.example.graduationproject.databinding.FragmentSearchBinding
import com.example.graduationproject.ui.login.TokenManager
import com.example.graduationproject.ui.mainActivity.fragment.home.UserDataHomeViewModel

class SearchFragment : Fragment(R.layout.fragment_search) {
    private lateinit var tokenManager: TokenManager
    private lateinit var viewBinding: FragmentSearchBinding
    private lateinit var postAdapter: PostAdapter
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var viewModelUserData: UserDataHomeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentSearchBinding.bind(view)
        tokenManager = TokenManager(requireContext())
        viewModelUserData = ViewModelProvider(this).get(UserDataHomeViewModel::class.java)

        val factory = SearchViewModelFactory(tokenManager)
        searchViewModel = ViewModelProvider(this, factory).get(SearchViewModel::class.java)

        // Initialize RecyclerView and adapter
        postAdapter = PostAdapter(emptyList(), tokenManager, searchViewModel)
        viewBinding.RVPost.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = postAdapter
        }

        val accessToken = tokenManager.getToken() ?: ""

        onClickBack()

        // Call ViewModel method when search button is clicked
        viewBinding.editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val accessToken = tokenManager.getToken() ?: ""
                val address = viewBinding.editTextSearch.text.toString()
                viewBinding.progressBar.visibility = View.VISIBLE

                searchViewModel.getPostsByPlace(accessToken, address)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        // Observe search results and update RecyclerView adapter
        searchViewModel.searchResults.observe(viewLifecycleOwner) { posts ->
            viewBinding.progressBar.visibility = View.GONE

            // Update RecyclerView adapter with new data
            postAdapter.updatePosts(posts)
            val userIds = posts.mapNotNull { it?.userId }
            userIds.distinct().forEach { userId ->
                viewModelUserData.getData(accessToken, userId, { userData ->
                    userData?.let {
                        viewBinding.scrollView.visibility = View.VISIBLE
                        postAdapter.addUserData(userId, userData)
                    }
                }, { error ->
                    // Handle error
                    Log.e("UserDataHomeViewModel", "Failed to get user data: $error")
                })
            }

            // Show the scrollView when search results are available
            viewBinding.scrollView.visibility = View.VISIBLE
        }

        filterTheSearchResponse()
    }

    private fun filterTheSearchResponse() {
        viewBinding.plasticBtn.setOnClickListener {
            resetButtonsState() // Reset other buttons' states
            viewBinding.plasticBtn.setBackgroundResource(R.drawable.rec_press)
            viewBinding.plasticBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            searchViewModel.filterByMaterial(listOf("بلاستيك", "plastic", "Plastic"))
        }

        // (2) Metal button
        viewBinding.metalBtn.setOnClickListener {
            resetButtonsState() // Reset other buttons' states
            viewBinding.metalBtn.setBackgroundResource(R.drawable.rec_press)
            viewBinding.metalBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            searchViewModel.filterByMaterial(listOf("معدن", "metal"))
        }

        // (3) Glass button
        viewBinding.glassBtn.setOnClickListener {
            resetButtonsState() // Reset other buttons' states
            viewBinding.glassBtn.setBackgroundResource(R.drawable.rec_press)
            viewBinding.glassBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            searchViewModel.filterByMaterial(listOf("زجاج", "glass", "white-glass"))
        }

        // (7) brown Glass button
        viewBinding.brownGlassBtn.setOnClickListener {
            resetButtonsState() // Reset other buttons' states
            viewBinding.brownGlassBtn.setBackgroundResource(R.drawable.rec_press)
            viewBinding.brownGlassBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            searchViewModel.filterByMaterial(listOf("brown-glass"))
        }

        // (4) Paper button
        viewBinding.paperBtn.setOnClickListener {
            resetButtonsState() // Reset other buttons' states
            viewBinding.paperBtn.setBackgroundResource(R.drawable.rec_press)
            viewBinding.paperBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            searchViewModel.filterByMaterial(listOf("ورق", "paper"))
        }

        // (5) Steel button
        viewBinding.steelBtn.setOnClickListener {
            resetButtonsState() // Reset other buttons' states
            viewBinding.steelBtn.setBackgroundResource(R.drawable.rec_press)
            viewBinding.steelBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            searchViewModel.filterByMaterial(listOf("حديد", "steel"))
        }

        // (6) Wood button
        viewBinding.woodBtn.setOnClickListener {
            resetButtonsState() // Reset other buttons' states
            viewBinding.woodBtn.setBackgroundResource(R.drawable.rec_press)
            viewBinding.woodBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            searchViewModel.filterByMaterial(listOf("خشب", "wood"))
        }

        // (8) cardboard button
        viewBinding.cardboard.setOnClickListener {
            resetButtonsState() // Reset other buttons' states
            viewBinding.cardboard.setBackgroundResource(R.drawable.rec_press)
            viewBinding.cardboard.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            searchViewModel.filterByMaterial(
                listOf(
                    "كرتون",
                    "cardboard",
                    "ورق مقوى",
                    "ورق مقوي",
                    "كرتونة"
                )
            )
        }

        // (8) battery button
        viewBinding.battery.setOnClickListener {
            resetButtonsState() // Reset other buttons' states
            viewBinding.battery.setBackgroundResource(R.drawable.rec_press)
            viewBinding.battery.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            searchViewModel.filterByMaterial(listOf("بطارية", "battery", "بطاريات"))
        }
    }

    private fun resetButtonsState() {
        // Reset all buttons' background and text color to default
        viewBinding.plasticBtn.setBackgroundResource(R.drawable.rectangle_bord)
        viewBinding.plasticBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        viewBinding.metalBtn.setBackgroundResource(R.drawable.rectangle_bord)
        viewBinding.metalBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        viewBinding.glassBtn.setBackgroundResource(R.drawable.rectangle_bord)
        viewBinding.glassBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        viewBinding.paperBtn.setBackgroundResource(R.drawable.rectangle_bord)
        viewBinding.paperBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        viewBinding.steelBtn.setBackgroundResource(R.drawable.rectangle_bord)
        viewBinding.steelBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        viewBinding.woodBtn.setBackgroundResource(R.drawable.rectangle_bord)
        viewBinding.woodBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        viewBinding.brownGlassBtn.setBackgroundResource(R.drawable.rectangle_bord)
        viewBinding.brownGlassBtn.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.black
            )
        )
        viewBinding.battery.setBackgroundResource(R.drawable.rectangle_bord)
        viewBinding.battery.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        viewBinding.cardboard.setBackgroundResource(R.drawable.rectangle_bord)
        viewBinding.cardboard.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
    }

    private fun onClickBack() {
        viewBinding.backBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}