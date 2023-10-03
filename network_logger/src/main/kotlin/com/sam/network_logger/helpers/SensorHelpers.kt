package com.sam.network_logger.helpers

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.Lifecycle
import kotlin.math.sqrt

fun startListeningToAccelerometer(
    context: Context,
    lifecycleEvent: Lifecycle.Event,
    onShakeDetected: () -> Unit
) {
    val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    val sensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

        override fun onSensorChanged(event: SensorEvent?) {
            if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                // Calculate total acceleration
                val acceleration = sqrt(x * x + y * y + z * z)

                // You can adjust the threshold as needed
                val threshold = 15f

                if (acceleration > threshold) {
                    // Shake detected, trigger the callback
                    onShakeDetected()
                }
            }
        }
    }

    if (lifecycleEvent == Lifecycle.Event.ON_CREATE) {
        sensorManager.registerListener(
            sensorEventListener,
            accelerometerSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }
    if (lifecycleEvent == Lifecycle.Event.ON_DESTROY) {
        sensorManager.unregisterListener(sensorEventListener)
    }
}