package com.example.loversdiary.ui.boarding

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.loversdiary.R
import com.example.loversdiary.ui.boarding.screens.DateOfStartScreen
import com.example.loversdiary.ui.boarding.screens.PartnerNameScreen
import com.example.loversdiary.ui.boarding.screens.SelfNameScreen
import com.example.loversdiary.util.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.view_pager_fragment.view.*
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class ViewPagerFragment : Fragment(R.layout.view_pager_fragment) {

    private val viewPagerViewModel: ViewPagerViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.view_pager.isUserInputEnabled = false

        val fragmentList = arrayListOf(
            SelfNameScreen(viewPagerViewModel),
            PartnerNameScreen(viewPagerViewModel),
            DateOfStartScreen(viewPagerViewModel)
        )

        val viewPagerAdapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        view.view_pager.adapter = viewPagerAdapter

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewPagerViewModel.vewPagerEvent.collect { event ->
                when (event) {
                    is ViewPagerViewModel.ViewPagerEvent.NavigateToNextScreen -> {
                        view.view_pager.currentItem = view.view_pager.currentItem + 1
                    }
                    is ViewPagerViewModel.ViewPagerEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                    is ViewPagerViewModel.ViewPagerEvent.FinishBoarding -> {
                        findNavController().navigate(R.id.action_viewPagerFragment_to_homeFragment)
                        onBoardingFinished()
                    }
                    else -> {}
                }.exhaustive
            }
        }
    }

    private fun onBoardingFinished() {
        val sharedPrefs = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as AppCompatActivity?)!!.supportActionBar?.hide()
    }
}