package com.naughtychild.fivedemo.ext

import android.app.Application
import android.content.Context
import android.widget.Toast

fun String.toast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}