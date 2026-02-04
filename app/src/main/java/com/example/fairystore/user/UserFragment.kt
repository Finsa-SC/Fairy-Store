package com.example.fairystore.user

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fairystore.R
import com.example.fairystore.core.network.ApiHelper
import com.example.fairystore.databinding.FragmentUserBinding
import kotlinx.coroutines.flow.combine
import org.json.JSONArray

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class UserFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    var _binding: FragmentUserBinding? = null
    val binding get() = _binding!!


    private lateinit var adapter: UserAdapter
    private val userList = mutableListOf<UserUiModel>()

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
        _binding = FragmentUserBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rv = binding.rvUser
        adapter = UserAdapter(
            userList,
            onCard = {user ->
                val intent = Intent(requireContext(), UserDetailActivity::class.java)
                intent.putExtra("USER_ID", user.id)
                startActivity(intent)
            }
            )

        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(requireContext())

        loadUser()
    }

    private fun loadUser(){
        ApiHelper.get("users"){
            when(it){
                is ApiHelper.ApiResult.Success -> {
                    val jsonArr = JSONArray(it.jsonBody)
                    for(i in 0 until jsonArr.length()){
                        val json = jsonArr.getJSONObject(i)
                        userList.add(UserUiModel(
                            json.getInt("id"),
                            json.getString("email"),
                            json.getString("username"),
                            json.getString("phone"),
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