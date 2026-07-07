package com.kovhan.data.library.util

import com.kovhan.domain.common.IdGenerator
import java.util.UUID
import javax.inject.Inject

class UuidIdGenerator @Inject constructor() : IdGenerator {
    override fun generate(): String = UUID.randomUUID().toString()
}
