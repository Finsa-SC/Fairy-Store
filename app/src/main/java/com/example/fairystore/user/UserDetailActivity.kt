package com.example.fairystore.user

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fairystore.R
import com.example.fairystore.core.network.ApiHelper
import com.example.fairystore.databinding.ActivityUserDetailBinding
import org.json.JSONArray
import org.json.JSONObject

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userId = intent.getIntExtra("USER_ID", -1)
        loadUser(userId)
    }

    @SuppressLint("SetTextI18n")
    private fun loadUser(userId: Int){
        ApiHelper.get("users/${userId}"){
            when(it){
                is ApiHelper.ApiResult.Success -> {
                    val json = JSONObject(it.jsonBody)
                    val jsonFullName = json.getJSONObject("name")
                    val jsonAddress = json.getJSONObject("address")

                    binding.lblUsername.text = json.getString("username")

                    val first_name = jsonFullName.getString("firstname")
                    val last_name = jsonFullName.getString("lastname")
                    binding.lblFullName.text = "${first_name} ${last_name}"

                    binding.lblEmail.text = json.getString("email")
                    binding.lblPhone.text = json.getString("phone")

                    binding.lblCity.text = jsonAddress.getString("city")
                    binding.lblStreet.text = jsonAddress.getString("street")
                    binding.lblNumber.text= jsonAddress.getString("number")
                    binding.lblZipcode.text = jsonAddress.getString("zipcode")
                }
                is ApiHelper.ApiResult.Empty ->
                    Toast.makeText(this, it.msg, Toast.LENGTH_SHORT).show()
                is ApiHelper.ApiResult.Error ->
                    Toast.makeText(this, it.msg, Toast.LENGTH_SHORT).show()
            }
        }
    }
}