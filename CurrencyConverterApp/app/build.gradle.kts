plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.currencyconverterapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.currencyconverterapp"
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
            buildConfigField("String", "FreeCurrencyAPI",
                "\"fca_live_W31NByq2i3M9trpRSu9pRqRP3GUfAus9N1Mkkxsv\"");
        }
        debug{
            buildConfigField("String", "FreeCurrencyAPI",
                "\"fca_live_W31NByq2i3M9trpRSu9pRqRP3GUfAus9N1Mkkxsv\"");
        }
    }
    buildFeatures {
        buildConfig = true// Enable BuildConfig feature
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    implementation ("com.squareup.okhttp3:okhttp:4.9.3")
}