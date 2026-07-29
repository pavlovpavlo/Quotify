package com.kovhan.core.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import kotlin.reflect.KClass

@ActivityRetainedScoped
class NavigationCoordinator @Inject constructor() {

    val backStack: SnapshotStateList<NavKey> = mutableStateListOf()

    /**
     * A destination requested from outside the app (e.g. the home-screen widget).
     * Consumed once the back stack reaches a top-level key so the deep link never
     * interrupts splash / auth. Observable so it fires even when the app is
     * already foregrounded on a top-level screen.
     */
    var pendingDeepLink: NavKey? by mutableStateOf(null)
    val bottomSheetStack: SnapshotStateList<BottomSheetKey> =
        mutableStateListOf(PlaceholderBottomSheetKey)
    val dialogStack: SnapshotStateList<DialogKey> = mutableStateListOf(PlaceholderDialogKey)

    val currentKey: NavKey?
        get() = backStack.lastOrNull()

    val currentBottomSheet: BottomSheetKey?
        get() = bottomSheetStack.lastOrNull()?.takeIf { it !is PlaceholderBottomSheetKey }

    val currentDialog: DialogKey?
        get() = dialogStack.lastOrNull()?.takeIf { it !is PlaceholderDialogKey }

    private val resultChannels = mutableMapOf<String, Channel<Any?>>()

    private data class OwnedBottomSheet(
        val key: BottomSheetKey,
        val ownerKey: NavKey?,
    )

    private val ownedBottomSheets = mutableListOf<OwnedBottomSheet>()

    private data class SavedOverlayState(
        val targetKey: NavKey,
        val bottomSheets: List<OwnedBottomSheet>,
        val dialogs: List<DialogKey>,
    )

    private val savedOverlayStacks = mutableListOf<SavedOverlayState>()

    private val topLevelKeys = mutableSetOf<KClass<out NavKey>>()
    private val dialogPreservingKeys = mutableSetOf<KClass<out NavKey>>()

    // ----------------------------------------------------------------
    // RESULT CHANNELS
    // ----------------------------------------------------------------

    fun <T> observeResult(resultKey: String): Flow<T?> {
        val channel = resultChannels.getOrPut(resultKey) {
            Channel(capacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
        }
        @Suppress("UNCHECKED_CAST")
        return channel.receiveAsFlow() as Flow<T?>
    }

    suspend fun <T> emitResult(resultKey: String, result: T) {
        resultChannels.getOrPut(resultKey) {
            Channel(capacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
        }.send(result)
    }

    /**
     * Drops any buffered value for [resultKey]. Result channels buffer the last
     * emitted value (capacity 1), so a result emitted while nobody was observing
     * — e.g. creating a collection from the library — would otherwise be
     * delivered to the next observer that mounts. Consumers that only care about
     * results produced during their own lifetime should drain first.
     */
    fun clearResult(resultKey: String) {
        val channel = resultChannels[resultKey] ?: return
        do {
            val outcome = channel.tryReceive()
        } while (outcome.isSuccess)
    }

    // ----------------------------------------------------------------
    // REGISTRATION
    // ----------------------------------------------------------------

    fun registerTopLevelKey(keyClass: KClass<out NavKey>) = topLevelKeys.add(keyClass)

    fun registerTopLevelKeys(vararg keyClasses: KClass<out NavKey>) =
        topLevelKeys.addAll(keyClasses)

    fun registerDialogPreservingKey(keyClass: KClass<out NavKey>) =
        dialogPreservingKeys.add(keyClass)

    fun registerDialogPreservingKeys(vararg keyClasses: KClass<out NavKey>) =
        dialogPreservingKeys.addAll(keyClasses)

    // ----------------------------------------------------------------
    // KEY TYPE CHECKS
    // ----------------------------------------------------------------

    fun isTopLevelKey(key: NavKey): Boolean = key::class in topLevelKeys

    private fun isDialogPreservingKey(key: NavKey): Boolean = key::class in dialogPreservingKeys

    fun shouldShowBottomBar(): Boolean = currentKey?.let { isTopLevelKey(it) } ?: false

    // ----------------------------------------------------------------
    // INITIALIZATION
    // ----------------------------------------------------------------

    private var isInitialized = false

    fun initialize(startKey: NavKey) {
        if (isInitialized && backStack.isNotEmpty()) return

        if (!isInitialized) {
            registerTopLevelKeys(
                HomeKey::class,
                QuotesKey::class,
                FavoritesKey::class,
                ProfileKey::class,
            )
        }

        backStack.clear()
        backStack.add(startKey)
        isInitialized = true
    }

    fun setStartDestination(key: NavKey) {
        backStack.clear()
        backStack.add(key)
        isInitialized = true
    }

    fun reset() {
        backStack.clear()
        ownedBottomSheets.clear()
        bottomSheetStack.clear()
        bottomSheetStack.add(PlaceholderBottomSheetKey)
        dialogStack.clear()
        dialogStack.add(PlaceholderDialogKey)
        savedOverlayStacks.clear()
        resultChannels.clear()
        isInitialized = false
    }

    // ----------------------------------------------------------------
    // SCREEN NAVIGATION
    // ----------------------------------------------------------------

    fun navigate(
        key: NavKey,
        popUpTo: NavKey? = null,
        inclusive: Boolean = false,
        restoreBottomSheets: Boolean = false,
    ) {
        val returnTarget = backStack.lastOrNull()

        if (isDialogPreservingKey(key)) {
            saveCurrentOverlayIfNeeded(returnTarget)
        } else {
            if (restoreBottomSheets && returnTarget != null) {
                saveCurrentOverlayIfNeeded(returnTarget)
            }
            if (popUpTo != null) {
                popBackStackInternal(
                    findIndex = { backStack.indexOfLast { it == popUpTo } },
                    inclusive = inclusive,
                    restoreOverlayForNewTop = false,
                )
            } else {
                clearBottomSheets()
                clearDialogs()
            }
        }

        if (isTopLevelKey(key)) {
            navigateToTab(key)
        } else {
            if (backStack.lastOrNull() != key) {
                backStack.add(key)
            }
        }
    }

    fun navigate(
        key: NavKey,
        popUpToClass: KClass<out NavKey>,
        inclusive: Boolean = false,
        restoreBottomSheets: Boolean = false,
    ) {
        val returnTarget = backStack.lastOrNull()

        if (isDialogPreservingKey(key)) {
            saveCurrentOverlayIfNeeded(returnTarget)
        } else {
            if (restoreBottomSheets && returnTarget != null) {
                saveCurrentOverlayIfNeeded(returnTarget)
            }
            popBackStackInternal(
                findIndex = { backStack.indexOfFirst { it::class == popUpToClass } },
                inclusive = inclusive,
                restoreOverlayForNewTop = false,
            )
        }

        if (isTopLevelKey(key)) {
            navigateToTab(key)
        } else {
            if (backStack.lastOrNull() != key) {
                backStack.add(key)
            }
        }
    }

    private fun saveCurrentOverlayIfNeeded(targetKey: NavKey?) {
        if (targetKey == null) return
        val hasSheets = ownedBottomSheets.isNotEmpty()
        val hasDialogs = dialogStack.size > 1
        if (!hasSheets && !hasDialogs) return

        val alreadySaved = savedOverlayStacks.any { it.targetKey == targetKey }
        if (alreadySaved) {
            clearBottomSheets()
            clearDialogs()
            return
        }

        savedOverlayStacks.add(
            SavedOverlayState(
                targetKey = targetKey,
                bottomSheets = ownedBottomSheets.toList(),
                dialogs = dialogStack.filter { it !is PlaceholderDialogKey },
            ),
        )
        clearBottomSheets()
        clearDialogs()
    }

    fun goBack(): Boolean {
        if (dialogStack.size > 1) {
            dismissDialog()
            return true
        }

        if (bottomSheetStack.size > 1) {
            dismissBottomSheet()
            return true
        }

        return if (backStack.size > 1) {
            val removedKey = backStack.removeAt(backStack.lastIndex)
            ownedBottomSheets.removeAll { it.ownerKey == removedKey }
            restoreOverlayForKey(backStack.lastOrNull())
            syncBottomSheetStack()
            true
        } else {
            false
        }
    }

    suspend fun goBackWithResult(resultKey: String, result: Any?): Boolean {
        emitResult(resultKey, result)
        return goBack()
    }

    suspend fun popBackToWithResult(
        key: NavKey,
        resultKey: String,
        result: Any?,
        inclusive: Boolean = false,
        restoreOverlays: Boolean = true,
    ) {
        emitResult(resultKey, result)
        popBackTo(key, inclusive, restoreOverlays)
    }

    fun popBackTo(
        key: NavKey,
        inclusive: Boolean = false,
        restoreOverlays: Boolean = true,
    ) {
        if (!restoreOverlays) {
            savedOverlayStacks.removeAll { it.targetKey == key }
        }
        popBackStackInternal(
            findIndex = { backStack.indexOfLast { it == key } },
            inclusive = inclusive,
            restoreOverlayForNewTop = restoreOverlays,
        )
    }

    fun popBackTo(
        keyClass: KClass<out NavKey>,
        inclusive: Boolean = false,
        restoreOverlays: Boolean = true,
    ) {
        if (!restoreOverlays) {
            savedOverlayStacks.removeAll { it.targetKey::class == keyClass }
        }
        popBackStackInternal(
            findIndex = { backStack.indexOfFirst { it::class == keyClass } },
            inclusive = inclusive,
            restoreOverlayForNewTop = restoreOverlays,
        )
    }

    private fun popBackStackInternal(
        findIndex: () -> Int,
        inclusive: Boolean,
        restoreOverlayForNewTop: Boolean,
    ) {
        val index = findIndex()
        if (index == -1) return

        val removeFrom = if (inclusive) index else index + 1
        if (removeFrom >= backStack.size) return

        val removedKeys = backStack.subList(removeFrom, backStack.size).toSet()
        backStack.subList(removeFrom, backStack.size).clear()

        ownedBottomSheets.removeAll { it.ownerKey in removedKeys }
        savedOverlayStacks.removeAll { it.targetKey in removedKeys }
        syncBottomSheetStack()

        if (restoreOverlayForNewTop) {
            restoreOverlayForKey(backStack.lastOrNull())
        }
    }

    private fun restoreOverlayForKey(targetKey: NavKey?) {
        if (targetKey == null) return
        val savedIndex = savedOverlayStacks.indexOfLast { it.targetKey == targetKey }
        if (savedIndex == -1) return

        val savedState = savedOverlayStacks.removeAt(savedIndex)
        ownedBottomSheets.addAll(savedState.bottomSheets)
        dialogStack.addAll(savedState.dialogs)
        syncBottomSheetStack()
    }

    fun navigateAndClearBackStack(key: NavKey) {
        clearAllOverlays()
        backStack.clear()
        backStack.add(key)
    }

    fun clearBackStack() {
        clearAllOverlays()
        val first = backStack.firstOrNull()
        backStack.clear()
        if (first != null) backStack.add(first)
    }

    private fun navigateToTab(tabKey: NavKey) {
        clearAllOverlays()
        val existingTabIndex = backStack.indexOfLast { it::class == tabKey::class }

        if (existingTabIndex != -1) {
            if (existingTabIndex < backStack.lastIndex) {
                backStack.subList(existingTabIndex + 1, backStack.size).clear()
            } else if (backStack[existingTabIndex] != tabKey) {
                backStack[existingTabIndex] = tabKey
            }
        } else {
            val lastTabIndex = backStack.indexOfLast { isTopLevelKey(it) }
            if (lastTabIndex != -1 && lastTabIndex < backStack.lastIndex) {
                backStack.subList(lastTabIndex + 1, backStack.size).clear()
            }
            backStack.add(tabKey)
        }
    }

    private fun clearAllOverlays() {
        clearBottomSheets()
        clearDialogs()
        savedOverlayStacks.clear()
    }

    private fun syncBottomSheetStack() {
        bottomSheetStack.clear()
        bottomSheetStack.add(PlaceholderBottomSheetKey)
        bottomSheetStack.addAll(ownedBottomSheets.map { it.key })
    }

    // ----------------------------------------------------------------
    // BOTTOM SHEET NAVIGATION
    // ----------------------------------------------------------------

    fun showBottomSheet(key: BottomSheetKey) {
        if (ownedBottomSheets.lastOrNull()?.key != key) {
            ownedBottomSheets.add(OwnedBottomSheet(key = key, ownerKey = currentKey))
            bottomSheetStack.add(key)
        }
    }

    fun dismissBottomSheet(): Boolean {
        clearDialogs()
        return if (ownedBottomSheets.isNotEmpty()) {
            ownedBottomSheets.removeAt(ownedBottomSheets.lastIndex)
            bottomSheetStack.removeAt(bottomSheetStack.lastIndex)
            true
        } else {
            false
        }
    }

    suspend fun dismissBottomSheetWithResult(resultKey: String, result: Any?): Boolean {
        emitResult(resultKey, result)
        return dismissBottomSheet()
    }

    fun clearBottomSheets() {
        ownedBottomSheets.clear()
        bottomSheetStack.clear()
        bottomSheetStack.add(PlaceholderBottomSheetKey)
    }

    // ----------------------------------------------------------------
    // DIALOG NAVIGATION
    // ----------------------------------------------------------------

    fun showDialog(key: DialogKey) {
        if (dialogStack.lastOrNull() != key) {
            dialogStack.add(key)
        }
    }

    fun dismissDialog(): Boolean {
        return if (dialogStack.size > 1) {
            dialogStack.removeAt(dialogStack.lastIndex)
            true
        } else {
            false
        }
    }

    suspend fun dismissDialogWithResult(resultKey: String, result: Any?): Boolean {
        emitResult(resultKey, result)
        return dismissDialog()
    }

    fun clearDialogs() {
        dialogStack.clear()
        dialogStack.add(PlaceholderDialogKey)
    }

    // ----------------------------------------------------------------
    // UTILITY METHODS
    // ----------------------------------------------------------------

    fun canGoBack(): Boolean =
        backStack.size > 1 || bottomSheetStack.size > 1 || dialogStack.size > 1

    fun isCurrentKey(key: NavKey): Boolean = currentKey == key

    fun isCurrentKeyClass(keyClass: KClass<out NavKey>): Boolean =
        currentKey?.let { it::class == keyClass } ?: false

    fun <T : NavKey> findInBackStack(keyClass: KClass<T>): T? {
        @Suppress("UNCHECKED_CAST")
        return backStack.lastOrNull { it::class == keyClass } as? T
    }

    companion object {
        const val KEY_SELECTED_THEME = "selected_theme"
        const val KEY_SELECTED_LANGUAGE = "selected_language"
        const val KEY_REMINDER_TIME = "reminder_time"
        const val KEY_PHOTO_ACTION = "photo_action"
        const val KEY_EDIT_FIELD_VALUE = "edit_field_value"
        const val KEY_LOGOUT_CONFIRMED = "logout_confirmed"
        const val KEY_DELETE_CONFIRMED = "delete_confirmed"
        const val KEY_DAILY_QUOTE_HIDE_FOREVER = "daily_quote_hide_forever"
        const val KEY_DAILY_QUOTE_HIDE_TODAY = "daily_quote_hide_today"
        const val KEY_COLLECTION_CREATED = "collection_created"
        const val KEY_TAG_SHEET_RESULT = "addquote_tag_sheet_result"
        const val KEY_OFFLINE_RETRY = "offline_retry"
        const val KEY_OFFLINE_DISMISS = "offline_dismiss"
        const val KEY_WIDGET_FREQUENCY = "widget_frequency"
        const val KEY_PLAYLIST_NAME = "playlist_name"
        const val KEY_PLAYLIST_SAVED = "playlist_saved"
        const val KEY_PLAYLIST_DELETE = "playlist_delete"
        const val KEY_WIDGET_QUOTE_DELETE = "widget_quote_delete"
        const val KEY_QUOTE_EDIT_RESULT = "quote_edit_result"
    }
}
