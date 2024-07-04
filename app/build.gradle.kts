plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.sp.uxpulsedemo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.sp.uxpulsedemo"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }


    sourceSets {
        getByName("main") {
            print("Hello")
            java.srcDirs("src/main/java", "$buildDir/generated/source/kotlin")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(project(":uxPulse"))
    implementation(files("/Users/sujanprajapati/Downloads/adobeMobileLibrary-4.18.2.jar"))
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.google.firebase:firebase-analytics:22.0.2")

    implementation(platform("com.adobe.marketing.mobile:sdk-bom:3.+"))
    implementation("com.adobe.marketing.mobile:core")
    implementation("com.adobe.marketing.mobile:identity")
    implementation("com.adobe.marketing.mobile:analytics")
}
