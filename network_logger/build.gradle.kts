plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("maven-publish")
}

group = "com.sam"
version = "1.0.0"

android {
    namespace = "com.sam.network_logger"
    compileSdk = 33

    defaultConfig {
        minSdk = 28

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures { // Enables Jetpack Compose for this module
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.4"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")

    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    val lifecycleVersion = "2.6.2"
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycleVersion")
    // ViewModel utilities for Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")
    // Saved state module for ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycleVersion")
    // Annotation processor
    kapt("androidx.lifecycle:lifecycle-compiler:$lifecycleVersion")


    val roomVersion = "2.5.2"
    // FOR ROOM
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$roomVersion")

    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.google.code.gson:gson:2.9.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    compilerOptions.freeCompilerArgs.add("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
    compilerOptions.freeCompilerArgs.add("-opt-in=androidx.compose.foundation.ExperimentalFoundationApi")
}

publishing {
    publications {
        create<MavenPublication>("NetworkLogger") {
            groupId = "com.sam"
            artifactId = "network-logger"
            version = "1.0.0"

            pom {
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("sam829")
                        name.set("Sam Macwan")
                        email.set("macwansaumya2@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/sam829/network_logging.git")
                    developerConnection.set("scm:git:ssh://github.com/sam829/network_logging.git")
                }
            }

            afterEvaluate {
                from(components["release"])
            }
        }
    }

    repositories {
        maven {
            name = "MavenCentral"
            /*url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = project.findProperty("ossrhUsername")?.toString()
                    ?: System.getenv("OSSRH_USERNAME")
                password = project.findProperty("ossrhPassword")?.toString()
                    ?: System.getenv("OSSRH_PASSWORD")
            }*/
            url = uri("https://repo1.maven.org/maven2/")
            credentials {
                username = project.findProperty("mavenCentralUsername")?.toString()
                password = project.findProperty("mavenCentralPassword")?.toString()
            }
        }
    }
}