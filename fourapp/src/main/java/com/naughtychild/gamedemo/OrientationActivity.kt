package com.naughtychild.gamedemo

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_orientation.*

class OrientationActivity : AppCompatActivity(), SensorEventListener {
    val r = FloatArray(9)
    val valuesArray = FloatArray(3)
    var accValues: FloatArray? = null
    var magValues: FloatArray? = null
    val sm by lazy {
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    val accSensor by lazy { sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }
    val magSensor by lazy { sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orientation)
//old api
        val oriSensor = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION)
        sm.registerListener(object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            }

            override fun onSensorChanged(event: SensorEvent) {
                oldApiTV.setText(
                    """
                        oldAPI:
    Azimuth:${event.values[0]}
    Pitch:${event.values[1]}
    Roll：${event.values[2]}
""".trimIndent()
                )
            }

        }, oriSensor, SensorManager.SENSOR_DELAY_NORMAL)

//new api
        sm.registerListener(this, accSensor, SensorManager.SENSOR_STATUS_ACCURACY_LOW)
        sm.registerListener(this, magSensor, SensorManager.SENSOR_STATUS_ACCURACY_LOW)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                accValues = event.values
            }
            Sensor.TYPE_MAGNETIC_FIELD -> {
                magValues = event.values
            }
        }
        calculate()
    }

    private fun calculate() {
        if (magValues != null && accValues != null) {
            if (SensorManager.getRotationMatrix(r, null, accValues, magValues)) {
                 SensorManager.getOrientation(r, valuesArray)
                newApiTv.setText(
                    """
                    newApi
                      Azimuth:${valuesArray[0]}
                      Pitch:${valuesArray[1]}
                      Roll：${valuesArray[2]}
                """.trimIndent()
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        sm.unregisterListener(this)
    }
}
