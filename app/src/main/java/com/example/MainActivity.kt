package com.example.autoclicker

import android.app.Activity
import android.content.Intent
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
        val serviceName = "${packageName}/${AutoClickAccessibilityService::class.java.name}"
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
            statusText.text = "狀態：✅ 無障礙權限已啟用！\n\n【使用引導與功能介紹】\n1. 請切換至 LINE 「散步趣」的移動頁面。\n2. 點擊「獲得金幣」後，本工具會自動幫您點擊中間的「60 獲得」。\n3. 進入廣告後會自動識別左上角跳過符號，並在最後自動點擊右上角「×」關閉廣告。\n4. 自動循環幫您省去長時間手動看廣告的麻煩！"
            btnAction.text = "重新整理權限狀態"
            btnAction.setOnClickListener {
                checkAccessibilityPermission()
            }
        } else {
            statusText.text = "狀態：⚠️ 尚未開啟無障礙權限\n\n請點擊下方按鈕前往手機設定，找到「下載的應用程式」，將「散步趣廣告點擊器」切換為開啟。"
            btnAction.text = "前往開啟無障礙權限"
            btnAction.setOnClickListener {
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            }
        }
    }
}
