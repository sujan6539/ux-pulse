# ux-pulse

Our Android Analytics Framework, uxPulse, is a comprehensive solution designed to seamlessly track and analyze user interactions within Android applications. It provides robust tools and features to gather detailed insights into user actions, session information, and device metadata to optimize user experiences and drive engagement.

## Gradle setup
Step 1: Add the JitPack repository to your build file
```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}

```
Step 2. Add the dependency
```kotlin
dependencies {
	        implementation 'com.github.sujan6539:ux-pulse:v2.0.3'
	}
```
## Maven setup
Step 1: Add the JitPack repository to your build file
```kotlin
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```
Step 2: Add the dependency
```kotlin
	<dependency>
	    <groupId>com.github.sujan6539</groupId>
	    <artifactId>ux-pulse</artifactId>
	    <version>v2.0.3</version>
	</dependency>
```

# Setting Up Firebase Realtime Database for uxPulse Framework

UxPulse uses Firebase Realtime Database, as a server database to store all events, so follow these steps:

## Step 1: Create a Firebase Project

1. **Go to the Firebase Console:**
   - Open your web browser and navigate to [Firebase Console](https://console.firebase.google.com/).

2. **Add a New Project:**
   - Click on the "Add project" button.
   - Enter your project name and follow the on-screen instructions to configure your project settings.
   - Click "Create project" and wait for Firebase to set up your project.

## Step 2: Add Your Android App to the Project

1. **Register Your App:**
   - In the Firebase Console, click on the Android icon to add an Android app to your project.
   - Enter your app's package name (e.g., `com.yourcompany.yourapp`).
   - Optionally, you can add a nickname for your app and the SHA-1 key (optional for basic setup).

2. **Download the `google-services.json` File:**
   - Click "Register app" and then click "Download `google-services.json`".
   - Save the `google-services.json` file to your computer.

3. **Add the `google-services.json` File to Your Project:**
   - Move the downloaded `google-services.json` file into the `app` directory of your Android project.

## Initializing UxPulse Framework
```kotlin
     FirebaseApp.initializeApp(applicationContext)
     var applicationSession = object : ApplicationSession {
            override fun getApplicationContext(): Context {
                return applicationContext
            }

        }

    // set instantPushEvent to true, if you want instant push to the server. 
    val uxPulseConfig = UxPulseConfig(
            apiKey = RANDOM_API_KEY,
            projectId = applicationContext.packageName,
            version = APP_VERSION,
            instanceId = YOUR_INSTANCE_ID
        )

    FirebaseApp.initializeApp(this);
    usPulseTracker = UxPulseProvider.initialize(
            applicationSession,
            uxPulseConfig,
        )
```

