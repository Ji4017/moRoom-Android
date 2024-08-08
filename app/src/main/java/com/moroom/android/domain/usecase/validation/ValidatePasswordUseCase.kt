package com.moroom.android.domain.usecase.validation

import javax.inject.Inject

class ValidatePasswordUseCase @Inject constructor() {
    operator fun invoke(password: String): Boolean = password.length >= 6
}