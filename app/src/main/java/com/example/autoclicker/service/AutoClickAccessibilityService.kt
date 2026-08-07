package com.example.autoclicker

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class AutoClickAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val rootNode = rootInActiveWindow ?: return

        // 1. 最優先：點擊「獲得金幣」
        val coinNodes = rootNode.findAccessibilityNodeInfosByText("獲得金幣")
        if (!coinNodes.isNullOrEmpty()) {
            val node = coinNodes[0]
            if (node.isClickable || node.parent?.isClickable == true) {
                (if (node.isClickable) node else node.parent)?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                return
            }
        }

        // 2. 第二優先：點擊「60 獲得」
        val get60Nodes = rootNode.findAccessibilityNodeInfosByText("60 獲得")
        if (!get60Nodes.isNullOrEmpty()) {
            val node = get60Nodes[0]
            if (node.isClickable || node.parent?.isClickable == true) {
                (if (node.isClickable) node else node.parent)?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                return
            }
        }

        // 3. 第三優先：點擊「跳過」
        val skipKeywords = listOf("跳過", ">l", "▸")
        for (keyword in skipKeywords) {
            val skipNodes = rootNode.findAccessibilityNodeInfosByText(keyword)
            if (!skipNodes.isNullOrEmpty()) {
                val node = skipNodes[0]
                if (node.isClickable || node.parent?.isClickable == true) {
                    (if (node.isClickable) node else node.parent)?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    return
                }
            }
        }

        // 4. 最後優先：點擊「×」或「關閉」，並計數
        val closeKeywords = listOf("×", "X", "關閉")
        for (keyword in closeKeywords) {
            val nodes = rootNode.findAccessibilityNodeInfosByText(keyword)
            if (!nodes.isNullOrEmpty()) {
                val node = nodes[0]
                if (node.isClickable || node.parent?.isClickable == true) {
                    (if (node.isClickable) node else node.parent)?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    // **關鍵：通知懸浮窗增加計數**
                    FloatingWindowService.updateCount()
                    return
                }
            }
        }
    }

    override fun onInterrupt() {}
}
