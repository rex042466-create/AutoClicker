package com.example.autoclicker

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    private lateinit var statusText: TextView
    private lateinit var btnAction: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 80, 50, 50)
        }

        val title = TextView(this).apply {
            text = "散步趣自動點擊助手"
            textSize = 24f
            setPadding(0, 0, 0, 30)
        }

        statusText = TextView(this).apply {
            text = "正在檢查權限狀態..."
            textSize = 16f
            setPadding(0, 0, 0, 40)
        }

        btnAction = Button(this)

        layout.addView(title)
        layout.addView(statusText)
        layout.addView(btnAction)
        setContentView(layout)
    }

    override fun onResume() {
        super.onResume()
        checkAccessibilityPermission()
    }

    private fun checkAccessibilityPermission() {
        val serviceName = "${packageName}/.service.AutoClickAccessibilityService"
        var isEnabled = false

        try {
            val enabledServicesSetting = Settings.Secure.getString(
                contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            if (!TextUtils.isEmpty(enabledServicesSetting)) {
                val splitter = TextUtils.SimpleStringSplitter(':')
                splitter.setString(enabledServicesSetting)
                while (splitter.hasNext()) {
                    val componentName = splitter.next()
                    if (componentName.equals(serviceName, ignoreCase = true)) {
                        isEnabled = true
                        break
                    }
                }
            }
        } catch (e: Exception) {
            isEnabled = false
        }

        if (isEnabled) {
            // 權限已開，啟動懸浮窗
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                statusText.text = "請開啟「顯示在其他應用程式上層」權限"
                btnAction.text = "前往設定懸浮窗權限"
                btnAction.setOnClickListener {
                    startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
                }
            } else {
                startService(Intent(this, FloatingWindowService::class.java))
                statusText.text = "狀態：✅ 無障礙權限已啟用！\n\n【使用引導】\n1. 懸浮視窗已在螢幕上顯示。\n2. 請直接切換至 LINE 「散步趣」即可開始運作。"
                btnAction.text = "重新整理狀態"
                btnAction.setOnClickListener { checkAccessibilityPermission() }
            }
        } else {
            statusText.text = "狀態：⚠️ 尚未開啟無障礙權限\n\n請點擊下方按鈕前往手機設定，將本 App 的服務開啟。"
            btnAction.text = "前往開啟無障礙權限"
            btnAction.setOnClickListener {
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            }
        }
    }
}
