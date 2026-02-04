package com.example.fairystore.product

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fairystore.R
import com.example.fairystore.core.network.ApiHelper
import com.example.fairystore.core.util.ImageHelper
import com.example.fairystore.databinding.ActivityProductDetailBinding
import org.json.JSONObject

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val _id = intent.getIntExtra("PRD_ID", -1)
        loadProduct(_id)
    }


    private fun loadProduct(id: Int){
        ApiHelper.get("products/${id}"){
            when(it){
                is ApiHelper.ApiResult.Success -> {
                    val json = JSONObject(it.jsonBody)
                    val jsonRating = json.getJSONObject("rating")
                    ImageHelper.imageLoader(binding.imgProduct, json.getString("image"))
                    binding.lblTitle.text = json.getString("title")
                    binding.lblPrice.text = json.getString("price")
                    binding.lblDescription.text = json.getString("description")
                    binding.lblStar.text = jsonRating.getDouble("rate").toString()
                    binding.lblRating.text = jsonRating.getInt("count").toString()
                }
                is ApiHelper.ApiResult.Empty ->
                    Toast.makeText(this, it.msg, Toast.LENGTH_SHORT).show()
                is ApiHelper.ApiResult.Error ->
                    Toast.makeText(this, it.msg, Toast.LENGTH_SHORT).show()
            }
        }
    }
}