plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.lintpublish"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.lintpublish"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(CommonLibs.`androidx-core-ktx`)
    implementation(CommonLibs.`androidx-appcompat`)
    implementation(CommonLibs.`androidx-transition`)
    implementation(CommonLibs.`androidx-constraintlayout`)
    implementation(CommonLibs.material)
    // 怎么依赖传递？
    // 怎么发布依赖？
    lintChecks(project(":lint"))
}