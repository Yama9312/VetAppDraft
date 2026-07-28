# Working in this repo

Android app, Kotlin + Jetpack Compose, single activity, single module.

## Commands

```sh
./gradlew assembleDebug              # build
./gradlew testDebugUnitTest          # JVM tests — run these before claiming anything works
./gradlew lintDebug                  # lint
./gradlew connectedDebugAndroidTest  # instrumented tests (needs a device/emulator)
./gradlew assembleRelease            # minified release
```

## Layout

| Package | Holds |
|---|---|
| `domain/` | `Branch`, `MusicPreference`, `Step`, `StepId`, `StepAction`, `BreathingPattern`. No Android dependencies beyond annotations. |
| `data/` | Room entity/DAO/database, `Converters`, migrations, `StepCatalog`, `VetRepository`. |
| `ui/theme/` | `Color.kt` (six branch palettes × light/dark), `Type.kt`, `Dimens.kt`, `Theme.kt`. |
| `ui/components/` | `CrisisButton`, `AudioController`, `BreathingPacer`. |
| `ui/screens/` | One file per screen, all stateless — state comes in as parameters, events go out as lambdas. |
| `ui/VetViewModel` | Activity-scoped. Owns the step position. |
| `ui/VetApp` | Navigation graph and chrome. |

## Conventions

- **Screens are stateless.** They take state and emit callbacks; only `VetViewModel` touches the repository. Keeps them previewable and testable.
- **All user-facing text goes in `strings.xml`.** Spanish localisation is a stated project requirement, so no hardcoded strings in composables.
- **Step behaviour keys off `StepId` and `StepAction`, never display text.** The previous version selected a breathing animation by comparing the step's visible text; that broke on rewording and made translation impossible.
- **Clinical content is not ours to edit.** Step titles and bodies in `strings_steps.xml` were authored by the project's psychologist. Rewording needs the content owner, not a judgement call.

## Non-negotiables

These are requirements, not preferences. Changing them needs a real reason.

- **Touch targets ≥ `Dimens.MinTouchTarget` (56dp).** Above Material's default, on purpose — the audience is in crisis and skews older.
- **Contrast ≥ AA, body text ≥ AAA.** `ContrastTest` fails the build otherwise. If you add a colour, add it to the scheme so the test sees it.
- **Nothing leaves the app without an explicit tap.** The old music players called `startActivity` from `onViewCreated`.
- **Dial with `ACTION_DIAL`, never `ACTION_CALL`.** No `CALL_PHONE` permission; the system dialler confirms.
- **Emergency reachable from every screen** except first-run setup.
- **Never `fallbackToDestructiveMigration()`.** Write a real `Migration`. The table holds a clinician-configured safety plan.
- **Crisis numbers**: 911, 988 (press 1), text 838255. Verify before changing — the original code shipped the pre-2022 Lifeline number and a placeholder police number.

## Adding a step

1. Add a `StepId` constant — **append, don't insert**. `MIGRATION_2_3` maps legacy positional indices onto `StepCatalog.steps` by position, and `StepCatalogTest` pins that order.
2. Add title/body to `strings_steps.xml`.
3. Add the `Step` to `StepCatalog.steps` with its `StepAction`s.

Audio goes in `res/raw` as mono 64 kbps `.m4a` — never commit WAV, `.gitignore` blocks it.
