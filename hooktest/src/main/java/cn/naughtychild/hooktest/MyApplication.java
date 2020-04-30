package cn.naughtychild.hooktest;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.lody.legend.Hook;
import com.lody.legend.HookManager;

import java.lang.reflect.Method;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            Method method1 = Log.class.getDeclaredMethod("d", String.class, String.class);
            Method method2=this.getClass().getMethod("logd");
            HookManager.getDefault().hookMethod(method1, method2);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static void logd() {

    }

    @Hook("android.util.Log::d@java.lang.String#java.lang.String")
    public static int Log_d(Log log, String str1, String str2) {
        HookManager.getDefault().callSuper(log, str1, str2);
        System.out.println("lallalal");
        return 2;
    }
}
