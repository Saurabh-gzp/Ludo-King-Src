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

---

## ⚖️ Update — v8.0.0.281 (versionCode 1565)

**Admin rules + real-ludo three-six rule:**

- **3-six void rule (all players):** third consecutive six shows on the dice but the move is voided and the turn passes — exactly like real ludo.
- **Admin never gets 3 consecutive sixes** — the roll engine caps six streaks before they can even appear.
- **Admin cut rules:** admin tokens can be cut naturally ONLY within the last 12 steps before home; farther out (12+) they can never be killed.
- **Admin always finishes 1st:** while the owner has not passed, the roll that would complete an opponent's FINAL token never appears (dice rerolls naturally). Once the owner passes, the rest of the game runs normally.
- **3-second long press** (was 5s) to toggle the smart dice on/off on the pressed pad.

---

## 🛡️ Update — v8.0.0.282 (versionCode 1566)

**Admin protection bug fix + guaranteed six quota:**

- **Cut-protection now ALWAYS active:** the admin (phone owner / Player 1) is locked from game start — protections never depend on whether the long-press assist was toggled. (This fixed the reported bug: an opponent's 5 could kill an admin token 12+ steps from home when the assist hadn't been activated that game.)
- **Guaranteed six quota (validated over 300k simulated rolls):** every fixed block of 12 owner rolls contains at least 3 sixes — 0 of 25,000 blocks fell short. Sixes arrive spread out, at useful moments, never 3 in a row (max 2 consecutive), and never more than 4 in any rolling 12-window.
- Owner numbers "as per need": the six bias fires early when exit/capture/finish is available.

---

## 🔧 Update — v8.0.0.283 (versionCode 1567)

**Stacking bug fix + assist now always on for the admin:**

- **No more enemy stacking on protected admin tokens:** earlier an opponent could LAND on an admin token it was not allowed to cut (12+ steps from home) — both tokens ended up stacked on one square. Now such a landing never happens: the opponent's dice quietly rerolls to a value that lands elsewhere. Inside the last-12 window the admin token can still be cut normally.
- **Admin engine starts ON:** the quota (3 sixes per 12 rolls, spread out, max 2 consecutive) and good-number assist now work from game start — no long press needed. The 3s press toggles the assist off/on as before. (This was the reason the six quota seemed "not working": the engine sat behind the press toggle.)

---

## ⚡ Update — v8.0.0.284 (versionCode 1568)

**Major dice-distribution fix + lag fix:**

- **Opponents' natural dice restored (cut bug fixed):** the admin-priority filter was replacing EVERY opponent roll with a random safe value — even perfectly safe natural rolls were discarded. That is why opponents never seemed to roll the number needed to cut an admin token sitting within the last-12 window. Now the roll is only rerolled when it is genuinely unsafe (would pass the admin's final token, or land on a protected admin token). Everything else stays natural — cuts on the admin happen normally again.
- **Lag fix:** removed leftover debug Toast spam inside the bot decision loop that flooded the UI thread on every bot roll.
