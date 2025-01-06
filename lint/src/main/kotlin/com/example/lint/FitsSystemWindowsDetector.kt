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
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Incident
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.isFalseLiteral

/**
 * @author xcc
 * @date 2025/1/5
 */
@Suppress("UnstableApiUsage")
internal class FitsSystemWindowsDetector : Detector(), SourceCodeScanner {

    override fun getApplicableMethodNames() = listOf("setFitsSystemWindows")

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        if (!context.evaluator.isMemberInClass(method, "android.view.View")) return
        if (node.valueArguments[0].isFalseLiteral()) return
        // View.setFitsSystemWindows(fitSystemWindows)的实参为字面量true或者变量
        context.report(
            Incident(context, ISSUE)
                .message("`fitSystemWindows = true`有版本兼容问题，谨慎使用")
                .at(node)
        )
    }

    companion object {
        val ISSUE = Issue.create(
            id = "FitsSystemWindows",
            briefDescription = "FitsSystemWindows",
            explanation = """
                Android 11以下，`fitSystemWindows = true`会让同级`child2`没有`WindowInsets`分发：
                ```
                val child1 = View(context)
                val child2 = View(context)
                parent.addView(child1)
                parent.addView(child2)
                
                child1.fitsSystemWindows = true // 消费systemWindowInsets并设置`paddings`
                ViewCompat.setOnApplyWindowInsetsListener(child2) { v, insets->
                    // child2的OnApplyWindowInsetsListener不会触发
                    insets
                }
                ```
                
                建议用OnApplyWindowInsetsListener代替`fitSystemWindows = true`，\
                确保同级`child2`有`WindowInsets`分发：
                ```
                ViewCompat.setOnApplyWindowInsetsListener(child1) { _, insets->
                    // 如果只需要系统栏间距，那么就获取systemBars()的数值
                    val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                    view.setPadding(0, statusBars.top, 0, systemBars.bottom)
                    insets // 返回传入的insets，不做消费处理
                }
                ```
                
                问题的原因是`parent`对`child1`分发`insets`，`child1`返回消费结果并赋值给`insets`，
                使得`insets.isConsumed()`为`true`退出循环，`parent`不会对`child2`分发`insets`：
                ```
                // Android 11以下的分发逻辑
                public abstract class ViewGroup extends View {
                
                    @Override
                    public WindowInsets dispatchApplyWindowInsets(WindowInsets insets) {
                        insets = super.dispatchApplyWindowInsets(insets);
                        if (!insets.isConsumed()) {
                            final int count = getChildCount();
                            for (int i = 0; i < count; i++) {
                                 insets = getChildAt(i).dispatchApplyWindowInsets(insets);
                                 if (insets.isConsumed()) {
                                    break;
                                 }
                            }
                        }
                        return insets;
                    }
                }
                ```
                """,
            implementation = Implementation(FitsSystemWindowsDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )
    }
}