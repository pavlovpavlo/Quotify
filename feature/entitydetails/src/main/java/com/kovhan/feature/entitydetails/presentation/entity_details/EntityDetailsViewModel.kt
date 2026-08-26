package com.kovhan.feature.entitydetails.presentation.entity_details

import com.kovhan.core.analytics.AnalyticsTracker
import com.kovhan.core.analytics.CollectionAction
import com.kovhan.core.analytics.QuoteAction
import com.kovhan.core.analytics.event.CollectionSettings
import com.kovhan.core.analytics.event.QuoteSettings
import com.kovhan.core.analytics.event.VisitCollection
import com.kovhan.core.models.quote.EnrichedQuote
import com.kovhan.core.models.LibraryEntityType
import com.kovhan.core.models.quote.QuoteFilter
import com.kovhan.core.models.collections.SavedAuthor
import com.kovhan.core.models.collections.SavedBook
import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.core.models.collections.SavedTag
import com.kovhan.core.navigation.EntityType
import com.kovhan.core.navigation.QuoteRemovalMode
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.library.use_case.author.DeleteSavedAuthorUseCase
import com.kovhan.domain.library.use_case.author.EditSavedAuthorUseCase
import com.kovhan.domain.library.use_case.author.ObserveSavedAuthorsUseCase
import com.kovhan.domain.library.use_case.book.DeleteSavedBookUseCase
import com.kovhan.domain.library.use_case.book.EditSavedBookUseCase
import com.kovhan.domain.library.use_case.book.ObserveSavedBooksUseCase
import com.kovhan.domain.library.use_case.collection.DeleteCollectionUseCase
import com.kovhan.domain.library.use_case.collection.EditCollectionUseCase
import com.kovhan.domain.library.use_case.collection.ObserveCollectionsUseCase
import com.kovhan.domain.library.use_case.quote.GetQuoteByIdUseCase
import com.kovhan.domain.library.use_case.quote.MoveQuoteToCollectionUseCase
import com.kovhan.domain.library.use_case.quote.ObserveEnrichedQuotesUseCase
import com.kovhan.domain.library.use_case.quote.RemoveQuoteFromEntityUseCase
import com.kovhan.domain.library.use_case.quote.UpsertQuoteUseCase
import com.kovhan.domain.library.use_case.tag.DeleteSavedTagUseCase
import com.kovhan.domain.library.use_case.tag.EditSavedTagUseCase
import com.kovhan.domain.library.use_case.tag.ObserveSavedTagsUseCase
import com.kovhan.feature.entitydetails.presentation.entity_details.model.EntityQuoteDraft
import com.kovhan.feature.entitydetails.presentation.entity_details.mvi.EntityDetailsEffect
import com.kovhan.feature.entitydetails.presentation.entity_details.mvi.EntityDetailsMenu
import com.kovhan.feature.entitydetails.presentation.entity_details.mvi.EntityDetailsScreenIntent
import com.kovhan.feature.entitydetails.presentation.entity_details.mvi.EntityDetailsState
import com.kovhan.feature.entitydetails.presentation.entity_details.mvi.EntityDetailsSummary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntityDetailsViewModel @Inject constructor(
    private val observeCollections: ObserveCollectionsUseCase,
    private val observeEnrichedQuotes: ObserveEnrichedQuotesUseCase,
    observeSavedAuthors: ObserveSavedAuthorsUseCase,
    observeSavedBooks: ObserveSavedBooksUseCase,
    observeSavedTags: ObserveSavedTagsUseCase,
    private val editCollection: EditCollectionUseCase,
    private val deleteCollection: DeleteCollectionUseCase,
    private val editTag: EditSavedTagUseCase,
    private val editBook: EditSavedBookUseCase,
    private val editAuthor: EditSavedAuthorUseCase,
    private val deleteTag: DeleteSavedTagUseCase,
    private val deleteBook: DeleteSavedBookUseCase,
    private val deleteAuthor: DeleteSavedAuthorUseCase,
    private val upsertQuote: UpsertQuoteUseCase,
    private val getQuoteById: GetQuoteByIdUseCase,
    private val moveQuoteToCollection: MoveQuoteToCollectionUseCase,
    private val removeQuoteFromEntity: RemoveQuoteFromEntityUseCase,
    private val analytics: AnalyticsTracker,
) : BaseViewModel<EntityDetailsState, EntityDetailsEffect>(EntityDetailsState()),
    EntityDetailsScreenIntent {

    private var bindJob: Job? = null
    private var currentKey: String? = null
    private var currentType: EntityType = EntityType.COLLECTION
    private var currentEntityId: String = ""
    private var generalName: String = ""
    private var currentCollection: SavedCollection? = null
    private var moveTargets: List<SavedCollection> = emptyList()
    private var authorOptions: List<String> = emptyList()
    private var bookOptions: List<String> = emptyList()
    private var tagPool: List<String> = emptyList()

    init {
        observeSavedAuthors()
            .onEach { authors -> authorOptions = authors.map { it.name } }
            .launchIn(viewModelScope)

        observeSavedBooks()
            .onEach { books -> bookOptions = books.map { it.name } }
            .launchIn(viewModelScope)

        observeSavedTags()
            .onEach { tags -> tagPool = tags.map { it.name } }
            .launchIn(viewModelScope)
    }

    fun bind(type: EntityType, entityId: String, title: String, generalName: String) {
        val key = "$type:$entityId"
        currentType = type
        currentEntityId = entityId
        this.generalName = generalName

        if (currentKey == key && bindJob != null) {
            if (type != EntityType.COLLECTION && title.isNotBlank()) {
                publishState { copy(title = title) }
            }
            return
        }

        currentKey = key
        bindJob?.cancel()
        currentCollection = null

        publishState {
            copy(
                isLoading = true,
                type = type,
                title = title,
                quotes = emptyList(),
                summary = EntityDetailsSummary(),
                menu = menuFor(type, null),
            )
        }

        when (type) {
            EntityType.COLLECTION -> bindCollection(entityId)
            EntityType.TAG,
            EntityType.BOOK,
            EntityType.AUTHOR,
            -> bindDerivedEntity(type, entityId, title)
        }
    }

    override fun onRenameRequested() {
        publishEffect(
            EntityDetailsEffect.OpenRenameSheet(
                type = currentType,
                initialName = uiState.value.title,
            ),
        )
    }

    override fun onRenameConfirmed(name: String) {
        val trimmed = name.trim()
        if (trimmed.isBlank()) return

        analytics.track(
            CollectionSettings(
                action = CollectionAction.RENAME,
                changedName = trimmed != uiState.value.title,
            ),
        )
        publishState { copy(title = trimmed) }
        viewModelScope.launch {
            when (currentType) {
                EntityType.COLLECTION -> {
                    val collection = currentCollection ?: return@launch
                    editCollection(collection.copy(name = trimmed))
                }
                EntityType.TAG -> editTag(SavedTag(id = currentEntityId, name = trimmed))
                EntityType.BOOK -> editBook(SavedBook(id = currentEntityId, name = trimmed))
                EntityType.AUTHOR -> editAuthor(SavedAuthor(id = currentEntityId, name = trimmed))
            }
        }
    }

    override fun onDeleteRequested() {
        publishEffect(EntityDetailsEffect.OpenDeleteEntityDialog(currentType))
    }

    override fun onDeleteConfirmed() {
        analytics.track(CollectionSettings(CollectionAction.DELETE))
        viewModelScope.launch {
            when (currentType) {
                EntityType.COLLECTION -> {
                    val collection = currentCollection ?: return@launch
                    deleteCollection(collection.id, generalName)
                }
                EntityType.TAG -> deleteTag(currentEntityId)
                EntityType.BOOK -> deleteBook(currentEntityId)
                EntityType.AUTHOR -> deleteAuthor(currentEntityId)
            }
            publishEffect(EntityDetailsEffect.Close)
        }
    }

    override fun onEditStyleRequested() {
        val collection = currentCollection ?: return
        publishEffect(
            EntityDetailsEffect.OpenCollectionStyleSheet(
                iconId = collection.iconId,
                tone = collection.iconColor,
            ),
        )
    }

    override fun onCollectionStyleConfirmed(iconId: String, tone: String) {
        val collection = currentCollection ?: return
        analytics.track(
            CollectionSettings(
                action = CollectionAction.EDIT,
                changedIcon = iconId != collection.iconId,
                changedColor = tone != collection.iconColor,
                changeFrom = collection.iconId,
                changeTo = iconId,
            ),
        )
        viewModelScope.launch {
            editCollection(
                collection.copy(
                    iconId = iconId,
                    iconColor = tone,
                ),
            )
        }
    }

    override fun onEditQuoteRequested(quote: EnrichedQuote) {
        analytics.track(QuoteSettings(QuoteAction.EDIT))
        publishEffect(
            EntityDetailsEffect.OpenQuoteEditor(
                draft = EntityQuoteDraft(
                    quoteId = quote.id,
                    text = quote.text,
                    authorName = quote.author?.name.orEmpty(),
                    bookName = quote.book?.name.orEmpty(),
                    tags = quote.tags.map { it.name },
                    inWidgetPlaylist = quote.inWidgetPlaylist,
                    inPushPlaylist = quote.inPushPlaylist,
                    page = quote.page?.toString().orEmpty(),
                ),
                authorOptions = authorOptions,
                bookOptions = bookOptions,
                tagPool = tagPool,
            ),
        )
    }

    override fun onEditQuoteSaved(draft: EntityQuoteDraft) {
        if (draft.text.isBlank()) return

        viewModelScope.launch {
            val existing = getQuoteById(draft.quoteId)
            // «Обране» — це прапорець isFavourite, а не папка. Прив'язати цитату
            // до нього означало б вирвати її з реальної колекції, тож на цьому
            // екрані лишаємо ту, що вже була.
            val collectionId = when (currentType) {
                EntityType.COLLECTION ->
                    if (isFavouritesScreen) existing?.collectionId else currentEntityId

                EntityType.TAG,
                EntityType.BOOK,
                EntityType.AUTHOR,
                -> existing?.collectionId
            }
            upsertQuote(
                id = draft.quoteId,
                text = draft.text,
                authorName = draft.authorName.ifBlank { null },
                bookName = draft.bookName.ifBlank { null },
                tagNames = draft.tags,
                collectionId = collectionId,
                inWidgetPlaylist = draft.inWidgetPlaylist,
                inPushPlaylist = draft.inPushPlaylist,
                generalName = generalName,
                sourceDailyId = existing?.sourceDailyId,
                page = draft.page.toIntOrNull(),
                isFavourite = existing?.isFavourite == true,
            )
        }
    }

    override fun onMoveQuoteRequested(quoteId: String) {
        analytics.track(QuoteSettings(QuoteAction.MOVE))
        viewModelScope.launch {
            publishEffect(
                EntityDetailsEffect.OpenMoveQuoteSheet(
                    quoteId = quoteId,
                    selectedCollectionId = moveTargets.firstOrNull()?.id,
                    excludedCollectionId = currentEntityId.takeIf {
                        currentType == EntityType.COLLECTION
                    },
                    keepsFavourite = getQuoteById(quoteId)?.isFavourite == true,
                ),
            )
        }
    }

    override fun onMoveQuoteConfirmed(quoteId: String, targetCollectionId: String) {
        viewModelScope.launch {
            moveQuoteToCollection(
                quoteId = quoteId,
                targetCollectionId = targetCollectionId,
                generalName = generalName,
            )
        }
    }

    private val isFavouritesScreen: Boolean
        get() = currentType == EntityType.COLLECTION &&
            currentEntityId == SavedCollection.FAVOURITES_ID

    override fun onRemoveQuoteRequested(quoteId: String) {
        viewModelScope.launch {
            publishEffect(
                EntityDetailsEffect.OpenDeleteQuoteDialog(
                    quoteId = quoteId,
                    mode = removalModeFor(quoteId),
                ),
            )
        }
    }

    override fun onRemoveQuoteConfirmed(quoteId: String) {
        analytics.track(QuoteSettings(QuoteAction.DELETE))
        viewModelScope.launch {
            removeQuoteFromEntity(
                quoteId = quoteId,
                entityType = currentType.toLibraryEntityType(),
                entityId = currentEntityId,
                generalName = generalName,
            )
        }
    }

    private fun bindCollection(collectionId: String) {
        var hadResolvedCollection = false
        bindJob = combine(
            observeCollections(),
            observeEnrichedQuotes(QuoteFilter(collectionId = collectionId)),
        ) { collections, quotes ->
            Triple(
                collections.firstOrNull { it.id == collectionId },
                quotes,
                buildMoveTargets(collections),
            )
        }.onEach { (collection, quotes, targets) ->
            moveTargets = targets
            currentCollection = collection

            if (collection == null) {
                if (hadResolvedCollection || !uiState.value.isLoading) {
                    publishEffect(EntityDetailsEffect.Close)
                } else {
                    publishState {
                        copy(
                            isLoading = false,
                            type = EntityType.COLLECTION,
                            title = "",
                            quotes = emptyList(),
                            summary = EntityDetailsSummary(),
                            menu = EntityDetailsMenu(),
                        )
                    }
                }
            } else {
                if (!hadResolvedCollection) {
                    analytics.track(
                        VisitCollection(
                            name = collection.name,
                            quoteCount = collection.quoteCount ?: quotes.size,
                        ),
                    )
                }
                hadResolvedCollection = true
                publishState {
                    copy(
                        isLoading = false,
                        type = EntityType.COLLECTION,
                        title = collection.name,
                        quotes = quotes,
                        summary = EntityDetailsSummary(
                            iconId = collection.iconId,
                            tone = collection.iconColor,
                            quoteCount = collection.quoteCount ?: quotes.size,
                        ),
                        menu = menuFor(EntityType.COLLECTION, collection),
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun bindDerivedEntity(type: EntityType, entityId: String, title: String) {
        val filter = when (type) {
            EntityType.TAG -> QuoteFilter(tagId = entityId)
            EntityType.BOOK -> QuoteFilter(bookId = entityId)
            EntityType.AUTHOR -> QuoteFilter(authorId = entityId)
            EntityType.COLLECTION -> QuoteFilter(collectionId = entityId)
        }

        bindJob = combine(
            observeEnrichedQuotes(filter),
            observeCollections(),
        ) { quotes, collections ->
            quotes to buildMoveTargets(collections)
        }
            .onEach { (quotes, targets) ->
                moveTargets = targets
                publishState {
                    copy(
                        isLoading = false,
                        type = type,
                        title = if (this.title.isBlank()) title else this.title,
                        quotes = quotes,
                        summary = EntityDetailsSummary(quoteCount = quotes.size),
                        menu = menuFor(type, null),
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun buildMoveTargets(collections: List<SavedCollection>): List<SavedCollection> {
        val general = collections.firstOrNull { it.id == SavedCollection.GENERAL_ID }
            ?: SavedCollection(
                id = SavedCollection.GENERAL_ID,
                name = generalName,
            )

        return (listOf(general) + collections.filterNot { it.id == SavedCollection.GENERAL_ID })
            .distinctBy { it.id }
            .filterNot { it.id == SavedCollection.FAVOURITES_ID }
            .filterNot { currentType == EntityType.COLLECTION && it.id == currentEntityId }
    }

    private fun menuFor(type: EntityType, collection: SavedCollection?): EntityDetailsMenu =
        when (type) {
            EntityType.COLLECTION -> {
                val canEdit = collection != null &&
                    collection.id != SavedCollection.GENERAL_ID &&
                    collection.id != SavedCollection.FAVOURITES_ID
                EntityDetailsMenu(
                    canRename = canEdit,
                    canEditStyle = canEdit,
                    canDelete = canEdit,
                )
            }
            EntityType.TAG,
            EntityType.BOOK,
            EntityType.AUTHOR,
            -> EntityDetailsMenu(
                canRename = true,
                canDelete = true,
            )
        }

    /**
     * Текст підтвердження має збігатися з тим, що станеться насправді, а це
     * залежить від самої цитати: та, що живе ще й в «Обраному», лише втратить
     * прив'язку, решта — зникне назовсім.
     */
    private suspend fun removalModeFor(quoteId: String): QuoteRemovalMode {
        if (currentType != EntityType.COLLECTION) return QuoteRemovalMode.REMOVE_FROM_ENTITY

        val quote = getQuoteById(quoteId)
        return if (currentEntityId == SavedCollection.FAVOURITES_ID) {
            if (quote?.collectionId != null) {
                QuoteRemovalMode.REMOVE_FROM_FAVOURITES
            } else {
                QuoteRemovalMode.DELETE
            }
        } else {
            if (quote?.isFavourite == true) {
                QuoteRemovalMode.REMOVE_FROM_COLLECTION
            } else {
                QuoteRemovalMode.DELETE
            }
        }
    }
}

private fun EntityType.toLibraryEntityType(): LibraryEntityType =
    when (this) {
        EntityType.COLLECTION -> LibraryEntityType.COLLECTION
        EntityType.TAG -> LibraryEntityType.TAG
        EntityType.BOOK -> LibraryEntityType.BOOK
        EntityType.AUTHOR -> LibraryEntityType.AUTHOR
    }
