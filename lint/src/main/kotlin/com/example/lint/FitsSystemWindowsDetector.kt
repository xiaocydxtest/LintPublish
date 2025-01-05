/*
 * Copyright 2023 xiaocydx
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.lint

import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Detector.UastScanner
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Incident
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression

/**
 * @author xcc
 * @date 2025/1/5
 */
@Suppress("UnstableApiUsage")
internal class FitsSystemWindowsDetector : Detector(), UastScanner {

    override fun getApplicableMethodNames() = listOf("setFitsSystemWindows")

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        context.report(Incident(context, ISSUE).message("not call setFitsSystemWindows").at(node))
    }

    companion object {
        val ISSUE = Issue.create(
            id = "FitsSystemWindowsId",
            briefDescription = "FitsSystemWindows",
            explanation = """
                view.fitsSystemWindows = true
                view.fitsSystemWindows = true
                """,
            implementation = Implementation(FitsSystemWindowsDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )
    }
}