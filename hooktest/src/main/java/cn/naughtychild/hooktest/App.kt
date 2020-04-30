package cn.naughtychild.hooktest

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.lody.legend.Hook
import com.lody.legend.HookManager
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("App", "onCreate: 调用了");
        Proxy.newProxyInstance(
            Log::class.java.classLoader,
            Log::class.java.interfaces,
            object : InvocationHandler {
                override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
                    println("调用了")
//                val result = method?.invoke(proxy, args)
                    println(method.toString())
                    return 1
                }
            })
    }

    @Hook("android.util.Log::d@java.lang.String#java.lang.String")
    fun Log_d(l: Log,str1:String,str2:String):Int {
        HookManager.getDefault().callSuper<Any>(l,str1,str2)
        Toast.makeText(this, "ss", Toast.LENGTH_SHORT).show()
        return 1
    }
}