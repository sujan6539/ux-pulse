import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Properties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
    id("kotlin-kapt")
}

// Read local.properties file
val localProps = Properties()
localProps.load(rootProject.file("local.properties").inputStream())

android {
    namespace = "com.sp.uxpulse"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.work.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    val room_version = "2.6.1"

    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation ("androidx.room:room-ktx:2.6.1")

    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:33.1.1"))

    // Add the dependency for the Realtime Database library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-database")

    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.logging.interceptor)
}

group = "com.github.sujan6539"
version = "1.0.0"


afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.github.sujan6539"
                artifactId = "ux-pulse"
                version = version
            }
        }
    }
}

// Ensure proper repositories configuration
publishing {
    repositories {
        mavenLocal() // For local publishing
        maven {
            url = uri("https://jitpack.io")
            credentials {
                username = localProps.getProperty("GITHUB_USERNAME") // or use your GitHub username directly
                password = localProps.getProperty("GITHUB_TOKEN") ?: ""// or use your GitHub personal access token
            }
        }
    }
}