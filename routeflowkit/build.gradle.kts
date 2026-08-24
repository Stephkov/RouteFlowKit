plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    `maven-publish`
}

group = providers.gradleProperty("GROUP").getOrElse("com.example.routeflowkit")
version = providers.gradleProperty("VERSION_NAME").getOrElse("1.0.0-SNAPSHOT")

android {
    namespace = "com.example.routeflowkit.library"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures { compose = true }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)

    // Compose (BOM)
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    compileOnly(libs.androidx.compose.ui.tooling)

    // Google Maps Compose — API dependency so consumers get transitive access
    implementation(libs.maps.compose)
    implementation(libs.play.services.maps)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                artifactId = "routeflowkit"
                pom {
                    name.set("RouteFlowKit")
                    description.set("Reusable route-flow UI components for Android Compose applications.")
                }
            }
        }
    }
}
