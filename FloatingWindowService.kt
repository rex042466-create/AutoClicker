package com.example

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView

class FloatingWindowService : Service() {
    private lateinit var windowManager: WindowManager
    private lateinit var statusView: TextView

    companion object {
        var clickCount = 0
        var instance: FloatingWindowService? = null

        fun updateCount() {
            clickCount++
            instance?.statusView?.post {
                instance?.statusView?.text = "已完成廣告：$clickCount 次"
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        statusView = TextView(this).apply {
            text = "已完成廣告：0 次"
            setBackgroundColor(0xAA000000.toInt()) 
            setTextColor(0xFFFFFFFF.toInt())      
            setPadding(30, 20, 30, 20)
            textSize = 14f
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 50
            y = 200
        }

        windowManager.addView(statusView, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(statusView)
        instance = null
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
