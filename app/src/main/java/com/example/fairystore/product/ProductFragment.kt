package com.example.fairystore.product

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fairystore.R
import com.example.fairystore.core.network.ApiHelper
import com.example.fairystore.databinding.FragmentProductBinding
import org.json.JSONArray
import org.json.JSONObject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProductFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    private val productList = mutableListOf<ProductModel>()
    private lateinit var adapter: ProductAdapter

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
        _binding = FragmentProductBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProductFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ProductAdapter(
            productList,
            onCard = {prd ->
                val intent = Intent(requireContext(), ProductDetailActivity::class.java)
                intent.putExtra("PRD_ID", prd.id)
                startActivity(intent)
            }
        )
        val rv = binding.rvProduct
        rv.adapter = adapter
        rv.layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        loadProduct()
    }

    private fun loadProduct(){
        ApiHelper.get("products"){
            when(it){
                is ApiHelper.ApiResult.Success -> {
                    val jsonData = JSONArray(it.jsonBody)
                    if(jsonData.length() <= 0) {
                        Toast.makeText(requireContext(), "No Product Found", Toast.LENGTH_SHORT).show()
                        return@get
                    }
                    productList.clear()
                    for(i in 0 until jsonData.length()){
                        val json = jsonData.getJSONObject(i)
                        val jsonRate = json.getJSONObject("rating")
                        productList.add(ProductModel(
                            json.getInt("id"),
                            json.getString("title"),
                            json.getDouble("price"),
                            json.getString("category"),
                            json.getString("image"),
                            jsonRate.getDouble("rate"),
                        ))
                        adapter.notifyDataSetChanged()
                    }
                }
                is ApiHelper.ApiResult.Empty ->
                    Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()
                is ApiHelper.ApiResult.Error ->
                    Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()
            }
        }
    }
}