<p align="center">
  <img src="web/assets/logo.svg" width="120" alt="PhoneIDE logo" />
</p>

<h1 align="center">PhoneIDE</h1>

<p align="center">
  <strong>A real Android IDE that runs on Android.</strong><br />
  Code editor, Linux toolchains, Gradle builds, a terminal and a 27-tool AI agent — in one universal APK.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/version-2.8.2--V7--Parvaaz-6C5CE7?style=flat-square" alt="Version 2.8.2-V7-Parvaaz" />
  <img src="https://img.shields.io/badge/Android-7.0%2B%20%C2%B7%20API%2024-3DDC84?style=flat-square&logo=android&logoColor=white" alt="Android 7.0+ / API 24" />
  <img src="https://img.shields.io/badge/Kotlin-2.0.21-7F52FF?style=flat-square&logo=kotlin&logoColor=white" alt="Kotlin 2.0.21" />
  <img src="https://img.shields.io/badge/ABI-arm64--v8a%20%7C%20armeabi--v7a-555555?style=flat-square" alt="arm64-v8a and armeabi-v7a" />
  <img src="https://img.shields.io/badge/license-Proprietary-ED4245?style=flat-square" alt="Proprietary license" />
  <a href="https://t.me/phoneide"><img src="https://img.shields.io/badge/Telegram-%40phoneide-26A5E4?style=flat-square&logo=telegram&logoColor=white" alt="Telegram channel" /></a>
</p>

<p align="center">
  <a href="https://github.com/Sandeepbedia/PhoneIDE">GitHub</a> ·
  <a href="https://t.me/phoneide">Telegram channel</a> ·
  <a href="https://t.me/adrodx">@adrodx</a>
</p>

---

**PhoneIDE** is an on-device Android IDE for people who want a real development environment on the phone they already have. It is not a remote editor and it is not a toy: it installs a Linux userland, provisions a JDK, Android SDK, Build-Tools, Gradle, Kotlin and AGP, opens a genuine terminal, and runs Android Gradle builds locally.

The app is a single universal APK. No root is required. A PC is not required. Shizuku or root are optional accelerators for silent APK installs and broader log access.

> This README documents the current source tree: **2.8.2-V7-Parvaaz** (`versionCode 422`). Every implemented feature below is backed by code in this repository. Placeholders and Beta surfaces are called out explicitly — see [Project status](#project-status).

## At a glance

| | |
|---|---|
| **Version** | 2.8.2-V7-Parvaaz · versionCode 422 |
| **Package** | `com.io.phone.ide` |
| **Android** | 7.0+ / API 24 · targetSdk 35 · compileSdk 35 |
| **Architecture** | Universal APK: `arm64-v8a` + `armeabi-v7a` |
| **UI** | Jetpack Compose · Material 3 |
| **App built with** | Kotlin 2.0.21 · AGP 8.7.3 · Gradle 8.11 · JVM 11 · Compose BOM 2024.12.01 |
| **Linux guests** | Ubuntu 24.04 LTS · Termux bootstrap 2026.08.30 · Alpine 3.24.1 |
| **Project templates** | 10 |
| **AI agent** | 6 modes · 27 tools · 4 built-in providers + saved custom providers |
| **UI languages** | 11 |
| **License** | Proprietary, All Rights Reserved |

## Contents

- [Why PhoneIDE](#why-phoneide)
- [Feature tour](#feature-tour)
  - [Home and project flow](#home-and-project-flow)
  - [Editor](#editor)
  - [Code intelligence](#code-intelligence)
  - [AI agent](#ai-agent)
  - [Terminal and Linux guests](#terminal-and-linux-guests)
  - [Toolchain manager](#toolchain-manager)
  - [Build system](#build-system)
  - [Preview Studio](#preview-studio)
  - [Version Control](#version-control)
  - [GitHub Manager](#github-manager)
  - [APK Center](#apk-center)
  - [JKS Manager and release signing](#jks-manager-and-release-signing)
  - [Icon Manager](#icon-manager)
  - [AppLog and IDE log](#applog-and-ide-log)
  - [Templates and new projects](#templates-and-new-projects)
  - [Search, files and project export](#search-files-and-project-export)
  - [Appearance and localization](#appearance-and-localization)
  - [Storage, updates and system settings](#storage-updates-and-system-settings)
  - [Privacy and permissions](#privacy-and-permissions)
  - [Developer tools](#developer-tools)
- [AI reference](#ai-reference)
- [Toolchain catalog](#toolchain-catalog)
- [Guest environments](#guest-environments)
- [Architecture](#architecture)
- [Build from source](#build-from-source)
- [Requirements and limitations](#requirements-and-limitations)
- [Project status](#project-status)
- [Security notes](#security-notes)
- [FAQ](#faq)
- [Credits and license](#credits-and-license)

---

## Why PhoneIDE

Most "IDE" apps on Android are text editors with a terminal attached. PhoneIDE is built around the things that actually decide whether a project can be compiled on a phone:

- **A real Linux userland under proot.** Ubuntu, Termux or Alpine, with a persistent home, package manager and shell.
- **A real toolchain manager.** JDK, cmdline-tools, Android SDK platforms, Build-Tools, platform-tools, Gradle, Kotlin, AGP, Flutter and four language servers — installed, versioned, verified, switched and repaired from inside the app.
- **A real Gradle pipeline.** Preflight checks, missing-package remedies, a build console with clickable diagnostics, background execution and signed APK output.
- **A real AI agent.** 27 tools with a project sandbox, per-capability permissions, diffs, numbered change sets and optional checkpoints — not a chat box that can only print code.
- **A real mobile workflow.** Pinch-zoom editor, file-tree drawer, tabbed buffers, per-project windows, split-screen, APK install/share, keystore management and clean ZIP export.

The result is a phone that can create a project, edit it, build it, sign it, install it and inspect its logs without another computer in the loop.

---

## Feature tour

### Home and project flow

The Home dashboard is the control room:

- **Local profile.** Display name and avatar, stored only on the device. There is no PhoneIDE account.
- **Pinned and recent projects.** Pin a project to the top, reopen recent work, or open any folder through the project picker.
- **Last build result.** The latest outcome per project — project, success or failure, duration or failure reason, and a relative timestamp — with rows sorted newest first. Tapping a row opens that project.
- **Storage overview.** Device storage used/free, plus the installed toolchain's footprint.
- **Quick actions.** New project, file manager, JKS Manager and Icon Manager, plus header shortcuts to search, GitHub, Version Control and Settings.
- **Home AI chat.** A separate app-wide conversation that is not attached to any project; tools and project context are off, and it opens in Chat mode.
- **Project picker / file manager.** Direct filesystem browsing rather than SAF URIs, because Gradle and proot need real paths. The picker recognises Gradle project roots and Android projects, and the same browser doubles as a file manager.
- **Per-project windows.** Off by default. When enabled, every project can keep its own Android task and window, so two projects can sit side by side in Android split-screen. Recents shows one card per project.
- **Safe deletion.** Deleting a project checks for an open window, unsaved edits and a running build first, closes live workspaces, then removes the project and its recents entry. It is permanent, not a recycle bin.
- **Clean ZIP export.** Export source and configuration without build output, IDE metadata, archives, generated binaries or signing secrets. A review sheet lists every excluded path so it can be forced back in. The project itself is never modified.

### Editor

The editor is built on **sora-editor 0.22.1**, pinned deliberately because it is the last release compiled to Java 11 bytecode.

- Mobile code-editor surface with line numbers, selection, magnifier, undo/redo and incremental analysis.
- Tabbed buffers, a floating file-tree drawer in portrait and a docked tree in landscape.
- Pinch-to-zoom text from **8sp to 32sp**, plus stepped sizes from 10sp to 24sp.
- Adjustable line spacing (1.0×, 1.15×, 1.3×, 1.5×), tab width 2/4/8, word wrap, indent guides, whitespace rendering, font ligatures and cursor animation.
- Auto-close brackets and quotes, auto-indent, smart backspace, format-on-paste, bold matching brackets, sticky scroll, scroll-past-end and autocomplete.
- **Auto-save modes:** Off, on tab switch, or on leaving the editor.
- Line-number gutter pinning, current-line highlight, generated-folder visibility, and a language-server toggle in the file-tree overflow menu.
- Optional **restore last project** on launch.

Syntax highlighting covers:

Kotlin, Java, Gradle/Groovy, JSON, XML/HTML/SVG, properties/INI/TOML, YAML, Markdown, C/C++, Python, JavaScript/TypeScript/JSX, CSS/SCSS/LESS, shell, SQL, Dart, Go, Rust, Dockerfile, Make/CMake, and a generic fallback.

### Code intelligence

PhoneIDE integrates four language servers through a shared LSP client:

| Language | Server |
|---|---|
| Kotlin | `fwcd/kotlin-language-server` 1.3.13 |
| Java | Eclipse JDT Language Server 1.38.0 |
| XML | Eclipse LemMinX 0.28.0 |
| Dart | Dart's analysis server, bundled with the Flutter SDK |

Completion, diagnostics, hover, go-to-definition, find-references and rename are supported where the server supports them. The AI agent can query the same indexes through `find_symbol`, `go_to_definition` and `find_references`.

**LSP is off by default.** It starts only when enabled from the file-tree overflow menu, because warming up the servers can trigger a Gradle classpath resolve and minutes of retries on a phone. The choice is remembered across projects and restarts.

### AI agent

PhoneIDE ships a full agent runtime, not just a chat panel.

#### Modes

| Mode | What it does | Can edit? | Autonomous? |
|---|---|---|---|
| **Chat** | Ask about the code. Nothing is changed. | No | No |
| **Build** | Full access: read, edit, run and verify until done. | Yes | Yes |
| **Edit** | Change the selection or the open file. | Yes | No |
| **Debug** | Diagnose a failure, fix it, re-run the build. | Yes | Yes |
| **Plan** | Break work into a checkable task list first. | No | Yes |
| **Explain** | Walk through how the code works, with `path:line` references. | No | No |

Read-only modes are never given mutating tools at all. Build gets the full 27-tool set; Debug gets all of it except `set_plan`. Plan keeps every read-only tool and adds `set_plan` and `question`, but still gets no file writes. Chat, Edit and Explain are single-reply modes and never get the `question` tool.

The AI surface docks as a **bottom sheet**, **left panel**, **right panel** or **full screen**, with snap sizes and free drag. Its **Chat**, **Activity**, **Plan**, **Changes**, **Context** and **Tools** panes are one tap apart.

#### Providers

| Provider | Notes |
|---|---|
| **OpenCode Zen** | Default `grok-code`; OpenAI-compatible endpoint; free and paid models |
| **OpenRouter** | Hundreds of models behind one key |
| **Custom endpoint** | Any OpenAI-compatible server |
| **Local server** | llama.cpp, Ollama's compatible endpoint or LM Studio; defaults to `http://127.0.0.1:11434/v1` |
| **Saved custom providers** | Name, base URL, key, model and wire format, stored by the user |

Wire formats are **OpenAI `/chat/completions`**, **Anthropic `/messages`** and **Auto-detect** (which picks Anthropic's shape for model families such as `claude-*`). Streaming, tool calling, temperature, max tokens, max steps and context window are configurable. A one-tap **token-saver preset** caps the run at 12 steps and 4096 output tokens, lowers temperature to 0.1, turns off current-file/tabs/related-files/Git-diff/problems/entire-project context and verify-after-edit, and leaves the selection, provider, mode and permissions untouched.

The transcript shows prompt/completion tokens, model used and cost where the provider reports it. Chain-of-thought can be shown or hidden and is included in export only when visible.

#### Context

The agent can receive:

- The current file
- The editor selection
- Other open tabs
- Related files
- The uncommitted Git diff
- Build problems / diagnostics
- The entire project

The composer understands Cursor-style mentions: `@file`, `@folder`, `@selection`, `@code`, `@problems`, `@git` and `@terminal`. Files, folders, selections, Git diffs, problem lists, build output and device attachments can be pinned as context chips. Device files picked through the system picker are copied into the app cache; they survive transcript reopens for about a day before the cache sweep, and clearing the cache removes them.

Project knowledge is loaded before the first prompt:

- **Instruction files, first found wins:** `AGENTS.md`, `CLAUDE.md`, `CONTEXT.md`, `.phoneide/AGENTS.md`.
- **Skills:** `<name>/SKILL.md` under `.phoneide/skills`, `.opencode/skills`, `.opencode/skill`, `.claude/skills` and `.agents/skills`.
- **Built-in skills:** `fix-build-errors`, `add-compose-screen`, `explain-codebase`.
- Project skills can override a built-in with the same name.

#### Permissions and safety

| Capability | Default |
|---|---|
| Read files | Allow |
| Create files | Allow |
| Modify files | Allow |
| Delete files | Ask |
| Read-only shell commands | Allow |
| Builds and tests | Allow |
| Package installs (`apt`, `pkg`, `sdkmanager`, `npm`, …) | Ask |
| Git writes (`commit`, `checkout`, `merge`, …) | Ask |
| Destructive commands (`rm -rf`, `git reset --hard`, force push, `mkfs`, …) | Deny |
| Network tools (`web_search`, `web_fetch`) | Allow |
| Change PhoneIDE's theme / font size | Allow |

Shell commands are classified by pattern, not by asking the model to self-report risk. The highest matching class wins, so `ls && rm -rf /` is destructive rather than safe. A permission prompt can be answered **Allow once**, **Allow for session** or **Deny**. Deletes are always once-only.

A separate **workspace trust** gate stands in front of command execution. A cloned project can carry a `settings.gradle` that runs arbitrary Kotlin at configuration time, so the first build or command in an untrusted project asks before running it.

The agent's filesystem is a canonicalised sandbox. Project file tools resolve inside the open project; `..`, symlinks and prefix tricks are resolved before the check. A separate pair of settings controls access to PhoneIDE's own toolchain tree (the guest rootfs, JDK, SDK and Gradle caches); read and write access both default to on and can be switched off to keep the agent inside the project.

#### Changes, checkpoints and review

- Every write produces a numbered, reviewable **change set** with a line diff.
- Changes can be applied, accepted, rejected or reverted as a unit.
- **Auto-apply modes:** Always ask, Auto-apply safe changes (default), Auto-apply everything.
- Safe mode applies new files and clean edits; deletes, dirty buffers and large destructive rewrites wait for approval.
- **Checkpoints** are optional and **off by default**. When enabled, an editing run snapshots the files it is about to touch. Up to 20 are kept in memory and can be restored from the Changes pane. Checkpoints deliberately do not survive process death; Git is the durable answer.
- The agent can publish a **plan** with task IDs, dependencies and evidence for completion. The plan survives in the saved transcript.

#### Transcripts

AI conversations are stored durably per project in `files/ai_transcripts`. Reopening a project restores both the visible chat and the model's conversational context. Transcripts can be exported as a plain-text document and shared, with optional reasoning.

#### Background runs

`AiRunService` is a foreground service that keeps a user-started agent run alive when the app is backgrounded. It is separate from the terminal and build services so one finishing never tears down another. The run can be stopped from the notification.

### Terminal and Linux guests

The terminal uses **Termux's terminal-view 0.118.0** with a real PTY and VT100/xterm rendering. `nano`, `vi`, `htop`, `top` and interactive installers work.

- App-scoped tabbed proot shells plus project-scoped sessions.
- Session drawer, pinch zoom, copy/paste and quick commands.
- First terminal launch downloads and extracts the selected guest rootfs with progress.
- A foreground service keeps shells alive while the app is backgrounded.
- Quick installers for **Claude CLI**, **OpenCode CLI**, **Codex CLI** and **Antigravity CLI** — complete, self-contained installers with dependency setup, PATH setup and verification, written for the Ubuntu guest's `apt`/`dpkg` environment.
- No root required: the guest runs under proot.

Three guests are available. See [Guest environments](#guest-environments).

### Toolchain manager

The Toolchains screen installs, switches, verifies and repairs every tool a build needs:

- **JDK** — 8, 11, 17, 21, 25
- **Command-line Tools** — catalogued `cmdline-tools` builds for ARM64
- **Android SDK Platform** — API 30 through 36
- **Android Build-Tools** — 33.0.2 through 36.1.0
- **Platform-Tools** — `adb`, `fastboot`
- **AAPT2** — ARM64 resource compiler
- **Kotlin** — 1.9.24 through 2.4.10
- **Gradle** — 8.9 through 9.7.1
- **Android Gradle Plugin** — 8.7.3 through 9.3.2
- **Flutter SDK** — stable and beta channels
- **Kotlin Language Server** 1.3.13
- **Eclipse JDT Language Server** 1.38.0
- **Eclipse LemMinX** 0.28.0
- **Dart Language Server** — bundled with Flutter

The catalog is bundled as `app/src/main/assets/tool_catalog.json` (updated 2026-08-25) and can be replaced at runtime by a remote catalog, so new tool versions do not require an app update.

Other toolchain behaviour:

- **Guest-aware releases.** One catalog carries both glibc Adoptium JDKs and Termux's bionic JDKs; releases a guest cannot run are dropped before install.
- **Compatibility engine.** Gradle/AGP and Gradle/Kotlin pairs are checked before a project is generated or a build is started. A stale active AGP is never allowed to make a fresh project unbuildable.
- **Custom versions.** JDK, Gradle, Kotlin, AGP, SDK, Build-Tools, cmdline-tools and Flutter accept a hand-entered version the catalog does not list, with immediate shape validation.
- **Verification and repair.** Every installed tool is checked against the one artifact that proves it works (`bin/java`, `bin/sdkmanager`, `android.jar`, `d8`, `adb`, …). Broken installs are reported with the expected path and a repair path.
- **Environment writer.** Installs are exported into the guest's `PATH`, `JAVA_HOME`, `ANDROID_HOME`/`ANDROID_SDK_ROOT`, Gradle properties and shell startup files.
- **Install queue.** Downloads and extraction run through a visible, cancellable queue with progress and a persistent toolchain log.
- **Storage-aware.** The Storage Manager measures what each tool occupies before you delete anything.

#### AAPT2 on ARM64

Google publishes `aapt2` for x86_64 Linux only. Ubuntu and Alpine therefore ship an ARM64 `aapt2` from the Android 14 line, which cannot link `compileSdk 35` or 36 resources.

PhoneIDE handles this in two ways:

1. **`Aapt2Overlay`** downloads SHA-256-verified ARM64 `aapt2`, `aidl`, `zipalign` and `split-select` binaries from `Commit451/android-arm-build-tools` (36.1.0, 36.0.0 or 35.0.1, newest first). A successful overlay raises the glibc guest's ceiling from 34 to **36**. If the download fails, it falls back to the distro package.
2. **Termux needs no overlay.** Its own `aapt2` is new enough for `compileSdk 36` natively.

PhoneIDE points Gradle at the guest `aapt2` with `android.aapt2FromMavenOverride`, because AGP's default is an x86_64 binary that cannot start on a phone.

#### Flutter on ARM64

Google's Linux Flutter tooling is published for x86_64. A stock `flutter` checkout fails on an ARM64 phone, or worse, produces a release APK that installs and black-screens.

PhoneIDE repairs the SDK in place after cloning it:

- Replaces the x86_64 Dart SDK with Dart's own ARM64 build at the **same version**, then clears stale snapshots.
- Restores executable bits that Android's ext4 extraction loses.
- Replaces the x86_64 `llvm-strip` with Ubuntu's ARM64 build, wrapped so a failure copies the unstripped library instead of emitting a zero-byte `.so`.
- Fetches Flutter's `linux-x64` `gen_snapshot` and runs it through `qemu-x86_64` with an amd64 glibc sysroot, because Google never publishes a `linux-arm64` release AOT compiler.

These fixups are applied only on **Ubuntu + ARM64**. Flutter is locked on Termux and Alpine until those runtimes are validated, and it is not offered to a 32-bit app process.

### Build system

Build Center starts Gradle tasks on any known project without opening it first:

- **Task presets:** `assembleDebug`, `assembleRelease`, `clean`, `lint`, `test`.
- **Task browser:** runs `gradle tasks --all` and parses the output into a searchable, grouped, tappable list.
- **Saved custom tasks:** store a label and Gradle arguments, with shell metacharacters rejected. Saved tasks belong to the project and live in its `.phoneide/` folder.
- **Preflight checks** before Gradle starts: AAPT2, Android platform, Build-Tools and Gradle compatibility.
- **Automatic remedies** for mechanically fixable failures:
  - install a missing SDK platform, Build-Tools or CMake package through `sdkmanager`;
  - stand in for a missing NDK with a lightweight `source.properties` plus `llvm-strip`/`llvm-objcopy` wrappers instead of downloading the gigabyte-scale package (enough for stripping prebuilt libraries; a project that genuinely compiles native code still needs a manually installed NDK);
  - activate the Gradle version AGP asks for;
  - switch a Flutter project off AGP's built-in Kotlin when it is too old for Flutter's plugin.
  A remedy is never applied twice for the same failure, so a bad guess cannot loop the build forever.
- **Build console** in the editor with filters, search, tap-to-jump diagnostics and cancel.
- **Completion sounds** for success and failure, because a phone build often runs while you are looking at another screen.
- **Foreground build service.** A running Gradle build survives backgrounding.
- **Run = build + install.** With Shizuku or root, the APK is installed silently behind an "Installing…" overlay. Without it, Android's installer is used. The "Auto install after builds" switch controls whether a successful build hands its APK to the installer automatically; explicit Run and Install actions are never gated by it.
- **Build Center is not a build history.** Nothing persists a list of past builds. What every row reports is the newest APK actually on disk for that project. Home keeps only the latest result per project.

### Preview Studio

Preview Studio renders layouts and sites without leaving the editor.

**Modes**

| Mode | What it shows |
|---|---|
| **Auto** | Picks Web, Flutter Web or Structure from the active file and project |
| **Web** | Serves a static HTML/CSS/JS project through a local asset server |
| **Flutter Web** | Builds and serves `build/web`; can add web support to a Flutter project first |
| **Structure** | Parses XML layouts, Compose or Dart into a device-framed structural preview |
| **URL** | Loads any URL in the embedded browser |

**Device presets:** Pixel, Galaxy S, Small Phone, Tablet, Fold (unfolded), Mobile, Desktop. Portrait/landscape, Light/Dark/AMOLED, zoom, fullscreen and a preview console are available.

- **XML layouts** are parsed into a live DOM. Selecting a node shows its attributes; edits are written back to the source file.
- **Layout tree** shows the hierarchy and can jump to the source line.
- **Compose and Dart structure previews are explicitly Beta** and heuristic. They are useful for shape and hierarchy, not pixel-perfect rendering.
- **Web preview** serves the project directly; no Node.js or build step is required for static sites.
- **Flutter Web preview** uses the installed Flutter SDK and shows build output/errors in the console.

### Version Control

Version Control is a separate surface from GitHub Manager and works on local repositories. It shells out to the guest's own `git`; PhoneIDE does not bundle JGit.

**Implemented (10 sections):**

- **Overview** — selected repository, branch, working tree and recent commits.
- **Repositories** — projects on the device, Git initialisation and real clone.
- **Changes** — stage, unstage, preview diffs and commit.
- **History** — commit log with author, time and full message.
- **Branches** — local branches, creation and checkout.
- **Sync** — fetch, pull and push against the configured remote.
- **Remotes** — remote names and URLs read from the repository.
- **Tags** — tags read from Git.
- **Stash** — save, apply and drop work in progress.
- **Settings** — commit identity, clone folder and guest Git status.

**Placeholders (4 sections):**

- Merge & Rebase
- Conflicts
- File History
- Submodules

These open an honest "next batch" card. They do not show invented data.

### GitHub Manager

GitHub Manager talks to `api.github.com` with a personal access token. It is a separate 20-section workspace from local Version Control.

**Implemented (7 sections):**

- **Overview** — account, repositories and API quota at a glance.
- **Repositories** — browse, search, create and import repositories. Import downloads the repository's ZIP snapshot into a project folder, so no `.git` history comes with it; a real `git clone` can be run in the terminal afterwards.
- **Issues** — issues assigned to, created by or mentioning you.
- **Pull Requests** — pull requests you created, are assigned or are mentioned in.
- **Actions & Workflows** — recent workflow runs for a repository.
- **Releases** — releases, assets and download counts.
- **Gists** — public and secret gists.

**Placeholders (13 sections):**

Notifications, Organizations, Collaborators, Projects, Discussions, Packages, GitHub Pages, Security, Webhooks, Access & Keys, Search Center, Analytics and Settings.

The token is stored in private `SharedPreferences`, never written to logs, and can be disconnected from the GitHub screen.

### APK Center

APK Center scans build outputs directly and groups them by project.

- Shows every APK under each project's module build outputs (`<module>/build/outputs/apk`), grouped by project, newest first, with variant, size and build time.
- **Install** through Shizuku/root silently when available, or through the system installer.
- **Share** through any app that accepts a file.
- **Delete** an APK without touching the project.
- Opens the system installer with the correct `FileProvider` URI for both debug and release variants.
- Reminds you about battery optimisation when the OEM battery manager is likely to kill installs/builds.

APK Center is an artifact browser, not a build history.

### JKS Manager and release signing

PhoneIDE keeps signing material in a central **`1Keystore/`** folder with a `registry.json` and per-keystore `config.properties`.

- **Create JKS:** keystore name, file name, store password, alias, key password, common name, organization, organizational unit, city, state, country (default `IN`), validity (default 25 years), RSA 2048.
- **Import JKS:** bring an existing keystore into the registry.
- **Per-project signing:** link a project to a keystore, test-sign, and choose signature schemes and ABI filters.
- **V1/V2/V3 signing** are independently selectable.
- **ABI filters** can restrict a release APK to selected architectures; empty means "All".
- **APK re-signing with `apksigner`.** AGP ignores parts of the injected signing config and has no injected V3 switch, so PhoneIDE re-signs the release APK explicitly. This is also what makes the same flow work for Flutter's stock Gradle template.
- **Signing test** checks the keystore and options before a release build.
- **Signing status** is reachable from the editor's overflow menu.

For source builds outside the app, release signing is read from `AB_KEYSTORE_DIR` (default `/storage/emulated/0/AndroStudioProjects/keystore`) and `config.properties`. If no keystore is found, the release build falls back to the **debug signing config**. See [Build from source](#build-from-source).

### Icon Manager

Generate a complete launcher icon set from one image:

- Pick an image, preview it in an adaptive-icon frame, and tune the logo scale and shape (circle, squircle, rounded, square).
- Choose a background colour from swatches or a custom value.
- Generate the optional monochrome layer used by Android 13+ themed icons; it needs transparent artwork, and a separate themed image can be supplied.
- Writes PNG mipmaps at every density, adaptive-icon XML, background colour resources and the manifest attributes that reference them.
- Works with existing projects; no external image tool needed.

### AppLog and IDE log

PhoneIDE has two separate logs.

**AppLog** is for the app you are building:

- Targets a **debug app built by PhoneIDE**, verified by package and signing certificate. It will not attach to an arbitrary app.
- Streams logcat, with filters, search, grouping and per-run sessions.
- Redacts secrets before display/export.
- Exports **TXT**, **JSON** or **ZIP**.
- Uses **Shizuku or root** when available to read another app's log lines, which Android's `logd` otherwise filters out. Without either, it shows what the app's own UID can see.

**IDE log** is PhoneIDE's own diagnostic log, reachable from Settings → System → Background. It is for troubleshooting the IDE itself, separate from AppLog and the terminal.

### Templates and new projects

Every template is backed by a real emitter. A template cannot be listed without producing buildable files.

| Template | Category | Toolkit | Language | Notes |
|---|---|---|---|---|
| **No Activity** | Phone & Tablet | Gradle skeleton | Kotlin or Java | Manifest and resources, no launcher activity |
| **Basic Activity** | Phone & Tablet | Views | Kotlin or Java | Title, counter, floating action button |
| **Empty Activity** | Phone & Tablet | Views | Kotlin or Java | One XML layout, one centred text view |
| **Compose Empty Activity** | Phone & Tablet | Compose | Kotlin only | Material 3 theme and greeting composable |
| **Bottom Navigation Activity** | Phone & Tablet | Views | Kotlin or Java | Three fragments plus Navigation component |
| **Navigation Drawer Activity** | Phone & Tablet | Views | Kotlin or Java | DrawerLayout, NavigationView, Toolbar, toggle |
| **Responsive Activity** | Phone & Tablet | Views | Kotlin or Java | Navigation rail plus a reflowing grid |
| **Static Website** | Web | HTML/CSS/JS | — | Responsive one-page site, no Node.js |
| **Multi-page Website** | Web | HTML/CSS/JS | — | Home, About and Contact sharing assets |
| **Flutter Application** | Other | Flutter | Dart | Created by the Flutter SDK; requires Flutter setup |

**New-project options:**

- App name, project name, package name and location.
- Kotlin or Java where the template supports it (Compose is Kotlin-only).
- **Kotlin DSL (`.kts`)** or **Groovy DSL (`.gradle`)**.
- **minSdk:** 21, 23, 24, 26, 28, 31 or 34.
- **compileSdk/targetSdk** resolved from the active installed platform within the guest's AAPT2 ceiling.
- **Build-Tools, AGP, Kotlin and Gradle** resolved from what is actually installed and active, so the artifacts are already in the Gradle cache.
- **Git initialisation:** none, a lightweight `.git` stub, or a full empty repository layout that `git status`/`add`/`commit` accept immediately.
- Real launcher icons rasterised on-device with Canvas at every density.
- `.gitignore`, Gradle wrapper scripts, version catalog, ProGuard rules and a `.nomedia` guard at the projects root so generated icons and media never show up in Gallery.

Native C++ and Game templates are deliberately absent: PhoneIDE has no first-class NDK/CMake setup flow, so offering them would create projects that cannot compile.

### Search, files and project export

- **Search** works on the active or last-opened project in two modes: **file names** and **file contents**. Results show path and line, open at the matching line, debounce while typing and cap very large result sets.
- **File tree** supports browsing, creating, renaming and deleting files and folders. Generated directories are shown by default and can be hidden with the "Show generated folders" setting.
- **Clean ZIP export** excludes build output, `.gradle`, `.git`, IDE metadata, archives, generated binaries, `local.properties`, `.env`, signing keys and Flutter's generated files. Every exclusion is reviewable and overridable.
- **Project deletion** runs a preflight and closes live windows/builds before removing bytes.

### Appearance and localization

**App themes:**

- **PhoneIDE** (the default static style)
- **HyperOS**
- **ASUS OS**
- **NothingOS**
- Material You **dynamic colour** on Android 12+, available with the PhoneIDE theme style
- Light / dark / follow system
- App-wide font scale **85%–130%** in 5% steps

**Editor themes:**

Follow app theme, Dracula, One Dark Pro, Monokai Pro, Nord, Tokyo Night, Catppuccin Mocha, Gruvbox Dark, GitHub Dark, Solarized Dark, GitHub Light, Solarized Light, Catppuccin Latte.

**Languages (11):**

English, हिन्दी (Hindi), Español (Spanish), Français (French), Deutsch (German), Português (Portuguese), Русский (Russian), العربية (Arabic, RTL), 中文 (Chinese), 日本語 (Japanese), Bahasa Indonesia (Indonesian).

Switching language recomposes the app instantly; no restart. Missing translations fall back to English rather than showing blank labels. Arabic switches the layout to RTL.

### Storage, updates and system settings

**Storage & cache** measures the things Android's App Info hides:

- Linux guest/rootfs
- Installed toolchains
- Gradle caches
- Project build outputs
- APKs
- App cache, tool downloads and temp files
- Language-server indexes

Safe bulk actions:

- **Clear safe cache** — app cache, downloads, temp and guest cache
- **Clear build caches** — Gradle caches, build outputs and LSP indexes

Installed tools, project workspaces and the Linux base image are shown but not deleted here; that is a Toolchains/setup decision. Cache clearing is blocked while an install or build is running.

**Updates:** PhoneIDE checks GitHub releases and tags every **12 hours** through WorkManager, posts a notification when a newer version appears, and offers in-app download and install. Tags work too; their description comes from the tag message. A check also runs when the app opens.

**System settings** include theme, language, font size, editor preferences, AI settings, toolchains, storage, battery optimisation, logs, privacy policy, open-source licences, developer credits and about.

### Privacy and permissions

PhoneIDE's in-app privacy policy is **version 1.0** with 11 sections: summary, no collection, local storage, permissions, network, AI, third-party services, user control, children, policy changes and contact.

- No PhoneIDE account.
- No ads and no analytics/telemetry SDK.
- Project files stay on the device unless you explicitly send them to an AI provider or GitHub.
- The AI agent sends prompts, context and attachments only to the provider you selected. A local server stays local.
- Update checks and GitHub features contact GitHub.
- AppLog and the IDE log stay local until you export them.

**Permissions requested:**

| Permission | Why |
|---|---|
| `INTERNET`, `ACCESS_NETWORK_STATE` | Toolchain downloads, AI providers, GitHub, updates |
| `MANAGE_EXTERNAL_STORAGE` | Real filesystem paths for proot and Gradle |
| `READ_EXTERNAL_STORAGE` (≤ API 32), `WRITE_EXTERNAL_STORAGE` (≤ API 29) | Legacy storage on older Android |
| `REQUEST_INSTALL_PACKAGES` | Install a built APK |
| `POST_NOTIFICATIONS` | Foreground-service and update notifications |
| `FOREGROUND_SERVICE`, `FOREGROUND_SERVICE_SPECIAL_USE`, `FOREGROUND_SERVICE_DATA_SYNC` | Keep terminal, AI runs and builds alive |
| `WAKE_LOCK` | Finish long builds/installs with the screen off |
| `RECEIVE_BOOT_COMPLETED` | Restore scheduled update checks |
| `REQUEST_IGNORE_BATTERY_OPTIMIZATIONS` | Offer the OEM battery exemption dialog |
| `SYSTEM_ALERT_WINDOW` | "Installing…" overlay during a silent install |

No camera, microphone, location, contacts or SMS permission is requested. Shizuku is optional and uses its own provider; root is optional and never required.

### Developer tools

- **About** — version, build information and project links.
- **Developer** — creator and contributor credits with copy/open actions.
- **Open Source Licenses** — every bundled third-party library and its licence.
- **IDE log** — PhoneIDE's own diagnostic log.
- **AppLog** — target-app logcat with redaction and export.
- **Storage Manager** — detailed footprint and safe cache clearing.
- **Battery optimisation** — direct link to the system exemption dialog.

---

## AI reference

### The 27 tools

| Group | Tools |
|---|---|
| **Read and navigate** | `read_file`, `list_files`, `search_code`, `search_files`, `glob`, `grep`, `find_symbol`, `go_to_definition`, `find_references`, `open_in_editor`, `skill`, `diagnostics`, `get_app_settings` |
| **Write** | `create_file`, `write_file`, `edit_file`, `apply_patch`, `delete_file` |
| **Execute** | `terminal`, `run_build`, `git_status`, `git_diff` |
| **Agent control** | `question`, `set_plan` |
| **Web** | `web_search`, `web_fetch` |
| **App settings** | `set_app_settings` |

Notable behaviour:

- `edit_file` replaces an exact snippet and requires a unique match unless `replace_all` is set.
- `apply_patch` accepts a unified diff across multiple files.
- `run_build` goes through the IDE pipeline, so output lands in the Gradle console and errors become clickable diagnostics.
- `find_symbol`, `go_to_definition` and `find_references` use the language server index, not text search.
- `open_in_editor` puts the user on the exact file and line being discussed.
- `set_plan` supports task IDs, `depends_on` and evidence for "done".
- `question` stops the run and waits for the user's answer.

### Change review

Every write is recorded as a numbered file change with a line diff:

- `CREATE` — new file
- `MODIFY` — changed file
- `DELETE` — deleted file, with the old text kept for revert

A change set reports `N files · +added −removed`. The Changes pane can accept, reject or revert each change. Reverts restore the text captured before the write.

### Checkpoints

- Opened before an editing run touches files, when the checkpoint setting is enabled (off by default).
- Capture only the files actually touched, first-write-wins.
- Maximum **20** checkpoints.
- Restoring a checkpoint rewrites the captured text and deletes files that did not exist before.
- In memory only. Process death loses checkpoints, but transcripts and files on disk remain.

---

## Toolchain catalog

The bundled catalog is a starting point, not a hard ceiling. A reachable remote catalog replaces it at runtime, and supported tools accept hand-entered versions.

| Tool | Catalogued range / versions |
|---|---|
| **JDK** | 8, 11, 17, 21, 25 |
| **Command-line Tools** | Catalogued ARM64-compatible builds |
| **Android SDK Platform** | API 30–36 |
| **Build-Tools** | 33.0.2–36.1.0 |
| **Platform-Tools** | Catalogued release |
| **AAPT2** | ARM64 build from the guest, plus the AAPT2 overlay |
| **Kotlin** | 1.9.24–2.4.10 |
| **Gradle** | 8.9–9.7.1 |
| **Android Gradle Plugin** | 8.7.3–9.3.2 |
| **Flutter** | Stable and beta |
| **Kotlin LSP** | 1.3.13 |
| **JDT LS** | 1.38.0 |
| **LemMinX** | 0.28.0 |
| **Dart LSP** | Bundled with the Flutter SDK |

### PhoneIDE's own build vs generated projects

These are deliberately different:

| | PhoneIDE itself | Generated projects |
|---|---|---|
| **Gradle** | 8.11 (wrapper) | Active installed version; fallback 9.7.1 |
| **AGP** | 8.7.3 | Active installed version; fallback 9.3.2 |
| **Kotlin** | 2.0.21 | Active installed version; fallback 2.4.10 |
| **JVM target** | 11 | Template default |
| **compileSdk** | 35 | Active installed platform within the AAPT2 ceiling; fallback 34 |
| **Build-Tools** | 35.0.0 | Active/installed version compatible with AGP; fallback 36.0.0 |
| **minSdk** | 24 | Chosen per project: 21–34 |

PhoneIDE pins its own build to Java 11 bytecode because sora-editor 0.22.1 requires it. Generated projects resolve to what the device actually has installed, so their artifacts are already in the Gradle cache.

---

## Guest environments

| Guest | Base image | libc | Package manager | Download | Installed | AAPT2 ceiling | Notes |
|---|---|---|---|---|---|---|---|
| **Ubuntu** | 24.04 LTS (24.04.4 base) | glibc | `apt` | ~30 MB | ~120 MB | 34 → 36 with overlay | Recommended. Google SDK binaries and Flutter work here. |
| **Termux** | bootstrap 2026.08.30 | bionic | `pkg` | ~33 MB | ~95 MB | 36 natively | Native Android packages; no Flutter. |
| **Alpine** | 3.24.1 | musl | `apk` | ~3 MB | ~8 MB | 34 → 36 with overlay | Tiny and fast; some prebuilt tools need `gcompat`. |

Guest choice is made on the welcome screen; once a guest is installed, Settings shows the other distros as locked. The rootfs is downloaded from the guest's official source and unpacked under `files/home/`.

**Flutter support is Ubuntu + ARM64 only.** Termux (bionic) and Alpine (musl) are locked until their Flutter/Dart runtimes are validated. A 32-bit app process cannot run Flutter's Linux toolchain.

---

## Architecture

PhoneIDE is a single Gradle module, `:app`.

```text
app/src/main/java/com/io/phone/ide/
├── ai/            Agent runtime, providers, 27 tools, permissions, diffs, checkpoints, skills, transcripts
├── data/          App preferences, editor settings, UI state, language, profile, logs
├── feature/       Screens and feature logic
│   ├── about/ developer/ privacy/ settings/ logs/
│   ├── apk/ applog/ buildcenter/ editor/ home/ newproject/ picker/
│   ├── github/ vcs/ jks/ icons/ preview/ search/ terminal/ toolchain/ sdk/
│   ├── onboarding/ permissions/ setup/ workspace/
├── lsp/           Kotlin, Java, XML and Dart language-server integration
├── navigation/    Routes, bottom tabs, transitions
├── project/       Templates, generators, file tree, search, ZIP export, deletion
├── shell/         Privileged shell (Shizuku/root)
├── terminal/      proot, guest distros, rootfs installer, PTY controller
├── toolchain/     Catalog, installers, compatibility, AAPT2 overlay, Flutter fixups
├── ui/            Compose theme, common components
├── update/        GitHub release/tag checker and WorkManager scheduling
├── localization/  11-language string system
├── AppContainer.kt
├── MainActivity.kt
├── ProjectActivity.kt
└── PhoneIDEApp.kt
```

Key assets:

- `app/src/main/assets/tool_catalog.json` — bundled, remotely replaceable tool catalog.
- `app/src/main/jniLibs/<abi>/` — proot, loader, talloc and android-shmem.
- `app/libs_backup/<abi>/` — known-good copies of those native libraries.
- `app/build.gradle.kts` — version, SDK, ABI and signing configuration.

### How a build flows

```text
Project (Editor or Build Center)
        │
        ▼
Preflight: AAPT2 · platform · Build-Tools · Gradle/AGP compatibility
        │
        ├── missing? → BuildRemedies installs/activates the package, retry once
        ▼
GuestShell → proot → Ubuntu / Termux / Alpine
        │
        ▼
Gradle + JDK + Android SDK + Build-Tools
        │
        ▼
APK in app/build/outputs/apk/…
        │
        ├── APK Center (install/share/delete)
        ├── JKS Manager re-signs (V1/V2/V3, ABI filters)
        └── Run → silent install via Shizuku/root, or system installer
```

### How the AI agent flows

```text
AiSession
   │  project knowledge: AGENTS.md / skills
   │  context: files, selection, problems, git diff, attachments
   ▼
AiClient ── OpenAI /chat/completions or Anthropic /messages
   │
   ▼
Tool loop (max steps / max tokens)
   │
   ▼
AiToolExecutor
   ├── AiPathGuard     canonicalised project sandbox
   ├── AiPermissions   capability + workspace trust
   ├── AiCommandRisk   safe → build → install → git write → destructive
   └── AiChangeSet     numbered diffs + checkpoints
   │
   ▼
Files on disk / guest terminal / build pipeline
```

State is held in `AppContainer`, one `ProjectWorkspaceState` per project, Compose snapshot state and private `SharedPreferences`. AI transcripts are JSON files under `files/ai_transcripts`; the guest and toolchains live under `files/home/`.

---

## Build from source

### Prerequisites

- Android SDK with **platform 35** and **Build-Tools 35.0.0**
- **JDK 17** (AGP 8.7.3's supported JDK; the module compiles to Java 11 bytecode)
- Network access for the Gradle wrapper and dependencies
- Git, if you want to clone the repository

The Gradle wrapper downloads **Gradle 8.11** itself.

### Two repository-specific changes before building on a desktop/CI machine

This repository is normally built by PhoneIDE on a phone and carries two machine-specific settings:

1. **`gradle.properties`** contains:

   ```properties
   android.aapt2FromMavenOverride=/data/data/com.jahangir/files/usr/bin/aapt2
   ```

   Remove that line, or point it at a real local `aapt2`, before a desktop/CI build.

2. **`local.properties`** currently points at a machine-specific SDK:

   ```properties
   sdk.dir=/root/Android/Sdk
   ```

   Set it to your own SDK path, or delete the file and let `ANDROID_HOME`/`ANDROID_SDK_ROOT` decide.

### Build commands

```bash
# Universal debug APK (arm64-v8a + armeabi-v7a)
./gradlew :app:assembleDebug

# Explicit ABI test builds
./gradlew :app:assembleDebug -Pphoneide.abi=arm64
./gradlew :app:assembleDebug -Pphoneide.abi=arm32

# Release build
./gradlew :app:assembleRelease
```

On Windows use `gradlew.bat`.

Outputs:

- Debug: `app/build/outputs/apk/debug/app-debug.apk`
- Release: `app/build/outputs/apk/release/app-release.apk`

The debug variant uses application ID `com.io.phone.ide.debug`; release uses `com.io.phone.ide`.

### Release signing

The build reads a shared keystore from `AB_KEYSTORE_DIR`:

```bash
export AB_KEYSTORE_DIR=/path/to/keystore
```

It expects `$AB_KEYSTORE_DIR/config.properties`:

```properties
storeFile=Universe-release.jks
storePassword=…
keyAlias=…
keyPassword=…
```

The default directory is `/storage/emulated/0/AndroStudioProjects/keystore`, and the default store file is `Universe-release.jks`.

- If the keystore is found, release builds use it.
- If it is missing, AGP falls back to the **debug signing config**.
- V1, V2 and V3 signing are enabled.
- R8 and resource shrinking are enabled for release.

PhoneIDE's own JKS Manager writes the same kind of configuration for projects built inside the app. Do not commit keystores or passwords.

### Native libraries

`restoreNativeLibs` copies `app/libs_backup/<abi>/*.so` into `app/src/main/jniLibs/<abi>/` before every build. This protects proot's ELF binaries from accidental text corruption by other editors/indexers.

- `packaging.jniLibs.useLegacyPackaging = true` is required: proot is executed from `nativeLibraryDir` on API 29+.
- `libproot.so`, `libproot_loader.so`, `libtalloc.so` and `libandroid-shmem.so` are kept unstripped.
- If `libs_backup/<abi>` is missing, the task skips and the existing `jniLibs` copy must already be valid.

### Optional install

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

Or install it directly on the phone from APK Center.

---

## Requirements and limitations

### Requirements

- **Android 7.0+ (API 24)**, targetSdk 35.
- **ARM device**: `arm64-v8a` strongly recommended; `armeabi-v7a` supported with a reduced toolchain (Flutter and some JDKs are 64-bit only).
- **Free storage**: plan for several GB. The guest is ~100 MB, the SDK/Build-Tools/JDK caches add hundreds of MB, and projects/build caches add more.
- **RAM**: 4 GB can work for small projects if the Gradle heap is lowered; 6 GB+ is recommended for on-device builds.
- **Internet** for first-run rootfs/toolchain downloads, AI providers, GitHub and updates. Builds can run offline once dependencies are cached.
- **Full storage access** and **install unknown apps** permission.
- **Shizuku or root** are optional, never required.

### Limitations

- **No x86/x86_64 Android support.** The native proot stack and guest toolchains are ARM.
- **No iOS, desktop or web app.** PhoneIDE itself is an Android application.
- **Builds are slow.** A phone is not a laptop; thermal throttling, battery limits and background killing are real. The app exposes foreground services and battery-exemption guidance, but cannot fix OEM behaviour.
- **glibc guests have an AAPT2 ceiling.** Ubuntu/Alpine can link `compileSdk 34` from the distro package; `Aapt2Overlay` can raise that to 36 when GitHub is reachable. Termux is 36 natively.
- **Flutter is Ubuntu + ARM64 only.** Termux/Alpine Flutter is locked. Release AOT uses a `qemu-x86_64` bridge because Google does not publish a Linux ARM64 `gen_snapshot`.
- **LSP is opt-in and resource-heavy.** It is off by default; enabling it can trigger a Gradle classpath resolve and slow first use.
- **No first-class NDK/CMake setup.** Native C++ and Game templates are intentionally absent.
- **No full debugger, profiler or emulator.** PhoneIDE builds, installs and logs; it does not replace Android Studio's debugging tools.
- **AI checkpoints are in-memory.** Up to 20, lost on process death. Transcripts and files on disk survive.
- **Some VCS/GitHub sections are placeholders.** See [Project status](#project-status).

---

## Project status

This is the honest map of what is shipped, what is Beta and what is a placeholder.

| Surface | Status |
|---|---|
| Editor, tabs, file tree, syntax highlighting | Shipped |
| LSP for Kotlin, Java, XML, Dart | Shipped, **off by default** |
| AI agent, 27 tools, 6 modes, permissions, diffs, opt-in checkpoints | Shipped |
| Terminal, 3 guests, quick CLI installers | Shipped |
| Toolchain manager, AAPT2 overlay, Flutter ARM64 fixups | Shipped |
| Gradle builds, remedies, console, task browser | Shipped |
| Preview Studio: Web, Flutter Web, URL, XML structure | Shipped |
| Preview Studio: Compose/Dart structure | **Beta** (heuristic) |
| Version Control | 10 of 14 sections implemented; 4 placeholders |
| GitHub Manager | 7 of 20 sections implemented; 13 placeholders |
| APK Center, JKS Manager, Icon Manager, AppLog, Storage Manager, updater | Shipped |
| Project templates (10) | Shipped |
| `Workspace` route | **Placeholder**; the Editor is the working surface |

**Version Control placeholders:** Merge & Rebase, Conflicts, File History, Submodules.

**GitHub Manager placeholders:** Notifications, Organizations, Collaborators, Projects, Discussions, Packages, GitHub Pages, Security, Webhooks, Access & Keys, Search Center, Analytics, Settings.

These screens open an explicit "next batch" card rather than fake data. If a feature is not listed as implemented above, do not assume it is shipped.

---

## Security notes

- **The agent is sandboxed to the project and the IDE tree.** Paths are canonicalised and checked before use. `..`, symlinks and prefix tricks are rejected. Separate settings control IDE-storage read and write access; both default to on and can be disabled.
- **Command risk is classified independently of the model.** Destructive commands are a separate capability and default to Deny.
- **Workspace trust is separate from capability permission.** Running a cloned project's Gradle scripts asks first.
- **Changes are reviewed before or after the write, depending on the auto-apply mode.** Deletes are always once-only.
- **API keys and GitHub tokens are stored in private `SharedPreferences`, not encrypted at rest.** The app never logs them. Treat them as protected by the Android app sandbox, not by hardware-backed encryption.
- **Android Auto Backup excludes only `files/home/`** — the multi-GB guest/toolchain tree. It does not exclude `SharedPreferences`, so tokens may be eligible for cloud backup. If that matters to you, disable backup for PhoneIDE in Android settings.
- **AppLog is restricted to a debug app built by PhoneIDE**, verified by signing certificate. Broader logcat access requires Shizuku or root.
- **The privacy policy is local and versioned.** Accepting version 1.0 records the version and timestamp; a material revision can ask again.
- **No analytics, ads or telemetry SDK is bundled.** The app does contact the network for toolchain downloads, AI requests, GitHub and update checks.

---

## FAQ

**Does PhoneIDE require root?**
No. The Linux guest runs under proot. Root and Shizuku are optional and only used for silent APK installs and broader logcat access.

**Does it really compile APKs on the phone?**
Yes. It installs a JDK, Android SDK, Build-Tools, Gradle, Kotlin and AGP into a Linux guest and runs Gradle there. The APK is produced on the device.

**Can it build without a PC?**
Yes, once the guest and toolchains are installed. First-run setup needs network access to download them.

**Can it build Flutter?**
Yes, on **Ubuntu + ARM64**, including release APKs. Flutter is locked on Termux and Alpine. Flutter Web preview is available when the Flutter SDK is installed.

**What `compileSdk` do generated projects use?**
The highest installed platform that fits the guest's AAPT2 ceiling — up to 36 on Termux, and on Ubuntu/Alpine up to 34 from the distro `aapt2`, or 36 with `Aapt2Overlay` active. When nothing is installed yet, the template fallback is 34.

**Why does it need "All files access"?**
proot and Gradle need real filesystem paths. SAF `content://` URIs cannot be built from. The app also reads and writes project folders anywhere on shared storage.

**Where are projects stored?**
By default under `/storage/emulated/0/PhoneIDEProjects`. Any folder can be chosen during project creation or opened through the picker.

**Is my code uploaded anywhere?**
Only when you explicitly use a network feature: an AI provider, GitHub, a toolchain download or an update check. A local AI server stays local.

**Does it work on a 32-bit phone?**
The universal APK includes `armeabi-v7a`, and Ubuntu/Termux/Alpine base images exist for ARM32. Some toolchain entries are 64-bit only, and Flutter is 64-bit only. ARM64 is strongly recommended.

**Can I use a different JDK/Gradle/AGP version?**
Yes, for supported tools. The catalog lists versions and also accepts hand-entered versions for JDK, Gradle, Kotlin, AGP, SDK, Build-Tools, cmdline-tools and Flutter. Compatibility checks still apply.

**Why does the release build fall back to debug signing?**
For source builds outside the app, release signing is read from `AB_KEYSTORE_DIR`. If no keystore is present, AGP uses the debug signing config. Configure a keystore for a real release.

**Why is Build Center not showing old builds?**
Because PhoneIDE does not persist a build history. Build Center shows the newest APK actually on disk per project. Home keeps only the latest result per project.

---

## Credits and license

**Created by [Sandeep Bedia](https://github.com/Sandeepbedia)** — creator of PhoneIDE.

**Contributor:** [Ankit Mehta](https://github.com/Playboy-gg).

- GitHub: [Sandeepbedia/PhoneIDE](https://github.com/Sandeepbedia/PhoneIDE)
- Telegram: [@adrodx](https://t.me/adrodx)
- Channel: [t.me/phoneide](https://t.me/phoneide)

PhoneIDE stands on excellent open-source work, including Jetpack Compose, AndroidX, Kotlin, Coil, Apache Commons Compress, XZ, Termux's terminal emulator/view, sora-editor, Shizuku and WorkManager. Their licences are listed in the app under **Settings → System → Developer → Open Source Licenses**.

**PhoneIDE is proprietary software.** Copyright © 2025 Sandeep Bedia. All rights reserved. No part of this software may be copied, modified, distributed, sublicensed or used without prior written consent. See [LICENSE](LICENSE) for the full text.

The PhoneIDE name and logo belong to their author. Third-party components remain under their own licences.

---

<p align="center">
  <strong>Build Android apps, on Android.</strong><br />
  <sub>PhoneIDE 2.8.2-V7-Parvaaz · versionCode 422</sub>
</p>
