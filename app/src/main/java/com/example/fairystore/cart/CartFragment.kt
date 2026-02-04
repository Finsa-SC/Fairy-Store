package com.example.fairystore.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fairystore.R
import com.example.fairystore.core.network.ApiHelper
import com.example.fairystore.databinding.FragmentCartBinding
import org.json.JSONArray
import org.json.JSONObject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CartFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    var _binding: FragmentCartBinding? = null
    val binding get() = _binding!!


    private var cartLoaded = false
    private var productLoaded = false


    private var responseCart: CartResponse? = null
    private var productList = mutableListOf<ProductModel>()
    private var cartList = mutableListOf<CartModel>()
    private var cartUiList = mutableListOf<CartItemModel>()


    private lateinit var adapter: CartAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rv = binding.rvCart

        rv.layoutManager = LinearLayoutManager(requireContext())
        adapter = CartAdapter(cartUiList)
        rv.adapter = adapter

        getUserCart(1)
        getProduct()
    }

    private fun getUserCart(userId: Int) {
        ApiHelper.get("carts/${userId}") {
            when (it) {
                is ApiHelper.ApiResult.Success -> {
                    val jsonResponse = JSONObject(it.jsonBody)
                    val cartResponse = jsonResponse.getJSONArray("products")

                    cartList.clear()
                    for (i in 0 until cartResponse.length()) {
                        val json = cartResponse.getJSONObject(i)

                        cartList.add(
                            CartModel(
                                json.getInt("productId"),
                                json.getInt("quantity")
                            )
                        )
                    }
                    adapter.notifyDataSetChanged()
                    responseCart = CartResponse( jsonResponse.getInt("id"), cartList)
                }

                is ApiHelper.ApiResult.Empty ->
                    Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()

                is ApiHelper.ApiResult.Error ->
                    Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun getProduct(){
        ApiHelper.get("products"){
            when(it){
                is ApiHelper.ApiResult.Success -> {
                    val jsonResponse = JSONArray(it.jsonBody)
                    productList.clear()
                    for(i in 0 until jsonResponse.length()){
                        val json = jsonResponse.getJSONObject(i)
                        productList.add(ProductModel(
                            json.getInt("id"),
                            json.getString("title"),
                            json.getDouble("price"),
                            json.getString("image")
                        ))
                    }
                }
                is ApiHelper.ApiResult.Empty ->
                    Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()
                is ApiHelper.ApiResult.Error ->
                    Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun cartMapping(){

        if(!cartLoaded || !productLoaded) return
        val productMap = productList.associateBy { it.id }

        cartUiList.clear()
        responseCart?.products?.forEach { cart ->
            val item = productMap[cart.productId]
            item?.let {
                cartUiList.add(CartItemModel(
                    image = it.image,
                    title = it.title,
                    price = it.price,
                    qty = cart.quantity
                ))
                adapter.notifyDataSetChanged()
            }
        }
    }
}