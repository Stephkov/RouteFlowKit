# RouteFlowKit

An Android library that provides route-flow UI primitives on top of
Google Maps Compose — **without** exposing Google Maps types in its
public API.

> **Week 2 scope:** Public models, sealed UI state, input validation
> with unit tests, and a Compose demo app.  Navigation, real route
> calculation, live tracking, address search, and backend integration
> are **out of scope**.

---

## Quick Start

```bash
# 1. Clone
git clone <repo-url> && cd RouteFlowKit

# 2. Set your Google Maps API key
echo "MAPS_API_KEY=YOUR_KEY_HERE" >> local.properties

# 3. Build & run tests
./gradlew assembleDebug :routeflowkit:testDebugUnitTest
```

---

## Module Structure

```
RouteFlowKit/
├── app/                  # Demo app (Compose)
│   └── depends on :routeflowkit
└── routeflowkit/         # Android library module
    ├── model/            # GeoCoordinate, RouteWaypoint, RouteInfo
    ├── state/            # RouteFlowUiState sealed interface
    ├── validation/       # RouteInputValidator + ValidationResult
    └── ui/               # RouteFlowMap composable
```

---

## Public Models

| Class | Description |
|-------|-------------|
| `GeoCoordinate` | Latitude / longitude pair (no Google Maps dependency) |
| `RouteWaypoint` | Label + `GeoCoordinate` |
| `RouteInfo` | Polyline points, distance, duration (stub) |

Google Maps `LatLng` is used **only internally** via `Mappers.kt`
(`internal` visibility).

---

## RouteFlowUiState

| Variant | When |
|---------|------|
| `Ready` | Map visible and idle; optional origin/destination/route |
| `Loading` | A route or location request is in progress |
| `DestinationRequired` | User must supply a destination |
| `RouteUnavailable` | No route found between origin and destination |
| `LocationPermissionRequired` | App needs location permission |
| `LocationServicesDisabled` | Device GPS / network location is off |
| `Error` | Unrecoverable error with message and optional cause |

---

## Edge-Case Catalogue

Every edge case has an **ID** that appears in the validator source,
the test file, and this table.

| ID | Rule | Implementation | Test |
|----|------|----------------|------|
| EC-1 | Latitude must be in [−90, 90] | [RouteInputValidator.kt (`validateCoordinate`)](routeflowkit/src/main/kotlin/com/example/routeflowkit/validation/RouteInputValidator.kt) | [RouteInputValidatorTest.kt (`latitude above 90`, `latitude below -90`)](routeflowkit/src/test/kotlin/com/example/routeflowkit/validation/RouteInputValidatorTest.kt) |
| EC-2 | Longitude must be in [−180, 180] | [RouteInputValidator.kt (`validateCoordinate`)](routeflowkit/src/main/kotlin/com/example/routeflowkit/validation/RouteInputValidator.kt) | [RouteInputValidatorTest.kt (`longitude above 180`, `longitude below -180`)](routeflowkit/src/test/kotlin/com/example/routeflowkit/validation/RouteInputValidatorTest.kt) |
| EC-3 | Latitude / Longitude must not be NaN | [RouteInputValidator.kt (`validateCoordinate`)](routeflowkit/src/main/kotlin/com/example/routeflowkit/validation/RouteInputValidator.kt) | [RouteInputValidatorTest.kt (`NaN latitude`, `NaN longitude`)](routeflowkit/src/test/kotlin/com/example/routeflowkit/validation/RouteInputValidatorTest.kt) |
| EC-4 | Latitude / Longitude must not be ±Infinity | [RouteInputValidator.kt (`validateCoordinate`)](routeflowkit/src/main/kotlin/com/example/routeflowkit/validation/RouteInputValidator.kt) | [RouteInputValidatorTest.kt (`positive infinity latitude`, `negative infinity longitude`)](routeflowkit/src/test/kotlin/com/example/routeflowkit/validation/RouteInputValidatorTest.kt) |
| EC-5 | Waypoint label must not be blank | [RouteInputValidator.kt (`validateWaypoint`)](routeflowkit/src/main/kotlin/com/example/routeflowkit/validation/RouteInputValidator.kt) | [RouteInputValidatorTest.kt (`blank label`, `whitespace-only label`)](routeflowkit/src/test/kotlin/com/example/routeflowkit/validation/RouteInputValidatorTest.kt) |
| EC-6 | Origin and destination must not be identical | [RouteInputValidator.kt (`validateOriginDestination`)](routeflowkit/src/main/kotlin/com/example/routeflowkit/validation/RouteInputValidator.kt) | [RouteInputValidatorTest.kt (`identical origin and destination`)](routeflowkit/src/test/kotlin/com/example/routeflowkit/validation/RouteInputValidatorTest.kt) |
| EC-7 | Waypoint list must contain ≥ 2 points | [RouteInputValidator.kt (`validateWaypointList`)](routeflowkit/src/main/kotlin/com/example/routeflowkit/validation/RouteInputValidator.kt) | [RouteInputValidatorTest.kt (`single-element list`, `empty list`)](routeflowkit/src/test/kotlin/com/example/routeflowkit/validation/RouteInputValidatorTest.kt) |
| EC-8 | Waypoint list must not contain duplicate coordinates | [RouteInputValidator.kt (`validateWaypointList`)](routeflowkit/src/main/kotlin/com/example/routeflowkit/validation/RouteInputValidator.kt) | [RouteInputValidatorTest.kt (`duplicate coordinates in list`)](routeflowkit/src/test/kotlin/com/example/routeflowkit/validation/RouteInputValidatorTest.kt) |

---

## Demo App

The `:app` module shows a `DemoScreen` with:

- A `RouteFlowMap` (Google Map with origin/destination markers) in the
  **Ready** state.
- A horizontal chip bar to switch between all 7 `RouteFlowUiState`
  variants.

---

## License

TBD
