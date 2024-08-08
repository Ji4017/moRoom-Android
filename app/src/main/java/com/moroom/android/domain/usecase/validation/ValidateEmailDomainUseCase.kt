package com.moroom.android.domain.usecase.validation

import javax.inject.Inject

class ValidateEmailDomainUseCase @Inject constructor() {
    operator fun invoke(email: String): Boolean {
        val domain = email.substringAfter("@")
        return domain.equals("cju.ac.kr", ignoreCase = true)
    }
}