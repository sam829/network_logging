# Network Logging Library

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/sam829/network_logging/blob/master/LICENSE)

[![Release](https://jitpack.io/v/sam829/network_logging.svg)](https://jitpack.io/#sam829/network_logging)

The Network Logging Library is a lightweight Android library for debugging network requests and responses. It provides an easy way to monitor network activity in your Android app, helping you with debugging and performance optimization.

## Features

- Log HTTP requests and responses, including headers and bodies.
- Integration with popular HTTP libraries like OkHttp.
- Uses material 3 dynamic color schemes

## Getting Started

### Prerequisites

- Android Studio with a project targeting Android API 28 (Pie) or higher.

### Installation

To include this library in your project, you can use JitPack.

1. Add the JitPack repository to your project's `build.gradle.kts` file:

   ```kotlin
   allprojects {
       repositories {
           // ...
           maven(url = "https://jitpack.io")
       }
   }
   ```

2. Add the library as a dependency in your app's `build.gradle.kts`:

   ```kotlin
   dependencies {
       implementation("com.github.sam829:network_logging:${latestVersion}")
   }
   ```

### Usage

1. Initialize the library in your application class:

   ```kotlin
   class MyApplication : Application() {
       override fun onCreate() {
           super.onCreate()
            AppTheme {
                LogSheet(materialTheme = MaterialTheme)
            }
       }
   }
   ```

2. Start logging network requests and responses:

    ```kotlin
   val client = OkHttpClient.Builder()
                    .addInterceptor(NetworkLoggingInterceptor(context))
                    .build()

   val retrofit = Retrofit.Builder()
                    .baseUrl("baseurl.com") // Set your base url here
                    .client(client)
                    .build()
   ```

## Contributing

Contributions are welcome! Feel free to open issues, create pull requests, or provide suggestions and feedback.

## License

This library is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
