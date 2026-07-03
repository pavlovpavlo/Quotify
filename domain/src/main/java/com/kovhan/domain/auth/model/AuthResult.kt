package com.kovhan.domain.auth.model

import com.kovhan.core.models.Outcome

typealias AuthResult<T> = Outcome<T, AuthError>
