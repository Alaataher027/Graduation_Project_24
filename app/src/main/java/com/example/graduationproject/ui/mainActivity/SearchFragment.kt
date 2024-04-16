package com.example.graduationproject.ui.mainActivity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.graduationproject.R
import com.example.graduationproject.databinding.FragmentSearchBinding
import com.example.graduationproject.ui.login.TokenManager

class SearchFragment : Fragment(R.layout.fragment_search) {
    private lateinit var tokenManager: TokenManager
    private lateinit var viewBinding: FragmentSearchBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentSearchBinding.bind(view)

        onClickBack()
    }

    private fun onClickBack() {
        viewBinding.backBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

}