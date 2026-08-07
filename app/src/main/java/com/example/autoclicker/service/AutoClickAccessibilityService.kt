package com.example.autoclicker

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class AutoClickAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val rootNode = rootInActiveWindow ?: return

        // 1. 最優先：在主畫面時，點擊「獲得金幣」來開啟獎勵
        val coinNodes = rootNode.findAccessibilityNodeInfosByText("獲得金幣")
        if (!coinNodes.isNullOrEmpty()) {
            for (node in coinNodes) {
                if (node.isClickable || node.parent?.isClickable == true) {
                    val target = if (node.isClickable) node else node.parent
                    target?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    return
                }
            }
        }

        // 2. 第二優先：彈出選單出現時，點擊中間的「60 獲得」
        val get60Nodes = rootNode.findAccessibilityNodeInfosByText("60 獲得")
        if (!get60Nodes.isNullOrEmpty()) {
            for (node in get60Nodes) {
                if (node.isClickable || node.parent?.isClickable == true) {
                    val target = if (node.isClickable) node else node.parent
                    target?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    return
                }
            }
        }

        // 3. 第三優先：廣告播放中，尋找左上角的「跳過」或箭頭符號
        val skipKeywords = listOf("跳過", ">l", "▸")
        for (keyword in skipKeywords) {
            val skipNodes = rootNode.findAccessibilityNodeInfosByText(keyword)
            if (!skipNodes.isNullOrEmpty()) {
                for (node in skipNodes) {
                    if (node.isClickable || node.parent?.isClickable == true) {
                        val target = if (node.isClickable) node else node.parent
                        target?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                        return
                    }
                }
            }
        }

        // 4. 最後優先：廣告結束時，尋找右上角的「×」、「X」或「關閉」
        val closeKeywords = listOf("×", "X", "關閉")
        for (keyword in closeKeywords) {
            val nodes = rootNode.findAccessibilityNodeInfosByText(keyword)
            if (!nodes.isNullOrEmpty()) {
                for (node in nodes) {
                    if (node.isClickable) {
                        node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                        return
                    } else if (node.parent?.isClickable == true) {
                        node.parent.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                        return
                    }
                }
            }
        }
    }

    override fun onInterrupt() {}
}
