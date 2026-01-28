package com.example.fairystore

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fairystore.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager

        val icons = listOf(
            R.drawable.outline_business_chip_24,
            R.drawable.baseline_shopping_cart_24,
            R.drawable.round_person_2_24
        )
        val adapter = MainPagerAdapter(this)
        viewPager.adapter=adapter

        TabLayoutMediator(tabLayout, viewPager) {tab, pos ->
            tab.icon = ContextCompat.getDrawable(this, icons[pos])
        }.attach()

    }
}