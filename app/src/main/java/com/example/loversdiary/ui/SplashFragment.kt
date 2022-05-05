package com.example.loversdiary.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.loversdiary.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.splash_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler().postDelayed({
            when (onBoardingFinished()) {
                true -> findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                false -> findNavController().navigate(R.id.action_splashFragment_to_viewPagerFragment)
            }
        }, 2000)
    }

    private fun onBoardingFinished(): Boolean {
        val sharedPrefs = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean("Finished", false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as AppCompatActivity?)!!.supportActionBar?.hide()
    }
}