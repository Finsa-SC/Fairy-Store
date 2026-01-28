package com.example.fairystore.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fairystore.core.util.ImageHelper
import com.example.fairystore.databinding.CartDesignUiBinding

class CartAdapter(
    private val cartList: List<CartItemModel>
): RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    class ViewHolder(val binding: CartDesignUiBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CartDesignUiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cart = cartList[position]
        holder.binding.lblTitle.text = cart.title
        holder.binding.lblPrice.text = cart.price.toString()
        holder.binding.lblQuantity.text = cart.qty.toString()
        ImageHelper.imageLoader(holder.binding.imgProduct, cart.image)

    }

    override fun getItemCount(): Int = cartList.size
}