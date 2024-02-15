package com.example.graduationproject.ui.mainActivityCustomer.fragment.chatFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.graduationproject.R
import com.example.graduationproject.databinding.CustomerFragmentChatBinding

class ChatFragmentCustomer : Fragment(R.layout.customer_fragment_chat) {

    private lateinit var viewBinding: CustomerFragmentChatBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = CustomerFragmentChatBinding.bind(view)
        //navigateToEditProfile()


    }

//    private fun navigateToEditProfile() {
//        // Use Intent to navigate to EditProfileActivity
//        viewBinding.buttonEdit.setOnClickListener {
//            val intent = Intent(requireActivity(), EditProfileBuyer::class.java)
//            requireActivity().startActivity(intent)
//        }
//    }


}
