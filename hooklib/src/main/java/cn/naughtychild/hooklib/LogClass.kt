package cn.naughtychild.hooklib

import android.util.Log

class LogClass {
    fun talk(): String {
        Log.d("LogClass", "talk: 我说话了");
        return "我要说话了"
    }
}