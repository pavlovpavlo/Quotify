# Quotify ProGuard / R8 rules.
#
# Release runs with `isMinifyEnabled = true`. The sections below keep type-safe
# Navigation routes, MVI contracts, Hilt-generated classes, Firestore DTOs and
# Crashlytics line info from being stripped or renamed.

# -- Stack traces ----------------------------------------------------------
-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*
-keepattributes InnerClasses, EnclosingMethod, Signature, Exceptions
-renamesourcefileattribute SourceFile

# -- Kotlinx Serialization -------------------------------------------------
# Required for the type-safe routes declared in `core/ui/.../Navigation.kt`
# (`@Serializable data object MainGraph.QuotesScreen`, etc.). Without these,
# R8 strips the generated `$serializer` and the app crashes on first navigate.
-keepclassmembers class **$$serializer { *; }

-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1> {
    static <1>$Companion Companion;
    static **$$serializer INSTANCE;
}

-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1>$Companion {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class kotlinx.serialization.** { *; }
-dontwarn kotlinx.serialization.**

# -- App @Serializable types -----------------------------------------------
# Navigation routes and any other domain-level Serializable types.
-keep @kotlinx.serialization.Serializable class com.kovhan.** { *; }

# -- Kotlin reflection / metadata (used by serialization + nav-compose) -----
-keep class kotlin.Metadata { *; }
-keep class kotlin.reflect.** { *; }
-dontwarn kotlin.reflect.**

# -- Hilt / Dagger ---------------------------------------------------------
# Most rules ship with Hilt consumer-rules; these protect generated
# `_HiltModules` shims that R8 sometimes trims overzealously.
-keep class **_HiltModules { *; }
-keep class **_HiltModules$* { *; }
-keepclassmembers class * {
    @dagger.hilt.android.lifecycle.HiltViewModel <init>(...);
}

# -- MVI contracts ---------------------------------------------------------
# State/Effect interfaces in `core:ui` are pure markers; their concrete
# subclasses are touched only via reflection from the BaseViewModel.
-keep interface com.kovhan.core.ui.UiState
-keep interface com.kovhan.core.ui.UiEffect
-keep class * implements com.kovhan.core.ui.UiState { *; }
-keep class * implements com.kovhan.core.ui.UiEffect { *; }

# -- DataStore -------------------------------------------------------------
-keep class androidx.datastore.*.** { *; }
-dontwarn androidx.datastore.**

# -- Compose runtime safety net --------------------------------------------
-keep class androidx.compose.runtime.** { *; }
-dontwarn androidx.compose.**

# -- Firebase / Crashlytics ------------------------------------------------
-keep class com.google.firebase.crashlytics.** { *; }
-dontwarn com.google.firebase.**

# -- Firestore DTOs --------------------------------------------------------
# `toObject`/`toObjects` and `set(dto)` map documents by *field name* via
# reflection. Renaming `textEn` to `a` silently yields default values instead
# of failing, so every DTO and its no-arg constructor must survive R8.
-keepclassmembers class com.kovhan.data.library.dto.** {
    <init>();
    <fields>;
}
-keep class com.kovhan.data.library.dto.** { *; }
-keepclassmembers class * {
    @com.google.firebase.firestore.PropertyName *;
}

# -- Credential Manager / Google Sign-In -----------------------------------
# Required by the Credential Manager docs: the Play Services provider is loaded
# reflectively, and the googleid option/credential travel through Bundles keyed
# by class-derived type strings.
-if class androidx.credentials.CredentialManager
-keep class androidx.credentials.playservices.** {
    *;
}
-keep class com.google.android.libraries.identity.googleid.** { *; }

# -- WebView JS bridge (uncomment when a JS interface is wired) ------------
#-keepclassmembers class com.kovhan.feature.webview.* {
#    public *;
#}
