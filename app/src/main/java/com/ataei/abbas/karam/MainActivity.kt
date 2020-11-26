package com.ataei.abbas.karam

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.ataei.abbas.karam.utils.UiMode
import com.ataei.abbas.karam.utils.UserPreferenceRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var userPreferenceRepository: UserPreferenceRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnNavView.selectedItemId = R.id.navigation_job
        lifecycleScope.launch(Dispatchers.Main) {
            val firsLaunch = userPreferenceRepository.fetchFirstLaunch().first()
            if (firsLaunch) {
                findController().navigate(R.id.ransomFragment)
                btnNavView.selectedItemId = R.id.navigation_ransom
                userPreferenceRepository.saveFirstLaunch(false)
            }
        }
        observeUiMode()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.WHITE
        }
        btnNavView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_job -> {
                    findController().navigate(R.id.jobFragment)
                }
                R.id.navigation_debt -> {
                    findController().navigate(R.id.debtFragment)
                }
                R.id.navigation_ransom -> {
                    findController().navigate(R.id.ransomFragment)
                }
            }
            true
        }
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()

    private fun findController(): NavController {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController
    }

    override fun onBackPressed() {
        finish()
    }

    private fun observeUiMode() {
        userPreferenceRepository.uiModeFlow.asLiveData().observe(this) {
            when (it!!) {
                UiMode.DARK -> Log.d("vvvvv", "dark")
                UiMode.LIGHT -> Log.d("vvvvv", "li")
            }
        }
    }
}