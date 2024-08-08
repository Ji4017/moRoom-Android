package com.moroom.android.domain.usecase.validation

import android.util.Patterns
import javax.inject.Inject

class ValidateIdUseCase @Inject constructor(){
    operator fun invoke(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()
}