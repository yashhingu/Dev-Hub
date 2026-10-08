# 🤝 Contributing to DevHub

While DevHub is primarily a personal showcase project, I welcome discussions, suggestions, and even PRs if you spot something that could be improved!

## Code Style Guide

- **Kotlin Conventions**: Follow the official [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html).
- **Compose Rules**: Follow standard Compose best practices (e.g., hoisted state, stable parameters, modifier as first optional parameter).
- **Formatting**: The project uses `ktlint`. Please run `./gradlew ktlintFormat` before committing.

## Branch Naming Conventions

Please prefix your branches with the type of work:
- `feature/your-feature-name`
- `bugfix/issue-description`
- `refactor/what-is-being-refactored`
- `docs/updating-documentation`

## Pull Request Process

1. Fork the repository and create your branch from `main`.
2. Ensure your code follows the architecture guidelines (e.g., no Android dependencies in `:domain`).
3. Write unit tests for your changes, especially if modifying business logic in the `:domain` layer.
4. Open a PR with a clear description of the problem solved or feature added.
5. CI will run ktlint and the test suite automatically.

## Testing Requirements

- **Domain Layer**: Any new UseCase or business logic MUST have 100% unit test coverage using JUnit5 and MockK.
- **ViewModels**: State transitions and Intent handling should be tested using Turbine.
- **UI**: Complex custom Compose components should have basic preview tests or instrumented tests.

Thanks for taking a look at my code!
