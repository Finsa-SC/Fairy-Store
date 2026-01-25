package com.example.fairystore.product

import android.graphics.Bitmap

data class ProductModel(
    val id: Int,
    val title: String,
    val price: Double,
    val category: String,
    val image: String,
    val rate: Double,
)
