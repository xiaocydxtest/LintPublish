plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("com.android.lint")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

lint {
    htmlReport = true
    htmlOutput = file("lint-report.html")
    textReport = true
    absolutePaths = false
    ignoreTestSources = true
}

dependencies {
    val lintVersion = "30.0.0"
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:1.9.20")
    compileOnly("com.android.tools.lint:lint-api:${lintVersion}")
    // compileOnly("com.android.tools.lint:lint-checks:$lintVersion")
    testImplementation("com.android.tools.lint:lint-tests:$lintVersion")
}