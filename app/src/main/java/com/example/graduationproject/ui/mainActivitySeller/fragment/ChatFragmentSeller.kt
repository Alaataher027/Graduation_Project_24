package com.example.graduationproject.ui.mainActivitySeller.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.graduationproject.R
import com.example.graduationproject.databinding.CustomerFragmentChatBinding
import com.example.graduationproject.databinding.SellerFragmentChatBinding

class ChatFragmentSeller : Fragment(R.layout.seller_fragment_chat) {

    private lateinit var viewBinding: SellerFragmentChatBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = SellerFragmentChatBinding.bind(view)
        //navigateToEditProfile()
    }
//
//    private fun navigateToEditProfile() {
//        // Use Intent to navigate to EditProfileActivity
//        viewBinding.buttonEdit.setOnClickListener {
//            val intent = Intent(requireActivity(), EditProfileBuyer::class.java)
//            requireActivity().startActivity(intent)
//        }
//    }


}
