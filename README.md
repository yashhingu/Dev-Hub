# DevHub

DevHub is a modular Android app for discovering GitHub repositories and saving them for later. I built it to explore Compose UI, local persistence, typed navigation, and a cached-first data flow in a small app that is easy to inspect.

## What works

- **Discover:** Browse popular repositories created recently, filter by language, and pull to refresh. The app displays saved feed results while refreshing or when a request fails.
- **Search:** Search GitHub repositories after a 300 ms pause in typing. Recent queries are stored on the device with DataStore.
- **Bookmarks:** Save repositories from the feed, search, or detail screen. Saved repositories are available offline through Room.
- **Details:** View repository statistics and the README. The custom viewer handles headings, code blocks, lists, quotes, rules, and a small subset of inline Markdown.

The Discover screen uses GitHub repository search, limited to recently created repositories and sorted by stars. Results can differ from GitHub's Trending page. The app makes unauthenticated API requests.

## Structure

```text
app/                       App entry point and navigation
build-logic/convention/    Shared Android and Compose Gradle plugins
core/common/               Result types and dispatcher abstraction
core/model/                Models shared by data and UI modules
core/network/              Ktor GitHub API client and DTO mapping
core/database/             Room cache and bookmarks
core/designsystem/         Theme and reusable Compose components
feature/feed/              Discover screen and cached-first repository
feature/search/            Search screen and local query history
feature/bookmarks/         Saved repository list
feature/repository-detail/ Repository details and README viewer
```

The app is Android-only today. Ktor and some Kotlin libraries can support a future multiplatform version, but no KMP target is configured.

## Build

Requirements: JDK 17 or newer, Android SDK 35, and an Android SDK path available through `ANDROID_HOME` or `local.properties`.

```bash
bash gradlew :app:assembleDebug
bash gradlew testDebugUnitTest
bash gradlew :app:lintDebug
```

On Windows, run `gradlew.bat` instead of `bash gradlew`. The debug APK is written to `app/build/outputs/apk/debug/`.

## Tests

Unit tests cover result mapping, GitHub DTO mapping, and cached feed behavior across refresh success and network failure. UI and device tests are future work.

## License

MIT. See [LICENSE](LICENSE).
