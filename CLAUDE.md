# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Android application currently configured as a foundation for Kotlin Multiplatform Mobile (KMM), but presently Android-only (no shared module or iOS targets yet). Built with Jetpack Compose and Material Design 3.

- **Package:** `com.example.kmm_jcmm_test`
- **Min SDK:** 26 | **Target/Compile SDK:** 36
- **Kotlin:** 2.0.21 | **AGP:** 9.0.1 | **Gradle:** 9.2.1 (JVM toolchain 21)

## Common Commands

```bash
# Build
./gradlew app:assembleDebug
./gradlew app:assembleRelease

# Unit tests (local JVM)
./gradlew app:testDebugUnitTest

# Instrumented tests (requires connected device/emulator)
./gradlew app:connectedDebugAndroidTest

# Lint
./gradlew app:lint
./gradlew app:lintFix

# Install on device
./gradlew app:installDebug

# Full clean build
./gradlew clean app:build
```

## Architecture

**Single module** (`app`). Entry point is `MainActivity` using `setContent {}` with Compose.

**UI structure:**
- `MainActivity.kt` — single Activity with edge-to-edge, delegates entirely to Compose
- `ui/theme/` — Material 3 theme (Color, Theme, Type); supports dynamic color on Android 12+

**Dependency management:** Version catalog at `gradle/libs.versions.toml` — add all new dependencies there, not inline in build files.

## Key Conventions

- **RepositoriesMode.FAIL_ON_PROJECT_REPOS** is set — declare repositories only in `settings.gradle.kts`
- `android.nonTransitiveRClass=true` — resource references must use the correct module's R class
- Java 11 source/target compatibility in the `app` module

## If Adding KMM (Shared Module)

To evolve this into true KMM, add a `shared` module with `commonMain`/`androidMain`/`iosMain` source sets and apply `kotlin("multiplatform")`. Register it in `settings.gradle.kts` with `include(":shared")` and add the `kotlin-multiplatform` plugin alias in the root `build.gradle.kts`.
