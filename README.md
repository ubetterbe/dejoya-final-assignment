# NIT3213 Final Assessment - DeJoya

Android app for my NIT3213 final assessment. Three screens - Login, Dashboard,
and Details - that log into a real API and pull back data tied to my
assigned topic (sports).

## Overview

- **Login** (`LoginActivity`) - a separate Activity, not part of the nav
  graph. Takes a student ID + first name, calls the auth endpoint, and gets
  back a keypass used for the rest of the session.
- **Dashboard** and **Details** - Fragments hosted in `MainActivity` via the
  Navigation Component. Dashboard shows a RecyclerView list of entities
  fetched with the keypass; tapping one navigates to Details with that
  entity's full info passed through as Bundle args (no Safe Args, just plain
  Bundle keys).

Tech stack:
- Kotlin
- XML layouts + ViewBinding (no Compose, no Jetpack Compose UI)
- MVVM - ViewModel + LiveData exposing sealed UI state (Idle/Loading/Success/Error)
- Koin for DI (wires up Retrofit/OkHttp/ApiService and the ViewModels)
- Retrofit + OkHttp + Gson for networking
- Navigation Component for Dashboard <-> Details
- Material 3 theme (navy and gold, tied to the olympicSport field in the data)

## How to build and run

1. Clone the repo
2. Open it in Android Studio
3. Let Gradle sync (pulls all the dependencies below automatically)
4. Run on an emulator or physical device - minSdk is 27, so anything API 27+
   works

No API keys or local config needed, the base URL is hardcoded in
`NetworkConfig.kt`.

## Before you run it (important)

If login just times out the first time you open the project, don't panic.
For me, it's just like that sometimes, so I usually open
https://nit3213apinew.onrender.com/ in a browser tab first, or hit the login/
dashboard endpoints once in Postman (POST to /footscray/auth, then GET to
/dashboard/{keypass}). Give it a bit, then launch the app - works fine after
that.

## Login credentials

Heads up - the format here does NOT match what the original assignment brief
PDF says. The professor sent a correction partway through swapping which
field means what, so this is what actually works against the real API:

- **Username field:** your student ID, but WITHOUT the leading "s"
  (e.g. `12345678`, not `s12345678`)
- **Password field:** your first name, and it's case-sensitive

If login keeps failing with a 404, this mismatch is the first thing to check.

## API info

- Base URL: `https://nit3213apinew.onrender.com/`
- This is hosted on Render's free tier, which spins down when idle. The
  first request after a while can take 30-60 seconds to come back while the
  instance wakes up - that's expected, not a bug. `NetworkConfig` sets
  generous connect/read timeouts (20s/30s) specifically for this, and both
  ViewModels catch the resulting `IOException` and show a "server might be
  waking up, try again in a moment" message instead of a generic error.

## Running tests

./gradlew test

These are local JVM unit tests only (no emulator needed) covering
`LoginViewModel` and `DashboardViewModel` - success path and both failure
paths (bad HTTP response, no response at all) for each. Built with JUnit,
MockK (faking `ApiService`), and kotlinx-coroutines-test (driving
`viewModelScope` with `StandardTestDispatcher`).

## Project structure

data/
  model/      - plain data classes matching the real API shapes (LoginRequest,
                LoginResponse, DashboardResponse, Entity) - confirmed against
                Postman, not the assignment brief's placeholder field names
  remote/     - ApiService (Retrofit interface) and NetworkConfig (base URL,
                timeouts)
di/           - AppModule, the one Koin module wiring up OkHttp/Retrofit/
                ApiService and both ViewModels
ui/
  login/      - LoginActivity + LoginViewModel
  dashboard/  - DashboardFragment + DashboardViewModel + EntityAdapter
                (RecyclerView adapter for the entity list)
  details/    - DetailsFragment only, no ViewModel - it just displays data
                handed to it via Bundle args, there's no async work happening
                on this screen so a ViewModel would just be a pass-through

`MainActivity` just hosts the NavHostFragment for Dashboard/Details - all
the actual screen logic lives in the Fragments themselves. `DeJoyaApp`
starts Koin before anything else needs it.

## Known limitations / notes

- Styling was intentionally left basic until functionality was confirmed
  working end-to-end - got the login -> dashboard -> details flow talking to
  the real API first, then went back for a visual pass.
- Dashboard/Details navigation uses plain Bundle args instead of the
  Navigation Safe Args plugin - simpler to wire up for a project this size.
