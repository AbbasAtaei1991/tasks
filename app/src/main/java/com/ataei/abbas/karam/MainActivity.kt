package com.ataei.abbas.karam

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.WHITE
        }
        btnNavView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_job -> {
                    titleTv.text = "کاراها"
                    findController().navigate(R.id.jobFragment)
                }
                R.id.navigation_debt -> {
                    titleTv.text = "انجام نشده ها"
                    findController().navigate(R.id.debtFragment)
                }
                R.id.navigation_ransom -> {
                    titleTv.text = "جریمه ها"
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
}