# mash
This repository contains a sample Android Studio project to help me understand how Compose works in Android.

Principles of Architecture:
Going forward, the project plans to use pure architecture principles. Right now it uses Jetpack Compose to represent the user interface and ViewModel to store business logic. I use a singleton instance of Graph to provide services, which provides easy access to domain layer interactions, which means we can easily transition to Hilt in the future.
Firebase Auth uses to keep user personal data and auth flow.
Firebase Firestore uses to keep user data, which allows us to get user history even app was deleted from the device.

Troubleshooting:
If you faced with next error
Android Gradle plugin requires Java 11 to run. You are currently using Java 1.8.
You only need to change Gradle JDK, that keeps on your IDE settings.
Build, Execution, Deployment -> Build Tools -> Gradle

Select: ---> Gradle JDK : JDK 11
