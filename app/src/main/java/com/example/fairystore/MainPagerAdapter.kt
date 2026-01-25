package com.example.fairystore

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.fairystore.cart.CartFragment
import com.example.fairystore.product.ProductFragment
import com.example.fairystore.user.UserFragment

class MainPagerAdapter(fragment: FragmentActivity): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> ProductFragment()
            1-> CartFragment()
            2-> UserFragment()
            else -> ProductFragment()
        }
    }
}