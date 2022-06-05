package com.example.myapplication

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() , SensorEventListener {
    lateinit var mSensor: Sensor
    var start_time: Long = 0
    var startFlag: Boolean = true
    var endFlag: Boolean = false
    var b_x: Float = 0.0f
    var b_y: Float = 0.0f
    var b_z: Float = 0.0f

    val BORDER: Double = 0.5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            game_start()
        }

        var mSensorManager: SensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI)

        game_start()
    }

    private fun game_start() {
        start_time = System.currentTimeMillis()

        val txt: TextView = findViewById(R.id.messageTextView)
        txt.text = getString(R.string.safe)

        val img: ImageView = findViewById(R.id.imageView)
        img.setImageResource(R.drawable.neko1)

        startFlag = true
        endFlag = false
    }

    private fun game_over() {
        var record_time :Long = System.currentTimeMillis() - start_time
        var sec: Long = record_time / 1000
        var msec: Long = (record_time % 1000) /100

        val txt: TextView = findViewById(R.id.messageTextView)
        txt.text = "記録^: " + sec.toString() + "."+ msec.toString() +" 秒"

        val img: ImageView = findViewById(R.id.imageView)
        if (sec == 3L && msec == 0L) {
            img.setImageResource(R.drawable.neko4)
        } else if (sec < 3L) {
            img.setImageResource(R.drawable.neko2)
        } else {
            img.setImageResource(R.drawable.neko3)
        }

        endFlag = true
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
            if (startFlag == true) {
                b_x = event.values[0]
                b_y = event.values[1]
                b_z = event.values[2]
                startFlag = false
            }

            var x: Float = event.values[0]
            var y: Float = event.values[1]
            var z: Float = event.values[2]
            var dist_x: Float = (x - b_x).absoluteValue
            var dist_y: Float = (y - b_y).absoluteValue
            var dist_z: Float = (z - b_z).absoluteValue

            if (endFlag == false) {
                if(dist_x > BORDER || dist_y > BORDER || dist_z > BORDER) {
                    game_over()
                }
            }

            b_x = x
            b_y = y
            b_z = z

        }
    }
}