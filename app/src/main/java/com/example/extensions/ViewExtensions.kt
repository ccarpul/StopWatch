package com.example.extensions

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible

fun Button.customText(resource: Int){ this.text = resources.getString(resource)}

fun View.customDrawable(resource: Int){ this.background = resources.getDrawable(resource)}

