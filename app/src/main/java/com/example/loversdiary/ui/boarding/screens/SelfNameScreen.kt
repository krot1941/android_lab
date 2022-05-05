package com.example.loversdiary.ui.boarding.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.viewpager2.widget.ViewPager2
import com.example.loversdiary.R
import com.example.loversdiary.databinding.ViewPageSelfNameFragmentBinding
import com.example.loversdiary.ui.boarding.ViewPagerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.view_page_self_name_fragment.view.*

@AndroidEntryPoint
class SelfNameScreen(
        private val viewModel: ViewPagerViewModel
) : Fragment(R.layout.view_page_self_name_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = ViewPageSelfNameFragmentBinding.bind(view)

        binding.apply {
            nextButtonSelfName.setOnClickListener {
                viewModel.userSelfName = selfNameTextEdit.text.toString()
                viewModel.onNextSelfNameClick()
            }
        }
    }
}