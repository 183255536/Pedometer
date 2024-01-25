package com.example.pedometer

import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result


/** PedometerPlugin */
class PedometerPlugin : FlutterPlugin, MethodCallHandler {
    private lateinit var stepDetectionChannel: EventChannel
    private lateinit var stepCountChannel: EventChannel
    private lateinit var stepMethodChannel: MethodChannel
    private lateinit var binding: FlutterPlugin.FlutterPluginBinding
    private var tag: String = "PedometerPlugin:::"

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        /// Create channels
        stepDetectionChannel = EventChannel(flutterPluginBinding.binaryMessenger, "step_detection")
        stepCountChannel = EventChannel(flutterPluginBinding.binaryMessenger, "step_count")
        stepMethodChannel = MethodChannel(flutterPluginBinding.binaryMessenger, "step_method")
        binding = flutterPluginBinding;
        stepMethodChannel.setMethodCallHandler(this)
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        stepDetectionChannel.setStreamHandler(null)
        stepCountChannel.setStreamHandler(null)
        stepMethodChannel.setMethodCallHandler(null)
    }


    override fun onMethodCall(
        @NonNull call: MethodCall, @NonNull result: Result
    ) {
        // 采用手动初始化，防止自动调用
        if (call.method.equals("initPlugin")) {
            println(tag + "插件初始化");

            /// Create handlers
            val stepDetectionHandler = SensorStreamHandler(binding, Sensor.TYPE_STEP_DETECTOR)
            val stepCountHandler = SensorStreamHandler(binding, Sensor.TYPE_STEP_COUNTER)

            /// Set handlers
            stepDetectionChannel.setStreamHandler(stepDetectionHandler)
            stepCountChannel.setStreamHandler(stepCountHandler)

            result.success(true);
        }
    }
}
