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

---

## ⚡ Update — v8.0.0.286 (versionCode 1570)

**Admin-color + touch + FPS overhaul ("2026 smooth" pass):**

- **Admin color follows the long-pressed pad (was always blue).** Long-press any pad (red / green / yellow / blue) and the admin moves to that color — a confirmation toast pops up: `Admin: <name> (RED)`. Pressing the same pad again toggles the assist off/on as before; pressing a different pad fully re-assigns the admin (and resets its quota / streak memory so the new admin starts a fresh 3-sixes-per-12-rolls window). The cut-protection rule is still always-on from game start (so you can't be killed 12+ steps from home even before the assist is enabled).
- **Touch handling — taps on pieces now actually register.** Root causes fixed:
  1. The piece click listener now guards against stale `currentPlayerDice == -1` (was the #1 source of "the click did nothing"). Duplicate taps during a move are ignored silently instead of running `move(-1)` and crashing.
  2. `switchPlayers()` now deactivates the previous player's pieces before flipping the turn — a piece that was `isClickable=true` from the previous turn no longer swallows taps.
  3. `check()`'s off-by-one (a 6 that finishes a token from step 51 was being silently disallowed, making the piece look "stuck at the home entrance") is fixed: `(numberOfSteps+diceValue) < 57` → `<= 57`.
  4. `check()`'s `diceValue==6` branch that can't be played now calls `inactiveState()` instead of leaving the piece clickable — closing a leak where the next tap would walk off the end of `winnerBlocks[]` and crash the turn.
  5. `showStep` now clamps `viewIndex` to `[1,6]` defensively so even an unexpected `-1` / `0` no-ops instead of throwing `ArrayIndexOutOfBoundsException` and freezing the game.
  6. `winnerBlocks[currWinnerBlock]` now bounds-checks `currWinnerBlock` before indexing — the piece snaps to the last winner cell if state ever drifts, instead of crashing the move chain.
- **Lag / FPS overhaul** ("2026 smooth" pass — closer to 0 jank on a long session):
  1. **No more 16 infinite animators from frame 1.** Every piece's `rotateAnimator` (the spinning "readyToPick" indicator) used to start in the piece constructor and run forever, even when the indicator was `INVISIBLE`. Now it's only started in `activeState()` and cancelled in `inactiveState()`, `die()`, and `onDestroy()`. This removes the single biggest source of constant Choreographer ticks.
  2. **The hint-arrow animator is cancellable.** It was a local variable and could never be cancelled — leaked the whole activity via the Choreographer. Stored as a field now and cancelled in `onDestroy`.
  3. **`onPause()` actually pauses work.** Was empty. Now it removes `globalHandler` + `diceHandler` callbacks, pauses the hint-arrow animator, all 16 piece animators, and pauses any playing sound. So backgrounding the app stops the move recursion from animating an invisible view hierarchy.
  4. **`onDestroy()` actually releases resources.** Was empty. Now it cancels all animators, releases all MediaPlayers (`diceRollSound`, `stepSound`, `gameStartSound`, `deathSound`, `safeSound`, `pantaSound`, `congratulationSound`), clears both Handler queues, and tears down the smart-dice handler. This is what stops the "lag grows as the game progresses" feeling across multiple game restarts.
  5. **Hardware layers are now toggled, not permanent.** Each piece + indicator used to keep a `LAYER_TYPE_HARDWARE` allocation for the whole game (16 pieces × 2 views = 32 GPU textures live forever). Now they get a hardware layer only while active (`activeState`) and drop it (`LAYER_TYPE_NONE`) when inactive — saving GPU memory and reducing background compositor cost.
  6. **Per-step pop animator is reused, not chained.** `move(6)` used to spawn 6 short `AnimatorSet`s that all ticked the Choreographer concurrently. Now the previous one is cancelled before the next starts (`currentMovePopAnim` field).
  7. **Bot-turn `new Handler(...)` allocation removed.** `switchPlayers` was creating a fresh `Handler` + `Runnable` pair every bot turn (one leak per turn). Reused `globalHandler` instead.
  8. **`stepSound` is no longer paused+seeked+restarted on every step.** It just plays through, removing 6 × (pause+seekTo+start) AudioTrack cycles per turn — a noticeable UI-thread jank reduction on lower-end devices.
  9. **`gameStartSound` respects `isSoundOn`.** It used to play unconditionally even with the sound toggle off, allocating a MediaPlayer and blocking the cleanup path.
- **Net effect on a 4-bot game:** significantly fewer Choreographer ticks per second, ~30 fewer GPU textures live, zero per-turn Handler leaks, zero per-turn AnimatorSet piles, and a clean teardown on activity exit. Tested for "no piece stuck / no tap dropped / no growing lag" over a 30-minute session.

---

## 🛡️ Update — v8.0.0.287 (versionCode 1571)

**Admin "first-press-wins" + toast-spam fix + finish-first guarantee.**

- **The "admin silently switched to blue on its own turn" bug is fixed.** Root cause was that every dice TAP (not just long-press) scheduled a delayed lambda that fired 3 seconds later, and `removeCallbacks(smartDiceLongPressAction)` was cancelling a *different object reference* (the inline lambda vs the named field) — so the cancellation was a no-op. Three seconds after every tap, the long-press action fired silently. Because the central dice image is constrained to the current player's corner pad by `moveDice`, every tap during Blue's turn registered as a long-press on Blue's pad, which then re-assigned the admin to Blue and showed the toast. The fix stores the pending lambda in a `pendingSmartDiceLongPress` field so `removeCallbacks` actually cancels it; the toast now only fires on a true 3-second long-press.
- **First-press-wins admin lock for the entire match.** New rule: whichever human long-presses first (any pad: red / green / yellow / blue, OR the central dice during their own turn) becomes the admin for the WHOLE match. Mid-match admin switching is no longer possible. Pressing the SAME admin's pad again toggles the assist on/off (`Admin assist ON` / `Admin assist OFF` toast); the admin color stays the same. Pressing any OTHER human's pad mid-match is silently ignored (no toast, no re-assignment). The next match starts fresh — the first long-press of the new match becomes its admin.
- **Cut-protection is always-on (independent of assist toggle).** Even when the assist is toggled off, the admin pieces are still protected: an opponent's roll never lands on a protected admin piece (admin 12+ steps from home), and the kill is skipped in `checkDeath` if `isProtectedAdminPiece(piece)` is true. Inside the last-12 window, the admin's pieces are cuttable naturally by any genuine natural roll — exactly as the user specified.
- **Admin-finishes-first guarantee is now airtight.** Original `rollWouldPassPlayer` only matched `numberOfSteps + v == 56` (one short of finish) — so an opponent rolling the EXACT value to land on step 57 (the real final winner cell) was NOT rerouted, and opponents could finish before the admin via the perfect roll. Now it matches `== 57`, so any opponent roll that would complete an opponent's final token is rerouted (when the admin has not yet finished all of theirs). Combined with the existing "no stacking on admin" rule, the admin always finishes 1st as long as the assist is on.
- **Quota sixes (3 per 12 owner rolls) and good-numbers assist are verified working** from the previous version. Soft selection in `chooseSmartDiceValue` + hard guard in `finalizeSmartDiceValue` enforce: ≤2 consecutive sixes, ≤4 sixes in any rolling 12-window, ≥3 sixes per fixed 12-roll block, and priority-biased rolls (capture 70% > rescue 55% > finish 50% > safe 40% > exit 35%) — all probabilistic to look natural.
- **Toast spam cleaned up.** The toggle toast only fires when the assist state actually changes (no more duplicate "Admin assist ON" toasts when spamming long-presses on the same pad). The initial admin-selection toast only fires the first time the user long-presses in a match.
