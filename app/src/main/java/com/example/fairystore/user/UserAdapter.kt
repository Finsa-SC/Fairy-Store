package com.example.fairystore.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fairystore.databinding.UserUiModelBinding

class UserAdapter(
    private val userList: List<UserUiModel>,
    private val onCard: (UserUiModel) -> Unit
): RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    class ViewHolder(val binding: UserUiModelBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UserUiModelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
        holder.binding.lblUsername.text = user.username
        holder.binding.lblEmail.text = user.email
        holder.binding.lblPhone.text = user.phone

        holder.binding.namecard.setOnClickListener {
            onCard(user)
        }
    }

    override fun getItemCount(): Int = userList.size
}