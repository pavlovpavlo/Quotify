# Quotify

Android-застосунок для роботи з цитатами. Побудований на Jetpack Compose з модульною
архітектурою, MVI та Firebase-автентифікацією.

## Стек

- **Мова / UI:** Kotlin, Jetpack Compose
- **Архітектура:** багатомодульна (feature / core / data / domain), MVI, Clean Architecture
- **DI:** Hilt
- **Навігація:** Navigation 3 (`NavDisplay` + `NavigationCoordinator`, без `NavHost`)
- **Бекенд:** Firebase Auth (Email/Password + Google Sign-In), Firestore, Crashlytics
- **Медіа:** Cloudinary (завантаження фото профілю)
- **Локальне сховище:** DataStore (тема, мова, кеш профілю)
- **Інше:** Coroutines, Credential Manager, Accompanist
