package com.openpay.android.ui.images

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.concurrent.futures.await
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.openpay.android.R
import com.openpay.android.databinding.FragmentImagesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ImagesFragment : Fragment() {

    private val GALLERY = 1

    private var _binding: FragmentImagesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var storage: FirebaseStorage

    private var imageUri: Uri? = null
    private lateinit var previewView: PreviewView
    private lateinit var imageCapture: ImageCapture
    private var preview: Preview? = null
    private val REQUEST_CODE_PERMISSIONS = 10

    private val imagesViewModel: ImagesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        storage = Firebase.storage
        _binding = FragmentImagesBinding.inflate(inflater, container, false)
        previewView = binding.previewView

        binding.buttonTakePicture.visibility = View.INVISIBLE

        setStyleButtons()

        observeUpload()
        return binding.root
    }

    private fun observeUpload() {
        imagesViewModel.stateUpload.observe(viewLifecycleOwner) {
            when (it) {
                "Success" -> onSuccess()
                "Error" -> showError()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonPickImage.setOnClickListener {
            showPictureDialog()
        }
        binding.buttonUploadImage.setOnClickListener {
            uploadImage()
        }
        binding.buttonTakePicture.setOnClickListener {
            takePicture()
        }
    }

    private fun initPreview() {
        preview = Preview.Builder().build()
        preview?.setSurfaceProvider(previewView.surfaceProvider)
    }

    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                binding.root.context,
                android.Manifest.permission.CAMERA
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(android.Manifest.permission.CAMERA),
                REQUEST_CODE_PERMISSIONS
            )
        } else {
            startCamera()
        }
    }

    private fun startCamera() {
        startCameraViews()
        initPreview()
        initImageCapture()
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
        lifecycleScope.launch {
            val cameraProvider = ProcessCameraProvider
                .getInstance(binding.root.context)
                .await()
            try {
                cameraProvider.unbindAll() // unbind all usecases
                cameraProvider.bindToLifecycle(
                    this@ImagesFragment,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                Log.e("Camera", "Error: binding usecases $e")
            }
        }
    }

    private fun startCameraViews() {
        binding.buttonTakePicture.visibility = View.VISIBLE
        binding.buttonPickImage.visibility = View.INVISIBLE
        binding.previewView.visibility = View.VISIBLE
        binding.imageView.visibility = View.INVISIBLE
    }

    private fun initImageCapture() {
        imageCapture = ImageCapture.Builder()
            .setJpegQuality(100)
            .build()
    }

    private fun canTakePicture() {
        requestCameraPermission()
    }

    private fun takePicture() {
        imageCapture.takePicture(
            ContextCompat.getMainExecutor(binding.root.context),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    binding.buttonTakePicture.visibility = View.INVISIBLE
                    binding.buttonPickImage.visibility = View.VISIBLE
                    val bitmap = image.convertImageProxyToBitmap()
                    imageUri = getImageUri(bitmap)
                    binding.imageView.visibility = View.VISIBLE
                    binding.imageView.setImageBitmap(bitmap)
                    binding.previewView.visibility = View.INVISIBLE
                    image.close()
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.d("Camera", "Image capture failed ${exception.message}")
                }
            })
    }

    private fun getImageUri(inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            binding.root.context.contentResolver,
            inImage,
            "Title",
            null
        )
        return Uri.parse(path ?: "")
    }

    fun ImageProxy.convertImageProxyToBitmap(): Bitmap {
        val buffer = planes[0].buffer
        buffer.rewind()
        val bytes = ByteArray(buffer.capacity())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            requestCameraPermission()
        }
    }

    private fun uploadImage() {
        imageUri?.let { imagesViewModel.uploadImage(it) }
    }

    private fun onSuccess() {
        binding.imageView.setImageURI(null)
        binding.imageView.visibility = View.INVISIBLE
        imageUri = null
        Toast.makeText(context, "Successfully Uploaded", Toast.LENGTH_LONG).show()
    }

    private fun showError() {
        Toast.makeText(context, "Fail to Uploaded", Toast.LENGTH_LONG).show()
    }

    private fun choosePhotoFromGallery() {
        binding.previewView.visibility = View.INVISIBLE
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(binding.root.context)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(
            pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallery()
                1 -> canTakePicture()

            }
        }
        pictureDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY) {
            if (data != null) {
                imageUri = data.data
                binding.previewView.visibility = View.INVISIBLE
                binding.imageView.visibility = View.VISIBLE
                binding.imageView.setImageURI(imageUri)
            }
        }
    }

    private fun setStyleButtons() {
        binding.buttonUploadImage.setTextColor(
            ContextCompat.getColor(
                binding.root.context,
                R.color.white
            )
        )
        binding.buttonUploadImage.setBackgroundColor(
            ContextCompat.getColor(
                binding.root.context,
                R.color.black
            )
        )
        binding.buttonTakePicture.setTextColor(
            ContextCompat.getColor(
                binding.root.context,
                R.color.white
            )
        )
        binding.buttonTakePicture.setBackgroundColor(
            ContextCompat.getColor(
                binding.root.context,
                R.color.black
            )
        )
        binding.buttonPickImage.setTextColor(
            ContextCompat.getColor(
                binding.root.context,
                R.color.white
            )
        )
        binding.buttonPickImage.setBackgroundColor(
            ContextCompat.getColor(
                binding.root.context,
                R.color.black
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}