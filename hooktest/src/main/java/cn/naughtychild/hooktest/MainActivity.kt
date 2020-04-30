package cn.naughtychild.hooktest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import cn.naughtychild.hooklib.LogClass
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val logClass: LogClass = LogClass()
        text2.text = logClass.talk()
        text2.setOnClickListener {
            Toast.makeText(this,"asasa",Toast.LENGTH_SHORT).show()
            Log.d("MainActivity", "onCreate: ssdsdds");
        }
    }
}
