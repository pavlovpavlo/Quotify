package com.kovhan.domain.common

/**
 * Produces unique ids for any entity created on-device (quotes, authors,
 * books, collections, …). Inject instead of calling a platform id source
 * directly so it can be faked in tests.
 */
interface IdGenerator {
    fun generate(): String
}
