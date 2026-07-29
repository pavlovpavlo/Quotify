package com.kovhan.feature.widget.presentation.picker

import com.kovhan.core.models.LibrarySearchResults
import com.kovhan.core.models.quote.EnrichedQuote
import com.kovhan.core.models.collections.SavedAuthor
import com.kovhan.core.models.collections.SavedBook
import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.core.models.collections.SavedTag
import com.kovhan.core.models.widget.Playlist
import com.kovhan.core.models.widget.PlaylistSource
import com.kovhan.core.navigation.PlaylistNameMode
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.library.use_case.author.ObserveSavedAuthorsUseCase
import com.kovhan.domain.library.use_case.book.ObserveSavedBooksUseCase
import com.kovhan.domain.library.use_case.collection.ObserveCollectionsUseCase
import com.kovhan.domain.library.use_case.quote.ObserveEnrichedQuotesUseCase
import com.kovhan.domain.library.use_case.search.SearchLibraryUseCase
import com.kovhan.domain.library.use_case.tag.ObserveSavedTagsUseCase
import com.kovhan.domain.widget.use_case.playlist.CreatePlaylistUseCase
import com.kovhan.domain.widget.use_case.playlist.DeletePlaylistUseCase
import com.kovhan.domain.widget.use_case.playlist.GetPlaylistByIdUseCase
import com.kovhan.domain.widget.use_case.playlist.RenamePlaylistUseCase
import com.kovhan.domain.widget.use_case.playlist.UpdatePlaylistUseCase
import com.kovhan.feature.widget.presentation.picker.mvi.PickerMode
import com.kovhan.feature.widget.presentation.picker.mvi.PickerScope
import com.kovhan.feature.widget.presentation.picker.mvi.PlaylistPickerEffect
import com.kovhan.feature.widget.presentation.picker.mvi.PlaylistPickerIntent
import com.kovhan.feature.widget.presentation.picker.mvi.PlaylistPickerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistPickerViewModel @Inject constructor(
    observeEnrichedQuotes: ObserveEnrichedQuotesUseCase,
    observeCollections: ObserveCollectionsUseCase,
    observeSavedBooks: ObserveSavedBooksUseCase,
    observeSavedAuthors: ObserveSavedAuthorsUseCase,
    observeSavedTags: ObserveSavedTagsUseCase,
    private val searchLibrary: SearchLibraryUseCase,
    private val getPlaylistById: GetPlaylistByIdUseCase,
    private val createPlaylist: CreatePlaylistUseCase,
    private val updatePlaylist: UpdatePlaylistUseCase,
    private val renamePlaylist: RenamePlaylistUseCase,
    private val deletePlaylist: DeletePlaylistUseCase,
) : BaseViewModel<PlaylistPickerState, PlaylistPickerEffect>(PlaylistPickerState()),
    PlaylistPickerIntent {

    private var initialized = false

    private val library = combine(
        observeEnrichedQuotes(),
        observeCollections(),
        observeSavedBooks(withCount = true),
        observeSavedAuthors(withCount = true),
        observeSavedTags(withCount = true),
    ) { quotes, folders, books, authors, tags ->
        Library(quotes, folders, books, authors, tags)
    }

    private val query = uiState.map { it.query }.distinctUntilChanged()

    init {
        combine(library, query) { snapshot, query ->
            searchLibrary(
                query = query,
                quotes = snapshot.quotes,
                folders = snapshot.folders,
                books = snapshot.books,
                authors = snapshot.authors,
                tags = snapshot.tags,
            )
        }
            .onEach { results -> publishState { copy(results = results, isLoading = false) } }
            .launchIn(viewModelScope)
    }

    /** Bind the nav-key arg once; loads the playlist to prefill when editing. */
    fun initialize(playlistId: String?) {
        if (initialized) return
        initialized = true
        if (playlistId == null) {
            publishState { copy(mode = PickerMode.CREATE) }
            return
        }
        viewModelScope.launch {
            val playlist = getPlaylistById(playlistId)
            publishState {
                copy(
                    mode = PickerMode.EDIT,
                    playlistId = playlistId,
                    name = playlist?.name.orEmpty(),
                    picked = playlist?.sources?.toSet() ?: emptySet(),
                )
            }
        }
    }

    override fun onQueryChange(query: String) = publishState { copy(query = query) }

    override fun onScopeChange(scope: PickerScope) = publishState { copy(scope = scope) }

    override fun onTogglePick(source: PlaylistSource) = publishState {
        copy(picked = if (source in picked) picked - source else picked + source)
    }

    override fun onSaveClicked() {
        val state = uiState.value
        if (!state.canSave) return
        if (state.mode == PickerMode.CREATE) {
            publishEffect(PlaylistPickerEffect.OpenNameSheet(PlaylistNameMode.CREATE, ""))
            return
        }
        val id = state.playlistId ?: return
        viewModelScope.launch {
            updatePlaylist(Playlist(id = id, name = state.name, sources = state.picked.toList()))
            publishEffect(PlaylistPickerEffect.Saved)
        }
    }

    /** Result from the name sheet — creates (create mode) or renames (edit mode). */
    fun onNameConfirmed(name: String) {
        val state = uiState.value
        viewModelScope.launch {
            if (state.mode == PickerMode.CREATE) {
                createPlaylist(name, state.picked.toList())
                publishEffect(PlaylistPickerEffect.Saved)
            } else {
                val id = state.playlistId ?: return@launch
                renamePlaylist(id, name)
                publishState { copy(name = name.trim()) }
            }
        }
    }

    override fun onToggleMenu() = publishState { copy(menuVisible = !menuVisible) }

    override fun onDismissMenu() = publishState { copy(menuVisible = false) }

    override fun onRenameClicked() {
        publishState { copy(menuVisible = false) }
        publishEffect(PlaylistPickerEffect.OpenNameSheet(PlaylistNameMode.RENAME, uiState.value.name))
    }

    override fun onDeleteClicked() {
        publishState { copy(menuVisible = false) }
        publishEffect(PlaylistPickerEffect.OpenDeleteDialog(uiState.value.name))
    }

    fun onDeleteConfirmed() {
        val id = uiState.value.playlistId ?: return
        viewModelScope.launch {
            deletePlaylist(id)
            publishEffect(PlaylistPickerEffect.Saved)
        }
    }

    private data class Library(
        val quotes: List<EnrichedQuote>,
        val folders: List<SavedCollection>,
        val books: List<SavedBook>,
        val authors: List<SavedAuthor>,
        val tags: List<SavedTag>,
    )
}
