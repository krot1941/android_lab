package com.example.loversdiary.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.loversdiary.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.momentsListFragment)
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        bottom_nav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id)   {
                R.id.splashFragment -> bottom_nav.visibility = View.GONE
                R.id.viewPagerFragment -> bottom_nav.visibility = View.GONE
                else -> bottom_nav.visibility = View.VISIBLE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}

const val PERMISSION_CODE = 101
const val EDIT_SETTINGS_RESULT_OK = Activity.RESULT_FIRST_USER
const val ADD_MOMENT_RESULT_OK = Activity.RESULT_FIRST_USER + 1
const val EDIT_MOMENT_RESULT_OK = Activity.RESULT_FIRST_USER + 2
const val DELETE_MOMENT_RESULT_OK = Activity.RESULT_FIRST_USER + 3
