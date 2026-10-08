# 🏗️ DevHub Architecture

This document details the architectural decisions, design patterns, and engineering principles used across DevHub.

---

## 1. Clean Architecture Layers

DevHub strictly follows Clean Architecture with unidirectional data flow (UDF):

### Presentation Layer (`:feature:*`, `:app`)
- **UI Toolkit**: 100% Jetpack Compose with Material 3 styling.
- **State Management**: MVI (Model-View-Intent) pattern. ViewModels expose a single immutable `StateFlow<UiState>` collected with `collectAsStateWithLifecycle()`.
- **Navigation**: Type-safe navigation routes via `kotlinx.serialization` `@Serializable` objects.
- **Design Tokens**: Standardized components from `:core:designsystem`.

### Domain Layer (UseCases & Domain Models)
- **Pure Kotlin**: 100% free of Android framework dependencies (`Context`, `Bundle`, etc.).
- **Immutability**: Models in `:core:model` use `@Immutable` annotations and `kotlinx.collections.immutable.ImmutableList` to ensure Compose skipping optimizations.
- **Result Wrapping**: Operations return typed `DataResult<T, DataError>` — exceptions are trapped and modeled as explicit errors.

### Data Layer (`:core:network`, `:core:database`)
- **Network Source**: Ktor 3.0 Client with OkHttp engine and `kotlinx.serialization` for JSON serialization.
- **Local Persistence**: AndroidX Room database with SQLite, exposing `Flow` for reactive query observing.
- **Repository Implementation**: Single source of truth pattern where the repository mediates between network and local cache.

---

## 2. Module Responsibility Matrix

| Module | Responsibility | Dependencies |
|---|---|---|
| `:app` | Application entry point, Hilt root, edge-to-edge configuration, global navigation host | All features, `:core:designsystem`, `:core:common` |
| `:feature:feed` | Trending repositories feed, language filtering, pull-to-refresh | `:core:model`, `:core:designsystem`, `:core:common`, `:core:network`, `:core:database` |
| `:feature:repository-detail` | Repository details, stats breakdown, markdown rendering | `:core:model`, `:core:designsystem`, `:core:common`, `:core:network` |
| `:feature:search` | Debounced search, recent query suggestions | `:core:model`, `:core:designsystem`, `:core:common`, `:core:network` |
| `:feature:bookmarks` | Offline bookmark persistence, tag assignment, local search | `:core:model`, `:core:designsystem`, `:core:common`, `:core:database` |
| `:core:common` | Coroutine dispatchers (`DispatcherProvider`), `DataResult`, `DataError`, `UiText` | Pure Kotlin / Android core |
| `:core:model` | Shared domain entities (`Repository`, `Owner`, `BookmarkTag`) | Pure Kotlin, `@Immutable` Compose annotations |
| `:core:network` | Ktor API service, DTOs, network mappers, `NetworkDataSource` | `:core:model`, `:core:common`, Ktor |
| `:core:database` | Room database, DAOs, entities, `LocalDataSource` | `:core:model`, `:core:common`, Room |
| `:core:designsystem` | Material 3 themes, typography, custom components, icons | Jetpack Compose, Material 3, Coil |

---

## 3. Why MVI over MVVM?

1. **Deterministic State Modeling**: The UI is a pure projection of `UiState`.
2. **Explicit User Intents**: Every user interaction is represented as a strongly-typed `Intent` (e.g., `FeedIntent.Refresh`, `FeedIntent.FilterLanguage(lang)`).
3. **Immutability Guarantees**: UI state classes are marked `@Immutable` to prevent accidental in-place mutation and maximize Compose recomposition pruning.

---

## 4. Offline-First Caching Strategy

```text
[Compose UI]
     │
     │ 1. Triggers FeedIntent.Refresh
     ▼
[FeedViewModel]
     │
     │ 2. Executes GetTrendingRepositoriesUseCase
     ▼
[FeedRepositoryImpl]
     ├──► 3. Emits cached data from Room immediately (instant UI rendering)
     │
     └──► 4. Fetches latest data from GitHub API via Ktor
               │
               ▼
          5. Upserts new records into Room database
               │
               ▼
          6. Emits fresh cache state to UI
```

If the device is offline or the network call fails:
- If cache exists, the user sees cached data seamlessly.
- If no cache exists, the pipeline yields `DataResult.Failure(DataError.NetworkError.NoInternet)` and the UI displays an `ErrorState` with a retry option.

---

## 5. Dependency Injection

DevHub relies on **Dagger Hilt** for dependency injection:
- `DispatcherProvider` is injected everywhere to avoid hardcoding `Dispatchers.IO` or `Dispatchers.Main`, enabling frictionless unit testing with `StandardTestDispatcher`.
- Singletons are scoped via `@Singleton` in their respective core modules (`NetworkModule`, `DatabaseModule`).
