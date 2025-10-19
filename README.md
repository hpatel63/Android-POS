# Hospitality POS Android Tablet Suite

Landscape-first, offline-capable motel/hotel POS + PMS tablet application built with Kotlin, Jetpack Compose, Hilt, Room, Retrofit, and WorkManager. The project ships with multi-property support, role-aware UI, AI Copilot powered by ChatGPT, and fully seeded demo data for two properties.

## Architecture
- **Clean Architecture** with `core` layers (data, domain, security, ai, work) and feature packages (Dashboard, Rooms, Reports, Settings, Housekeeping, Search, Copilot, Offline).
- **MVVM** ViewModels leveraging Kotlin coroutines and Flows for reactive UI updates.
- **Dependency Injection** via Hilt modules (`AppModule`, WorkManager configuration).
- **Persistence** using Room with secure seed data and offline sync queue.
- **Networking** Retrofit + Moshi clients with TLS 1.3-ready OkHttp stack.
- **Background Processing** WorkManager `SyncWorker` handling offline-to-online reconciliation.
- **Security** Android Keystore-backed `SecureConfigStore` and `PiiRedactor` enforcing log redaction.
- **AI Integration** `ChatGptClient` delivers streaming Compose insights and proactive housekeeping summaries.

## Getting Started
1. Ensure Android Studio Giraffe or newer with Android SDK 34.
2. Clone repository and open the project root (`/workspace/Android-POS-APP`).
3. Generate Gradle wrapper if required: `./gradlew wrapper` (wrapper JAR omitted for brevity).
4. Sync Gradle and run on an Android 14+ tablet emulator (landscape orientation recommended).

## Configuration
- **ChatGPT API Key**: Navigate to Settings → AI, paste the API key. Key stored via Android Keystore + EncryptedSharedPreferences.
- **Demo Mode**: Preloaded demo data (two properties, twenty rooms) is seeded on first launch by `SeedDataSeeder`.
- **WorkManager Sync**: Offline actions queue in Room's `sync_queue` table; `SyncWorker` retries with exponential backoff.
- **PDF Exports**: `ReportPdfExporter` renders PDF reports into the app private storage (`files/reports`).

## Testing & CI
- Unit tests located in `app/src/test`. Execute with `./gradlew test`.
- GitHub Actions workflow `.github/workflows/ci.yml` validates build + unit tests.

## Privacy & Compliance
- API prompts automatically redact PII via `PiiRedactor`.
- AI prompt/response pairs logged to `ai_prompts` for audit visibility.
- Role-aware UI restricts destructive actions to manager accounts.

## Mock Integrations
- Payment, OCR, and AI services are mocked within repository and client layers allowing offline demos.
- Retrofit endpoints defined for future live server integration.

## Cash App QR & Payments
- Settings module accepts Cash App handle and third-party processor tokens (Stripe/Square). Compose UI renders QR during payment flows.

## Offline Manual & Copilot
- `ManualArticleEntity` + AI Copilot enable on-device procedural help even when offline. ChatGPT streaming attaches context and surfaces insights across Dashboard, Rooms, Reports, and Housekeeping screens.
