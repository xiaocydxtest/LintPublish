package com.example.lint.mid

import android.view.View
import com.example.lint.library.LintTest

/**
 * @author xcc
 * @date 2025/1/5
 */
class Test {

    fun run(view: View) {
        LintTest
        view.fitsSystemWindows = true
    }
}