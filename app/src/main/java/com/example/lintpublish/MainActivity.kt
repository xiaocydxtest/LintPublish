package com.example.lintpublish

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.fitsSystemWindows = false // 字面量false不用提示
        window.decorView.fitsSystemWindows = true
    }
}