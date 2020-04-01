package com.naughtychild.gamedemo

import android.content.Context
import android.hardware.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var sm: SensorManager
    lateinit var lightListener: SensorEventListener
    var spinnerArrayList: ArrayList<String> = ArrayList()
    lateinit var spinnerAdapter: BaseAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sm = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        // 获取传感器对象，并获取相关信息
        //一般传感器，包括光传感器，温度传感器，接近传感器，都是这样用法
        val lightSensor = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION)

        val lightInfo = """
            名称：${lightSensor.name}
            耗电量：${lightSensor.power}
            型号：${lightSensor.type}
            版本：${lightSensor.version}
            敏感度（测量的最小差值）：${lightSensor.minDelay}
            最大值：${lightSensor.maximumRange}
        """.trimIndent()
        Log.d("MainActivity", "onCreate: $lightInfo")
        lightListener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            }

            override fun onSensorChanged(event: SensorEvent) {
                Log.d(
                    "MainActivity", """
                    tx=${event.values[0]}
                    ty=${event.values[1]}
                    tz=${event.values[2]}
                """.trimIndent()
                )
            }
        }
        sm.registerListener(lightListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        //注意进行注销监听器
        sm.unregisterListener(lightListener)
    }
}
