package com.openpay.android.ui.images

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.openpay.android.data.repository.ImagesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class ImagesViewModel @Inject constructor(
    private val imagesRepository: ImagesRepository
): ViewModel() {

    val stateUpload: LiveData<String> = imagesRepository.getStateUpload()

    fun uploadImage(imUri: Uri){
        imagesRepository.uploadImage(imUri)
    }
}