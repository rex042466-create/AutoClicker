package com.example.autoclicker

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 用程式碼建立一個簡單乾淨的介面，不需要額外的 xml 佈局檔
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 100, 50, 50)
        }

        val title = TextView(this).apply {
            text = "散步趣廣告點擊器"
            textSize = 24f
        }

        val desc = TextView(this).apply {
            text = "\n請點擊下方按鈕，前往手機設定中開啟「無障礙服務」權限，App 才能開始自動點擊廣告。\n"
            textSize = 16f
        }

        val btnOpenSettings = Button(this).apply {
            text = "前往開啟無障礙權限"
            setOnClickListener {
                // 直接跳轉到手機的無障礙設定頁面
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                startActivity(intent)
            }
        }

        layout.addView(title)
        layout.addView(desc)
        layout.addView(btnOpenSettings)

        setContentView(layout)
    }
}
