package com.example.graduationproject.ui.mainActivity.search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.R
import com.example.graduationproject.databinding.FragmentSearchBinding
import com.example.graduationproject.ui.login.TokenManager

class SearchFragment : Fragment(R.layout.fragment_search) {
    private lateinit var tokenManager: TokenManager
    private lateinit var viewBinding: FragmentSearchBinding
    private lateinit var postAdapter: PostAdapter
    private lateinit var searchViewModel: SearchViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentSearchBinding.bind(view)
        tokenManager = TokenManager(requireContext())

        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        // Initialize RecyclerView and adapter
        postAdapter = PostAdapter(emptyList())
        viewBinding.RVPost.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = postAdapter
        }

        onClickBack()

        // Call ViewModel method when search button is clicked
        viewBinding.editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val accessToken = tokenManager.getToken() ?: ""
                val address = viewBinding.editTextSearch.text.toString()
                searchViewModel.getPostsByPlace(accessToken, address)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        // Observe search results and update RecyclerView adapter
        searchViewModel.searchResults.observe(viewLifecycleOwner) { posts ->
            // Update RecyclerView adapter with new data
            postAdapter.updatePosts(posts)
        }
    }

    private fun onClickBack() {
        viewBinding.backBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}
