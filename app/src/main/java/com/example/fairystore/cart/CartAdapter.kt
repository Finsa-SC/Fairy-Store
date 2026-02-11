package com.example.fairystore.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.fairystore.core.util.ImageHelper
import com.example.fairystore.databinding.CartDesignUiBinding

class CartAdapter(
    private val cartList: MutableList<CartItemModel>,
    private val onPlus: (CartItemModel) -> Unit,
    private val onDecrease: (CartItemModel) -> Unit
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

        holder.binding.btnAdd.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if(pos != RecyclerView.NO_POSITION){
                onPlus(cart)
                notifyItemChanged(pos)
            }
        }
        holder.binding.btnDecrease.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if(pos!= RecyclerView.NO_POSITION){
                if(cart.qty > 1){
                    onDecrease(cart)
                    notifyItemChanged(pos)
                }else{
                    cartList.removeAt(pos)
                    notifyItemRemoved(pos)
                }
            }
        }
    }

    override fun getItemCount(): Int = cartList.size
}