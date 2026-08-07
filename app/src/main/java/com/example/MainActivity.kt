package com.example.autoclicker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 100, 50, 50)
        }

        val title = TextView(this).apply {
            text = "散步趣廣告點擊器"
            textSize = 24f
        }

        val desc = TextView(this).apply {
            text = "\n請點擊下方按鈕前往開啟無障礙權限\n"
            textSize = 16f
        }

        val btnOpenSettings = Button(this).apply {
            text = "前往開啟無障礙權限"
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            }
        }

        layout.addView(title)
        layout.addView(desc)
        layout.addView(btnOpenSettings)
        setContentView(layout)
    }
}
