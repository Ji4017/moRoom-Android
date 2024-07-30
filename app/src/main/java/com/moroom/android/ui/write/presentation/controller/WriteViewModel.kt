package com.moroom.android.ui.write.presentation.controller

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moroom.android.ui.write.data.model.CheckItem
import com.moroom.android.ui.write.data.model.Coordinates
import com.moroom.android.ui.write.domain.model.WrittenReview
import com.moroom.android.ui.write.domain.usecase.CheckLatLngExistsUseCase
import com.moroom.android.ui.write.domain.usecase.SaveReviewUseCase
import com.moroom.android.ui.write.domain.usecase.FetchCheckItemsUseCase
import com.moroom.android.ui.write.domain.usecase.GetCheckItemsUseCase
import com.moroom.android.ui.write.domain.usecase.GetLatLngUseCase
import com.moroom.android.ui.write.domain.usecase.SaveLatLngUseCase
import com.moroom.android.ui.write.domain.usecase.UpdateCheckBoxUseCase
import com.moroom.android.ui.write.domain.usecase.UpdateUserReviewStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WriteViewModel @Inject constructor(
    private val fetchCheckItemsUseCase: FetchCheckItemsUseCase,
    private val getCheckItemsUseCase: GetCheckItemsUseCase,
    private val updateCheckBoxUseCase: UpdateCheckBoxUseCase,
    private val saveReviewUseCase: SaveReviewUseCase,
    private val checkLatLngExistsUseCase: CheckLatLngExistsUseCase,
    private val getLatLngUseCase: GetLatLngUseCase,
    private val saveLatLngUseCase: SaveLatLngUseCase,
    private val updateUserReviewStatusUseCase: UpdateUserReviewStatusUseCase

) : ViewModel() {
    val checkItems: StateFlow<List<CheckItem>> = getCheckItemsUseCase().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        emptyList()
    )

    private var _writeCompleteState = MutableStateFlow<Result<Unit>?>(null)
    val writeCompleteState = _writeCompleteState.asStateFlow()

    fun fetchCheckItems() {
        viewModelScope.launch { fetchCheckItemsUseCase.invoke() }
    }

    fun onCheckBoxClicked(text: String, isChecked: Boolean) {
        viewModelScope.launch { updateCheckBoxUseCase.invoke(text, isChecked) }
    }

    fun onWriteComplete(writtenReview: WrittenReview) {
        viewModelScope.launch {
            val result = try {
                saveLatLngIfNeeded(writtenReview.address)
                saveReviewUseCase.invoke(writtenReview)
                updateUserReviewStatusUseCase.invoke()
                Result.success(Unit)
            } catch (e: Exception) {
                Log.e("onWriteComplete", e.toString())
                Result.failure(e)
            }
            _writeCompleteState.value = result
        }
    }

    private suspend fun saveLatLngIfNeeded(address: String) {
        val isLatLngStored = checkLatLngExistsUseCase.invoke(address)
        if (!isLatLngStored) {
            val (latitude, longitude) = getLatLngUseCase.invoke(address)
            val coordinates = Coordinates(address, latitude, longitude)
            saveLatLngUseCase.invoke(coordinates)
        }
    }
}