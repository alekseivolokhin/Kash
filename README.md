# Kash

Personal finance tracker built with Kotlin Multiplatform and Compose Multiplatform for Android and iOS.

## Features

- **Transactions** — add, edit, delete income and expenses with categories, dates, and comments
- **Categories** — preset and custom categories with icons and colors
- **Bank Import** — CSV and PDF statement import with auto-detection and smart categorization
- **Statistics** — monthly charts, top spending categories, month-over-month comparison, balance overview
- **Settings** — currency selection, dark/light theme, CSV export

## Tech Stack

- **Kotlin Multiplatform** — shared business logic across platforms
- **Compose Multiplatform** — shared UI
- **SQLDelight** — local database
- **Koin** — dependency injection
- **Decompose** — navigation
- **Clean Architecture** — MVVM + UDF

## Build

```bash
# Android
./gradlew :androidApp:installDebug

# iOS
# Open iosApp/.xcodeproj in Xcode and run

# Tests
./gradlew :shared:allTests
```
