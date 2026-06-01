# Consumer rules for :core:ui — applied automatically to any module that
# depends on this one. Keeps the type-safe Compose Navigation routes and MVI
# marker interfaces alive under R8.

# Type-safe Navigation routes (@Serializable data objects / classes)
-keep @kotlinx.serialization.Serializable class com.kovhan.core.ui.navigation.** { *; }
-keepclassmembers class com.kovhan.core.ui.navigation.**$$serializer { *; }

# MVI marker interfaces — concrete State/Effect classes live in feature modules
# but R8 may strip the parent interfaces if no code directly references them.
-keep interface com.kovhan.core.ui.UiState
-keep interface com.kovhan.core.ui.UiEffect
