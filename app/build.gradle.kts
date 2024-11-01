plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.se1731_houserentailproject_group1"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.se1731_houserentailproject_group1"
        minSdk = 25
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
    packaging {
        resources {
            excludes.add("META-INF/DEPENDENCIES") // Loại bỏ tệp META-INF/DEPENDENCIES
        }
    }
}

dependencies {
    implementation(libs.circleimageview)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Thêm phụ thuộc BCrypt
    implementation("org.mindrot:jbcrypt:0.4")
    // Thêm phụ thuộc Mail


    // Thêm phụ thuộc Sms
    implementation("com.twilio.sdk:twilio:8.20.0")
}
