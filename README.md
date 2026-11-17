# Root App Killer

A focused, root-only Android utility: it lists installed apps, shows which
ones have a background process right now, and force-stops the ones you
pick. Nothing else — no RAM booster, no cache/junk cleaner, no "optimizer"
claims.

## How it works

- **Root detection** — `root/RootManager.kt` opens a shell through
  [libsu](https://github.com/topjohnwu/libsu), which triggers whichever
  root solution is installed (Magisk, KernelSU, or APatch) to show its own
  grant prompt. The app never tries to script or auto-approve that prompt.
- **Manager identification** — checked first by looking for the known
  manager app package (`com.topjohnwu.magisk`, `me.weishu.kernelsu`,
  `me.bmax.apatch`), then falls back to probing for `magisk` / `ksud` /
  `apd` binaries on `PATH` if root was granted but no manager app is
  visible.
- **App list** — `data/AppRepository.kt` enumerates installed apps via
  `PackageManager`, then reads `ps -A` and
  `dumpsys window | grep mCurrentFocus` through the root shell to mark
  each app **Foreground**, **Background**, or **Stopped**.
- **Force stop** — `data/ForceStopEngine.kt` is the single chokepoint that
  issues `am force-stop <package>` as root. It independently re-checks
  that the target isn't protected and isn't the foreground app before
  running anything, regardless of what the UI already filtered.
- **Protection** — `data/ProtectedPackages.kt` blocks the current
  launcher (resolved at runtime), the installed root-manager app, this
  app itself, and a fixed list of AOSP system packages (System UI,
  Settings, telephony, Bluetooth, NFC, Play services/Store, the stock
  keyboard). Protected apps never get a selectable checkbox or a working
  Stop button.

## What it deliberately does not do

- Never touches the foreground app.
- Never force-stops anything on the protected list, even if selected.
- Makes no claim that stopping an app frees RAM permanently — Android
  will relaunch most background apps on its own schedule regardless.
- No auto-repeat / scheduled killing loop.

## Project structure

```
app/src/main/java/com/io/root/killer/
├── MainActivity.kt
├── root/
│   ├── RootManagerType.kt     enum of Magisk / KernelSU / APatch + RootState
│   └── RootManager.kt         shell access, manager detection, command runner
├── data/
│   ├── AppInfo.kt             AppInfo + RunState + StopResult models
│   ├── ProtectedPackages.kt   the non-negotiable protection list
│   ├── AppRepository.kt       installed-app enumeration + running-state probes
│   └── ForceStopEngine.kt     the only place `am force-stop` is called
└── ui/
    ├── MainViewModel.kt       UI state, selection, refresh, stop actions
    ├── MainScreen.kt          Compose screen: status, tabs, list, actions
    └── theme/                 dark Material 3 theme
```

## Requirements

- Android Studio (Ladybug or newer recommended).
- A rooted device/emulator running Android 8.0 (API 26) or higher, with
  Magisk, KernelSU, or APatch granting root to apps.
- On first launch the app will prompt for root through whichever manager
  is installed; granting it is required for the app list and force-stop
  features — the UI explains this and offers a Retry if it's denied.

## Build

```
./gradlew assembleDebug
```

The debug build installs as `com.io.root.killer.debug` so it can sit
alongside a release build without overwriting it.
