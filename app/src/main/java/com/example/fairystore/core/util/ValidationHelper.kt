package com.example.fairystore.core.util
import android.view.View
import android.view.ViewGroup
import androidx.core.view.size
import com.google.android.material.textfield.TextInputEditText

object ValidationHelper {
    fun hasEmpty(view: View): Boolean =
        when(view){
            is TextInputEditText ->
                view.text.isNullOrBlank()
            is ViewGroup ->
                (0 until view.size)
                    .any{ hasEmpty(view.getChildAt(it))}
            else -> false
        }
}