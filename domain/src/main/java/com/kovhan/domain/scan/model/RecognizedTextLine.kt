package com.kovhan.domain.scan.model

/**
 * A single line of text produced by the OCR engine from a scanned image.
 *
 * Lines are the unit the user selects on the review view: one recognized line
 * maps to one selectable row.
 */
data class RecognizedTextLine(val text: String)
