package com.naughtychild.fivedemo.ext

import android.view.View

fun View.click(block: () -> Unit) {
    this.setOnClickListener {
        block()
    }
}