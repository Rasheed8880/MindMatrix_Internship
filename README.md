# Namma Poster (Jetpack Compose + Kotlin)

Production-ready Android Studio project using:
- Kotlin
- Jetpack Compose (Material3) — **no XML UI**
- MVVM + Clean Architecture
- Coroutines + StateFlow
- Room (local posters)
- Retrofit + Gson (Gemini Pro REST API)
- Coil (image loading)
- Firebase Authentication for login and registration

## Where to put the Gemini API key (mandatory)

1. Open the project root `local.properties` (same level as `settings.gradle.kts`).
2. Add:

```
GEMINI_API_KEY=your_key_here
```

3. Sync Gradle. The key is injected into `BuildConfig.GEMINI_API_KEY`.

## Run instructions (Android Studio)

1. **Open project**: Android Studio -> Open -> select this folder.
2. **Add API key**: edit `local.properties` as above.
3. **Sync**: "Sync Project with Gradle Files".
4. **Emulator**: Tools -> Device Manager -> Create device -> Download system image -> Start.
5. **Run**: select the app configuration -> Run.

## App features

- Poster editor canvas:
  - Add text (tap text to edit)
  - Change font size + color
  - Add image from gallery
  - Drag/move elements
  - Change background color
- AI copy generation:
  - "Generate AI Text" → Gemini Pro API → adds a text element
  - Loading + error states handled
- Local storage:
  - Save poster content to Room DB (`PosterEntity`)
  - View/delete saved posters
- Login/register:
  - Email/password validation
  - Firebase Authentication using the configured Jal Firebase project
- Export:
  - Captures the Compose canvas to a bitmap (view capture + crop)
  - Saves to Gallery via `MediaStore`
  - Runtime permission for API 28 and below

## Debugging (common issues)

- **Missing `GEMINI_API_KEY`**
  - Ensure `local.properties` contains `GEMINI_API_KEY=...`
  - Re-sync Gradle so `BuildConfig` updates

- **401/403 from Gemini**
  - Verify the key is valid and enabled for Generative Language API in Google Cloud

- **"Empty response from AI"**
  - Temporary API behavior; try again or adjust prompt text

- **Image not loading**
  - Ensure you picked an image from a document provider that returns a valid `content://` URI

- **Saving poster fails on Android 9 and below**
  - Grant storage permission when prompted

