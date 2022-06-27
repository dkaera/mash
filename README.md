# Mash
This repository contains a sample Android Studio project to help me understand how Compose works in Android.

## Principles of Architecture:
Going forward, the project plans to use Clean Architecture to manage intercations with all layers. Since we have no business logic it was simplified by using a singleton instance of Graph to provide access to ViewModels. It allows us to easily migrate to dependency injection (Hilt or something else) in the future.
Firebase Auth uses to keep user personal data and auth flow.
Firebase Firestore uses to keep user data, which allows us to get user history even app was deleted from the device.

## Troubleshooting:
If you faced with next error
Android Gradle plugin requires Java 11 to run. You are currently using Java 1.8.
You only need to change Gradle JDK, that keeps on your IDE settings.
Build, Execution, Deployment -> Build Tools -> Gradle

Select: ---> Gradle JDK : JDK 11
