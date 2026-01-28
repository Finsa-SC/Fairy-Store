package com.example.fairystore.cart

data class CartResponse(
    val userId: Int,
    val products: List<CartModel>
)
data class CartModel(
    val productId: Int,
    val quantity: Int
)
