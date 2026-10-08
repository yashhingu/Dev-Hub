# 🚀 DevHub

An enterprise-grade, open-source GitHub client for Android built with Kotlin Multiplatform-ready Clean Architecture, 100% Jetpack Compose, and offline-first reactive pipelines.

![API](https://img.shields.io/badge/API-26%2B-brightgreen.svg?style=flat)
![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-blue.svg?style=flat)
![Compose](https://img.shields.io/badge/Compose-BOM%202024.12.01-4285F4.svg?style=flat)
![License](https://img.shields.io/badge/License-MIT-green.svg)
![CI](https://github.com/yashh/DevHub/actions/workflows/ci.yml/badge.svg)

I built DevHub as a showcase resume & portfolio project to demonstrate modern Android architectural best practices in 2024–2025. Rather than another tutorial clone, DevHub is engineered around production-grade modularization, strict immutability, offline-first caching, and comprehensive unit tests with cashapp Turbine.

---

## ✨ Features

- **🔥 Trending Feed (`:feature:feed`)**: Real-time trending GitHub repositories, language filter chips, pull-to-refresh, shimmer placeholder states, and seamless offline fallback.
- **🔍 Smart Search (`:feature:search`)**: Debounced real-time query processing (300ms), search history cache, and reactive result list.
- **🏷️ Local Star & Bookmark Manager (`:feature:bookmarks`)**: Offline-first tagging and bookmark persistence with full-text search capability.
- **📄 Repository Details & Markdown Viewer (`:feature:repository-detail`)**: Deep repository metadata, stat badges (stars, forks, open issues), and native Compose Markdown parser for README files.
- **🎨 Design System (`:core:designsystem`)**: Complete Material 3 tokenized theming, dynamic color support (Android 12+), dark mode, custom GitHub language badge palette, and `@PreviewLightDark` states.
- **🛡️ Enterprise Clean Architecture**: MVI (Model-View-Intent) unidirectional data flow, pure Kotlin domain layer, zero Android dependencies in core models, typed `DataResult<T, E>`.

---

## 🏗️ Architecture & Modularization

DevHub uses Gradle convention plugins (`build-logic`) to maintain consistent configurations across all modules with explicit architectural boundaries:

```text
├── build-logic/                  # Convention plugins (Application, Library, Compose, Hilt, Feature)
├── core/
│   ├── common/                  # Coroutine dispatchers, DataResult<T, E>, Flow extensions, UiText
│   ├── network/                 # Ktor 3.0 OkHttp client, ContentNegotiation, NetworkDataSource
│   ├── database/                # Room persistence, DAOs, entities, LocalDataSource
│   ├── designsystem/            # M3 tokens, typography, components (RepositoryCard, Shimmer, EmptyState)
│   └── model/                   # Pure Kotlin @Immutable domain models (Repository, Owner, BookmarkTag)
└── feature/
    ├── feed/                    # Trending repositories feed (MVI ViewModel, Screen, UseCases)
    ├── repository-detail/       # Detail inspector, stats row, markdown viewer
    ├── search/                  # Debounced live search with filter chips
    └── bookmarks/               # Local bookmarked items and tag management
```

### Unidirectional Data Flow (MVI) & Offline-First

```text
       ┌───────────────┐
       │  Compose UI   │ ◄──────────────────────────────┐
       └───────┬───────┘                                │
               │ Emits Intent                           │ StateFlow<UiState>
               ▼                                        │
       ┌───────────────┐                                │
       │   ViewModel   │ ───────────────────────────────┘
       └───────┬───────┘
               │ Executes UseCase
               ▼
       ┌───────────────┐
       │  Repository   │
       └───────┬───────┘
          ┌────┴────────────────────────┐
          │ (1) Emit cached data        │ (2) Fetch remote & update cache
          ▼                             ▼
   ┌──────────────┐              ┌──────────────┐
   │ Local Cache  │ ◄─────────── │ Ktor Network │
   │    (Room)    │   Save fresh │  Data Source │
   └──────────────┘              └──────────────┘
```

---

## 🛠️ Technology Stack

| Category | Technology | Purpose |
|---|---|---|
| **Language** | Kotlin 2.0.21 | Modern language features, Compose compiler plugin |
| **UI Toolkit** | Jetpack Compose + Material 3 | Declarative UI, dynamic theming, edge-to-edge |
| **Navigation** | Navigation Compose 2.8.5 | Type-safe serializable navigation routes |
| **Networking** | Ktor Client 3.0.3 (OkHttp Engine) | Multiplatform HTTP client, kotlinx.serialization |
| **Persistence** | AndroidX Room 2.6.1 | Local SQLite persistence with Flow observers |
| **Async Storage** | Jetpack DataStore 1.1.1 | Recent searches & preference storage |
| **Dependency Injection** | Dagger Hilt 2.53.1 | Standard Android DI with assisted viewmodel factories |
| **Image Loading** | Coil 3.0.4 | Multiplatform asynchronous image loader |
| **Logging** | Timber 5.0.1 | Debug logging |
| **Testing** | MockK + CashApp Turbine | Coroutine Flow assertions and unit testing |
| **CI/CD** | GitHub Actions | Automated build, Spotless check, tests & coverage |

---

## 🧪 Testing Strategy

The project adheres to high testing standards:
- **StateFlow & MVI Transitions**: Verified using **CashApp Turbine** to assert every step of the UI lifecycle (`Loading` ➔ `Success`, `Loading` ➔ `Failure`).
- **Mocking**: MockK mocks with explicit `coEvery` and `coVerify` verification on repository boundaries.
- **Dispatchers**: Injected `DispatcherProvider` allowing deterministic testing via `StandardTestDispatcher`.
- **Pre-submit Pipeline**: Automated GitHub Actions workflow runs unit tests, ktlint, and generates Kover code coverage reports.

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug (2024.2.1+) or newer
- JDK 17
- Android SDK 35

### Build and Run
```bash
# Clone the repository
git clone https://github.com/yashh/DevHub.git
cd DevHub

# Build debug APK
./gradlew assembleDebug

# Run unit tests across all modules
./gradlew testDebugUnitTest
```

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
