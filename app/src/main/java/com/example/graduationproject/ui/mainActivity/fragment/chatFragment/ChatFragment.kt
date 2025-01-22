package com.example.graduationproject.ui.mainActivity.fragment.chatFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.graduationproject.R
import com.example.graduationproject.databinding.FragmentChatBinding

class ChatFragment : Fragment(R.layout.fragment_chat) {

    private lateinit var viewBinding: FragmentChatBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentChatBinding.bind(view)
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
