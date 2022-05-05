package com.example.loversdiary.util

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager

class NSGridLayoutManager(context: Context, spanCount: Int) : GridLayoutManager(context, spanCount) {
    override fun canScrollVertically(): Boolean {
        return false
    }
}