package com.example.loversdiary.ui.boarding.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.example.loversdiary.R
import com.example.loversdiary.databinding.ViewPagePartnerNameFragmentBinding
import com.example.loversdiary.databinding.ViewPageSelfNameFragmentBinding
import com.example.loversdiary.ui.boarding.ViewPagerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.view_page_partner_name_fragment.*

@AndroidEntryPoint
class PartnerNameScreen(
        private val viewModel: ViewPagerViewModel
) : Fragment(R.layout.view_page_partner_name_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = ViewPagePartnerNameFragmentBinding.bind(view)

        binding.apply {
            nextButtonPartnerName.setOnClickListener {
                viewModel.userPartnerName = partnerNameTextEdit.text.toString()
                viewModel.onNextPartnerNameClick()
            }
        }
    }
}