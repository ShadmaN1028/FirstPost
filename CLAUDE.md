# Project context — read before doing anything

## What we are building

A single-device Android demo for a take-home task. The company's app (VMP
Messenger) has a problem: only 20% of new users make a post within 24 hours of
signing up. The goal is 50%.

Research on the real app found why: posting is never mentioned during a
12-screen signup, new users finish onboarding with zero friends, the post
composer is an empty box, and a first post gets no reactions. A first post
feels like shouting into an empty room.

This demo shows the fix, "First Post Welcome":

1. **Say hello step** — a new final onboarding step (08/08). The profile
   photo the user uploaded in step 01 is turned into a first post, shown as a
   preview exactly as it will appear in the feed: a "profile picture update"
   post, like Facebook's "Azoth updated their profile picture", with the
   photo and a pre-filled caption a new user might write, e.g. "Hello
   people! I'm Azoth, new here 👋". The caption is editable. One tap to post,
   plus a "Skip for now" option.
2. **Boost + reactions** — the first post appears at the top of the feed in a
   "New members" section and receives synthetic likes and comments within the
   first minute (time-compressed for the demo).
3. **Reaction notification** — a real Android system notification plus an
   in-app notification: "3 people reacted to your first post".

Everything else is a faithful replica of VMP's current signup and feed, so the
new step is seen in context.

## Hard constraints

**No backend.** No Firebase, no network calls, no database. All data is
synthetic and hardcoded. All state lives in memory. Losing state on restart is
correct behaviour.

**One device, one real user.** Other users, posts, stories, friends and
reactions are synthetic. Reactions arrive on timers.

**Do not upgrade the toolchain.** AGP 9.0.1, compileSdk 36, minSdk 28. If the
template pulls androidx versions that need AGP 9.1.0 or compileSdk 37, they are
pinned in `gradle/libs.versions.toml` to: coreKtx 1.15.0, lifecycleRuntimeKtx
2.8.7, activityCompose 1.9.3, espressoCore 3.6.1, junitVersion 1.2.1. Never
bump AGP, compileSdk or any androidx version. If a new dependency conflicts,
pin that dependency down instead. All versions go in the version catalog.

**Build only the section you are given.** Sections come one at a time.
Structure code so later sections slot in, but never build ahead.

**Explain as you go.** After each file, give a short plain-English rundown of
what it does and why. I must be able to explain the code myself.

**Ask before restructuring.** No silent refactors.

## Code organisation

- Small, single-purpose files. Past ~150 lines, split.
- Composables are dumb: render state, emit events. No timers or logic inside
  a `@Composable`.
- One ViewModel per screen or flow, exposing a single `StateFlow` of an
  immutable UI state data class.
- All synthetic data lives in `data/`. All timings, counts and caption
  templates live in `Constants.kt`.
- One shared in-memory `AppRepository` holds the current user, posts,
  friends and notifications, so every screen stays in sync.

### Target package layout

```
com.shadman.firstpost/
├── MainActivity.kt
├── Constants.kt
├── data/
│   ├── Models.kt              // User, Post, Story, Notification, Reaction
│   ├── SyntheticData.kt       // seeded users, posts, stories, comments
│   └── AppRepository.kt       // shared in-memory state
├── onboarding/                // welcome, mock Google sign-in, steps 01–08
├── circle/                    // Build your circle
├── paywall/                   // plan picker (visual only, no billing)
├── feed/                      // feed, stories row, New members, composer
├── profile/                   // own profile, shows own posts
├── notifications/             // in-app list, ReactionScheduler, system notification
├── navigation/                // routes + bottom nav
└── ui/theme/                  // fixed dark theme, no Material You
```

## The full flow, in order

1. Welcome screen → "Sign in with Google" (mock account picker, no real auth)
2. 01/08 Add a profile photo (gallery picker; skip allowed)
3. 02/08 What's your name?
4. 03/08 Choose a username
5. 04/08 Add a short bio (skip allowed)
6. 05/08 Your gender (skip allowed)
7. 06/08 Date of birth
8. 07/08 Interested in (skip allowed)
9. **08/08 Say hello — NEW.** Preview of a "<Name> updated their profile
   picture" post with the step-01 photo and a pre-filled, editable caption
   using their name. Buttons: "Post" and "Skip for now". If no photo was
   added, a text-only post with the same caption. In the feed this post
   renders with the "updated their profile picture" header line.
10. Build your circle — 5 synthetic people pre-ticked, as in the original
11. Paywall — visual replica, X closes it, no billing
12. Notification permission — the real Android 13 POST_NOTIFICATIONS request
13. Feed

The step counter shows /08 because Say hello is part of the flow.

## Reference screenshots

I will attach screenshots of the real VMP app with some sections. Copy their
layout, spacing, colours and wording. Do not copy other users' names, photos
or posts from them — all synthetic users and content must be invented.

## Visual bar

Match VMP's look from the screenshots: dark navy background, blue accent,
rounded cards, step counter "01 / 08" with a progress bar, bottom nav with
Home, Chats, Calls, Friends, Marketplace, Settings. Only Home (feed), Friends
and Profile are functional; other tabs are simple placeholders. System
sans-serif font.

## Out of scope

- Real authentication, real billing, real contacts
- Chats, calls, marketplace, dating, live
- A "before" version of the flow
- DI frameworks, multi-module Gradle, use-case layers

## Sections (orientation only — wait to be given each one)

1. Theme, models, synthetic data, repository, navigation shell
2. Welcome + mock Google sign-in
3. Onboarding steps 01–07
4. Say hello step (08) — the core of the demo
5. Build your circle + paywall + notification permission
6. Feed, stories, New members section, composer, own profile
7. Boost, timed reactions, in-app + system notification
8. Polish
