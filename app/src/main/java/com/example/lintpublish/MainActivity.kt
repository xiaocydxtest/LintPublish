package com.example.lintpublish

import android.app.Activity
import android.os.Bundle

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.fitsSystemWindows = false // 字面量false不用提示
        window.decorView.fitsSystemWindows = true
    }
}