package com.example.graduationproject.ui.mainActivityCustomer.ListComponents.material

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityMaterialsBinding

class MaterialsActivity : AppCompatActivity() {
    lateinit var viewBinding : ActivityMaterialsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMaterialsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        onClickBackBtn()
    }
    private fun onClickBackBtn() {
        viewBinding.buttonBack.setOnClickListener {
            // Handle the click event, for example, navigate back one step
            onBackPressed()
        }
    }

}