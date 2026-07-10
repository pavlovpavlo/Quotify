package com.kovhan.domain.library.use_case.sync

import com.kovhan.domain.library.sync.LibrarySynchronizer
import javax.inject.Inject

class SyncPendingChangesUseCase @Inject constructor(
    private val synchronizer: LibrarySynchronizer,
) {
    suspend operator fun invoke(): Boolean = synchronizer.syncPendingChanges()
}
