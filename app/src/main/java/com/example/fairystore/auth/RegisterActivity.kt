package com.example.fairystore.auth

import android.content.Intent
import android.hardware.biometrics.BiometricManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fairystore.MainActivity
import com.example.fairystore.R
import com.example.fairystore.core.network.ApiHelper
import com.example.fairystore.core.util.ValidationHelper
import com.example.fairystore.databinding.ActivityRegisterBinding
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.lbltoLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            this.finish()
        }
        binding.btnRegister.setOnClickListener {
            val view = binding.main
            if(ValidationHelper.hasEmpty(view)){
                Toast.makeText(this, "Require input is missing or empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val email = binding.txtEmail.text.toString().trim()
            val username = binding.txtUsername.text.toString().trim()
            val password = binding.txtPassword.text.toString().trim()

            registerUser(email, username, password)
        }
    }
    private fun registerUser(email: String, username: String, password: String){
        val jsonBody = JSONObject().apply {
            put("email", email)
            put("username", username)
            put("password", password)
        }
        ApiHelper.post("users", jsonBody){
            when(it){
                is ApiHelper.ApiResult.Success ->{
                    Toast.makeText(this, "Register Successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    this.finish()
                }
                is ApiHelper.ApiResult.Empty ->
                    Toast.makeText(this, it.msg, Toast.LENGTH_SHORT).show()
                is ApiHelper.ApiResult.Error ->
                    Toast.makeText(this, it.msg, Toast.LENGTH_SHORT).show()
            }
        }
    }
}