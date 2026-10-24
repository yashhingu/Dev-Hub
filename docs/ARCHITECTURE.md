# Architecture notes

DevHub is an Android application split into `app`, `core`, and `feature` Gradle modules. Shared Gradle settings live in `build-logic/convention`. The repository is modular, but it does not configure Kotlin Multiplatform targets.

## Data flow

The Discover screen sends intents to `FeedViewModel`, which exposes `StateFlow<FeedUiState>`. `GetTrendingRepositoriesUseCase` delegates to `FeedRepositoryImpl`. The repository reads Room first, emits cached repositories when present, fetches from GitHub, writes the response to Room, and emits the refreshed snapshot. A failed request with existing cache leaves the cached content visible. A failed request without cache produces an error state.

The GitHub search endpoint is used as a trending approximation: repositories created within a recent time window, sorted by star count. It is not equivalent to GitHub's Trending page.

`RoomLocalDataSource` stores repository snapshots and bookmark records. A refresh keeps bookmark flags for repositories already in the database. Search history is a separate DataStore preference containing up to ten recent queries.

## Module boundaries

- `core/network` contains Ktor requests, response models, and mapping to app models.
- `core/database` contains Room entities, DAOs, and local data operations.
- `core/model` contains shared repository and owner models. It uses Compose stability annotations and persistent collections, so it is not a platform-independent domain module.
- `feature/*` modules contain screens, ViewModels, and use cases. They currently depend directly on the relevant core data modules.
- `app` owns the Hilt root, navigation graph, and bottom navigation.

The feature modules use a small intent/state pattern. It keeps UI events explicit, but does not implement a general MVI framework. Search results are fetched from the network; saved repositories remain available through the Bookmarks screen when offline.

## Known limits

- README rendering supports headings, fenced code blocks, bullets, blockquotes, rules, bold, italic, and inline code. Links, images, tables, and nested structures are not fully supported.
- The feed cache is a snapshot cache, rather than a continuously observed Room stream.
- GitHub API calls are unauthenticated and subject to rate limits.
- Automated coverage is limited to core result and mapping behavior plus feed cache behavior. There are no instrumented UI tests yet.
