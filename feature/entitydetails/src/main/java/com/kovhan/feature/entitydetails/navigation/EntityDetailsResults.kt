package com.kovhan.feature.entitydetails.navigation

/**
 * Navigation result contracts for the entity-details flow — keys and payload
 * types passed back from the sheets/dialogs it launches. Kept in the neutral
 * module-level `navigation` package so producing sheets (move / style / rename /
 * quote edit) and the consuming details screen share them without either
 * feature package depending on the other.
 */

internal const val KEY_ENTITY_RENAME_RESULT = "entity_details_rename_result"
internal const val KEY_ENTITY_STYLE_RESULT = "entity_details_style_result"
internal const val KEY_ENTITY_QUOTE_EDIT_RESULT = "entity_details_quote_edit_result"
internal const val KEY_ENTITY_MOVE_QUOTE_RESULT = "entity_details_move_quote_result"
internal const val KEY_ENTITY_DELETE_CONFIRMED = "entity_details_delete_confirmed"
internal const val KEY_ENTITY_DELETE_QUOTE_RESULT = "entity_details_delete_quote_result"

internal data class EntityCollectionStyleResult(
    val iconId: String,
    val tone: String,
)

internal data class EntityMoveQuoteResult(
    val quoteId: String,
    val targetCollectionId: String,
)
