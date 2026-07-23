package com.kovhan.domain.connectivity.use_case

import com.kovhan.domain.connectivity.ConnectivityRepository
import javax.inject.Inject

class CheckConnectivityUseCase @Inject constructor(
    private val repository: ConnectivityRepository,
) {
    suspend operator fun invoke(): Boolean = repository.isOnline()
}
