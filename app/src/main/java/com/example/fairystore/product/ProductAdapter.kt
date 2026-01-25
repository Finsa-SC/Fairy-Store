package com.example.fairystore.product

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fairystore.databinding.ProductItemModelBinding

class ProductAdapter(
    private val productList: List<ProductModel>
): RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    class ViewHolder(val binding: ProductItemModelBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProductItemModelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]
        holder.binding.lblTitle.text = product.title
        holder.binding.lblPrice.text ="$${product.price}"
        holder.binding.lblRating.text = product.rate.toString()
    }

    override fun getItemCount(): Int = productList.size
}