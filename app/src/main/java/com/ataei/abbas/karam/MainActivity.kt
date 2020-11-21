package com.ataei.abbas.karam

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.asLiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.ataei.abbas.karam.data.model.Job
import com.ataei.abbas.karam.utils.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var userPreferenceRepository: UserPreferenceRepository
    private var date = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setDate()
        observeUiMode()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.WHITE
        }
        btnNavView.selectedItemId = R.id.navigation_job
        btnNavView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_job -> {
                    titleTv.text = "وظایف"
                    findController().navigate(R.id.jobFragment)
                }
                R.id.navigation_debt -> {
                    titleTv.text = "آمار"
                    findController().navigate(R.id.debtFragment)
                }
                R.id.navigation_ransom -> {
                    titleTv.text = "تنظیمات"
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

    private fun setDate() {
        val sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val currentDateTime = sdf.format(Date())
        date = DateUtils.getShamsiDate(Date())!!
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