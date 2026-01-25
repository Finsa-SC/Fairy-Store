package com.example.fairystore.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fairystore.MainActivity
import com.example.fairystore.R
import com.example.fairystore.core.network.ApiHelper
import com.example.fairystore.databinding.ActivityLoginBinding
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.txtUsername.text.toString().trim()
            val password = binding.txtPassword.text.toString().trim()

            loginUser(username, password)
        }
        binding.lbltoRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun loginUser(username: String, password: String){


        val jsonBody = JSONObject().apply {
            put("username", username)
            put("password", password)
        }

        Thread{
            val (code, response) = ApiHelper.post("auth/login", jsonBody)
            runOnUiThread {
                if(code == 200 || code == 201 && !response.isNullOrBlank()){
                    val jsonResponse = JSONObject(response)
                    val token = jsonResponse.getString("token")
                    startActivity(Intent(this, MainActivity::class.java))
                    this.finish()
                }else if(!response.isNullOrBlank()){
                    Toast.makeText(this,  response, Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Server Doesn't Responding", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
}