import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    kotlin("plugin.serialization") version "1.9.22"
    alias(libs.plugins.kotlin.android)
}

val localProperties = Properties()
localProperties.load(rootProject.file("local.properties").inputStream())

val SUPABASE_URL: String = localProperties.getProperty("SUPABASE_URL", "")
val SUPABASE_ANON_KEY: String = localProperties.getProperty("SUPABASE_ANON_KEY", "")
val RESEND_API_KEY: String = localProperties.getProperty("RESEND_API_KEY", "")

val REDIRECT_URI: String = localProperties.getProperty("REDIRECT_URI", "")
val WEB_CLIENT_ID: String = localProperties.getProperty("WEB_CLIENT_ID", "")
val PROJECT_URL: String = localProperties.getProperty("PROJECT_URL", "")
val PASSWORD_RESET_REDIRECT_URI: String = localProperties.getProperty("PASSWORD_RESET_REDIRECT_URI", "")



android {
    namespace = "com.example.housing"
    compileSdk = 36

    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        applicationId = "com.example.housing"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


        buildConfigField("String", "SUPABASE_URL", "\"$SUPABASE_URL\"")
        buildConfigField("String", "SUPABASE_ANON_KEY", "\"$SUPABASE_ANON_KEY\"")
        buildConfigField("String", "RESEND_API_KEY", "\"$RESEND_API_KEY\"")
        buildConfigField("String", "REDIRECT_URI", "\"$REDIRECT_URI\"")
        buildConfigField("String", "WEB_CLIENT_ID", "\"$WEB_CLIENT_ID\"")
        buildConfigField("String", "PROJECT_URL", "\"$PROJECT_URL\"")
        buildConfigField("String", "PASSWORD_RESET_REDIRECT_URI", "\"$PASSWORD_RESET_REDIRECT_URI\"")
    }

    signingConfigs {
        create("release") {
            storeFile = file("../new-release-key.keystore")
            storePassword = "887845"
            keyAlias = "newkey"
            keyPassword = "887845"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        getByName("debug") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    implementation("com.google.android.gms:play-services-auth:21.4.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    testImplementation("junit:junit:4.13.2")


    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test:core:1.5.0")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
}
