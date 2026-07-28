# Veterans Safety S.O.P.

An Android app that walks a veteran through their personal safety plan during a mental
health crisis.

The concept comes from Kristoffer Rehder, a veteran and clinical psychology PhD candidate
who worked in inpatient psychiatry at the Portland VA. The app **is** the safety plan: a
veteran and their clinician agree on a sequence of coping steps in advance, and in the
moment the app walks through them one at a time. It is deliberately not a wellness app
you browse.

Built at Pacific University's Berglund Center. Requirements and clinical content are the
project team's; this repository is the Android implementation.

---

## Design constraints

These are not stylistic preferences. They come from the project requirements and they
drive most of the decisions in this codebase.

> "There are no extra functions or buttons to press that may confuse the user."

> "Too many buttons to push, options to pick from — not set up for a veteran experiencing
> a mental health crisis."

> "Simplicity is crucial both for Veterans of older age where technology is not a strong
> suit and in general to limit stressors when a crisis is occurring."

Concretely:

- **Minimum touch target 56dp**, above Material's 48dp default.
- **Step text at 34sp, body at 22sp**, all in `sp`, laid out to survive 200% system font
  scale.
- **WCAG AA everywhere, AAA for body text**, enforced by a unit test that fails the build
  if a colour changes to something unreadable.
- **Every action is a labelled button.** No gesture-only affordances, no hidden menus, no
  icon without a content description.
- **Emergency is one tap from every screen.**
- **Nothing leaves the app without an explicit tap.**

---

## Architecture

Single-activity Compose app, Kotlin throughout.

```
edu.pacificu.berglund.vetapp
├── domain/          Branch, MusicPreference, Step, StepId, StepAction, BreathingPattern
├── data/            Room entity/DAO/database, migrations, StepCatalog, VetRepository
└── ui/
    ├── theme/       Colour tokens, six branch palettes x light/dark, type scale, dimens
    ├── components/  CrisisButton, AudioController, BreathingPacer
    ├── screens/     Sop, Emergency, Settings, StepOrder, Appearance, Music, Setup, End
    ├── VetViewModel Shared at activity scope — owns the step position
    └── VetApp       Navigation graph and app chrome
```

**Why one activity-scoped ViewModel.** The step position lives there rather than in a
screen argument. That is what makes leaving for Settings or Emergency and coming back
resume on the step the veteran was actually on — the single most important behaviour in a
crisis tool, and the thing the previous fragment-based version got wrong.

**Why steps declare actions instead of describing themselves.** A `Step` carries a stable
`StepId` and a list of `StepAction`s. Behaviour never keys off display text, so rewording
a step cannot break its animation and the app can be translated (the requirements call for
English and Spanish).

**Why no dynamic colour.** The branch palette is a documented requirement and doubles as a
mitigation — the project notes that some colours may remind veterans of service in an
unwelcome way, so the choice stays the veteran's rather than the wallpaper's.

---

## Data and privacy

Everything is on-device. There is no network backend, no analytics, and no account.

`android:allowBackup="false"` is deliberate: the database holds a clinician-configured
safety plan and an emergency contact, and the project documentation raises HIPAA/PHI
handling as an open question without ever resolving it. Until that is settled, nothing
syncs to Google Drive backup.

Room migrations are real (`MIGRATION_2_3`), not `fallbackToDestructiveMigration()`. Wiping
a veteran's safety plan on a schema change is the worst failure mode this app has.

---

## Crisis contacts

- **911** — emergency services
- **988, press 1** — Veterans Crisis Line
- **Text 838255** — Veterans Crisis Line by text
- The veteran's own emergency contact, captured at setup

All dialling uses `ACTION_DIAL`, which hands off to the system dialler and requires no
permission. The app holds no `CALL_PHONE` permission.

> If you are a veteran in crisis, call or text **988** and press **1**.

---

## Build

```sh
./gradlew assembleDebug      # debug APK
./gradlew testDebugUnitTest  # unit tests
./gradlew lintDebug          # lint
./gradlew assembleRelease    # minified release APK
```

Requires JDK 17+. Gradle 8.9 / AGP 8.7.3 / Kotlin 2.0.21, `compileSdk` 35, `minSdk` 24.

`minSdk` stays at 24 on purpose: the audience skews older and is likelier to be on an
ageing phone. Raising it would drop real users.

### Audio assets

Narration ships as mono 64 kbps AAC in `app/src/main/res/raw`. The masters are
uncompressed WAV and are **not** in the repository (`.gitignore` excludes `*.wav`) — they
live with the project's recording archive. To regenerate:

```sh
ffmpeg -i in.wav -c:a aac -b:a 64k -ac 1 -ar 44100 -movflags +faststart out.m4a
```

`R.raw.*` identifiers are extension-independent, so swapping the format needs no code
change.

---

## Not implemented

Named honestly, because these are real requirements that were never built rather than
things that were forgotten:

- **Skill Toolbox** — browsing skills by category (Meditations, Breathing, Craving, Anger,
  PTSD, Self-Harm) plus an alphabetical master list. Specified in both the 2018 and 2024
  designs. The previous version rendered a Settings button for it whose click listener was
  commented out, so it did nothing; that button is gone rather than left as a dead control.
- **FRAGO / mid-crisis SOP switching** — branching into a different plan if the nature of
  the crisis changes.
- **Multiple SOPs** — only the Safety plan exists. The requirements also define Sobriety
  and Combat Stress (PTSD) plans.
- **Emergency contact photos** — the wireframe wants a photo tile that acts as the call
  button, falling back to the name. Only a single number is stored.
- **Non-emergent police number** — previously a hardcoded placeholder, `123-456-7890`,
  which would have dialled a stranger. Removed. A configurable secondary contact is the
  right fix and is not built.
- **User-supplied media** — the "review reasons for living" photo slideshow and the
  personal distraction list.
- **Spanish localisation** — all user-facing strings are externalised and ready, but no
  `values-es` exists.
- **Skill graphics** — roughly eleven skills were specified with artwork that was never
  produced (stream loop, ocean/surfer, Mount Hood, interactive values list, fidget
  spinner). The two breathing pacers are implemented, drawn in Compose.
- **Instrumented tests** — unit tests cover the catalog, migration mapping and contrast.
  Compose UI and Room migration tests need a device or emulator.

---

## Licence and status

Not currently licensed, patented or published. Prototype quality by intent — this is a
university innovation-centre project, not a released medical device, and it has not been
clinically validated.
