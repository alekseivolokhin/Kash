# iOS App Setup Design

**Date:** 2026-03-28
**Topic:** Create `iosApp/` Xcode project for KMP + Compose Multiplatform

## Context

The `composeApp` Gradle module already targets iOS (iosX64, iosArm64, iosSimulatorArm64) and exports a static framework named `ComposeApp`. The entry point `mainViewController()` is defined in `iosMain`. The only missing piece is an Xcode project (`iosApp/`) to run the app on a simulator or device.

## Design

### Entry Point: SwiftUI `@main`

Modern SwiftUI-based entry point. `iOSApp.swift` declares the `@main` App struct, `ContentView.swift` wraps the KMP `mainViewController()` via `UIViewControllerRepresentable`.

### File Structure

```
iosApp/
  iosApp.xcodeproj/
    project.pbxproj          — Xcode project definition
  iosApp/
    iOSApp.swift             — @main SwiftUI App entry point
    ContentView.swift        — wraps mainViewController() from ComposeApp framework
    Info.plist               — app metadata (bundle ID, display name, iOS version)
```

### Build Integration

The Xcode project includes a **Run Script Build Phase** that runs:
```bash
cd "$SRCROOT/.."
./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
```

This Gradle task:
1. Compiles the KMP shared code for the active architecture
2. Places `ComposeApp.framework` in the location Xcode expects
3. Signs the framework

`FRAMEWORK_SEARCH_PATHS` in the Xcode project is set to `$(SRCROOT)/..` so Xcode can find the framework.

### App Parameters

| Parameter | Value |
|---|---|
| Bundle ID | `com.volokhinaleksey.kash` |
| Deployment target | iOS 15.0 |
| Supported devices | iPhone + iPad |
| Swift version | 5.0 |
| Framework | ComposeApp (static, embedded) |

### Swift Source Files

**iOSApp.swift**
```swift
import SwiftUI

@main
struct iOSApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
```

**ContentView.swift**
```swift
import UIKit
import SwiftUI
import ComposeApp

struct ContentView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        mainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
```

## What is NOT in scope

- Push notifications, deep links, or native iOS features
- Custom AppDelegate
- CocoaPods or Swift Package Manager dependencies
