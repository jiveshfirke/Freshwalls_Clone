package com.dedsec.freshwalls.utils

import android.content.Context
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

interface DisplayMetrics {
    companion object {
        fun getHeight(context: Context): Dp {
            return context.resources.displayMetrics.heightPixels.dp
        }

        fun getWidth(context: Context): Dp {
            return context.resources.displayMetrics.widthPixels.dp
        }
    }
}