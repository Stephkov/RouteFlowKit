# Repository Guidelines

## Project Structure & Module Organization

RouteFlowKit is a Kotlin Android project with two Gradle modules. `routeflowkit/` is the reusable library; its production code lives under `src/main/kotlin/com/example/routeflowkit/`, organized into `model`, `state`, `validation`, and `ui`. Keep Google Maps types internal so the public API remains platform-neutral. `app/` is the Compose demo application and depends on the library. Android resources are in each module's `src/main/res/`; local JVM tests use `src/test/`, and device tests use `src/androidTest/`. Central dependency versions are maintained in `gradle/libs.versions.toml`.

## Build, Test, and Development Commands

Run Gradle through the checked-in wrapper from the repository root:

- `.\gradlew.bat assembleDebug` builds debug artifacts for all modules.
- `.\gradlew.bat :routeflowkit:testDebugUnitTest` runs library JVM unit tests.
- `.\gradlew.bat test` runs all local unit-test tasks.
- `.\gradlew.bat connectedDebugAndroidTest` runs instrumentation tests on a connected emulator or device.
- `.\gradlew.bat :app:installDebug` installs the demo app for manual verification.

On macOS or Linux, replace `.\gradlew.bat` with `./gradlew`.

## Coding Style & Naming Conventions

Follow the official Kotlin style configured in `gradle.properties`: four-space indentation, trailing commas in multiline declarations, and concise expression-oriented code. Use `PascalCase` for classes, sealed-state variants, and composables; use `camelCase` for functions and properties. Name composables descriptively, such as `RouteFlowMap`. Keep packages under `com.example.routeflowkit`, and place new code in the matching feature package. Use Android Studio's Kotlin formatter before submitting changes.

## Testing Guidelines

JVM tests use JUnit 4; Android tests use AndroidX JUnit and Espresso. Name test classes after the subject (`RouteInputValidatorTest`) and use readable backtick test names describing behavior. Add regression tests for validation boundaries and preserve the `EC-*` identifiers documented in the validator and README. Run the focused library test task before opening a pull request.

## Configuration & Security

Set `MAPS_API_KEY=...` in untracked `local.properties`. Never commit API keys, signing material, generated `build/` output, or machine-specific IDE configuration.

## Commit & Pull Request Guidelines

Git history is unavailable in this checkout. Use short, imperative commit subjects, optionally scoped (for example, `validation: reject duplicate waypoints`). Pull requests should explain the change and verification performed, link relevant issues, and include screenshots or recordings for Compose UI changes. Call out public API changes and keep unrelated refactors separate.
