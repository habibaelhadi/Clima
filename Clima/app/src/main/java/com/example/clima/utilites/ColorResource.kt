package com.example.clima.utilites

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.example.clima.R

enum class ColorResource(@ColorRes val resId: Int) {
    GREY(R.color.grey),
    PURPLE(R.color.purple),
    WHITE(R.color.white),
    BLACK(R.color.black);

    fun getColor(context: Context): Int {
        return ContextCompat.getColor(context, resId)
    }
}