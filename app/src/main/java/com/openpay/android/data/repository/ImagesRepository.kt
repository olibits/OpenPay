package com.openpay.android.data.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

interface ImagesRepository {
    fun uploadImage(imageUri: Uri)
    fun getStateUpload():LiveData<String>
}

@ExperimentalCoroutinesApi
class ImagesRepositoryImpl @Inject constructor(
    private var storage: FirebaseStorage
) : ImagesRepository {

    private var _stateUpload = MutableLiveData<String>()

    override fun getStateUpload(): LiveData<String> = _stateUpload

    override fun uploadImage(imageUri: Uri) {
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH)
        val currentDate = Date()
        val fileName = formatter.format(currentDate)
        //val storageReference = FirebaseStorage.getInstance().reference
        val storageReference = storage.reference
        //val mFileRef = storageReference.child("${fileName}.webp")
        val mPathRef = storageReference.child("images/${fileName}.jpg")
        //val mPathRef = storageReference.child("images/${imageUri?.lastPathSegment}")
        val uploadTask = mPathRef.putFile(imageUri!!)
        uploadTask.addOnSuccessListener {
            _stateUpload.value = "Success"
        }.addOnFailureListener {
            _stateUpload.value = "Error"
        }
    }


}