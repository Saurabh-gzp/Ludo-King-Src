# Ludo King Clone — Fixed & Building App

This repository contains the **complete fixed source code** of the
[Ludo King Clone](https://github.com/Vinaykpro/Ludo_King_Clone) Android app,
placed inside the **`ludo_src/`** folder.

The source has been repaired and **successfully builds** into installable APKs
(debug + release) with Android Gradle Plugin 8.10.1 / Gradle 8.11.1.

## 📁 Repository Structure

```
├── ludo_src/          ← Complete app source code (fixed)
│   ├── app/           ← Main application module (Java, resources, manifest)
│   ├── build.gradle   ← Root build script
│   ├── settings.gradle← Project settings
│   ├── gradle/        ← Gradle wrapper configuration
│   ├── gradlew        ← Gradle wrapper (Linux/Mac)
│   └── gradlew.bat    ← Gradle wrapper (Windows)
└── README.md
```

## 🔧 Fixes Applied

| # | Issue | Fix |
|---|-------|-----|
| 1 | Corrupted/truncated `drawable/innercircle.png` (broken IDAT chunk — AAPT2 crash) | Regenerated the 150×150 anti-aliased circle programmatically |
| 2 | 316 other PNG resources re-validated through PIL (structural safety) | Re-encoded losslessly, pixels preserved |
| 3 | `switch/case R.id.*` — "constant expression required" (48 errors on AGP 8) | Added `android.nonFinalResIds=false` in `gradle.properties` |
| 4 | AGP 8 requires `namespace` (manifest `package` attribute removed) | Added `namespace 'ludo.king.my'` in `app/build.gradle` |
| 5 | Dead `jcenter()` repository | Removed; now uses `google()` + `mavenCentral()` only |
| 6 | Old AGP 7.0.2 / Gradle 7.2 not compatible with modern JDK | Upgraded to AGP 8.10.1 + Gradle 8.11.1 |
| 7 | Content overlapped the device status bar | Added system-bar inset handling while keeping status and navigation bars visible |
| 8 | Main home-screen Ludo image was oversized | Reduced the image to approximately 65% of its previous effective width |

## 🛠️ How to Build

```bash
cd ludo_src
./gradlew assembleDebug     # → app/build/outputs/apk/debug/app-debug.apk
./gradlew assembleRelease   # → app/build/outputs/apk/release/app-release-unsigned.apk
```

**Requirements:** JDK 17+ (tested on JDK 21), Android SDK platform 36 + build-tools 36.0.0

**App info:** `ludo.king.my` · versionCode 1562 · versionName 8.0.0.278 · minSdk 21 · targetSdk 36

## 📌 Notes

- `local.properties` is not included (auto-generated; set your `sdk.dir` there).
- Original project by [Vinaykpro](https://github.com/Vinaykpro/Ludo_King_Clone).
