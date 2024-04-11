package com.kiryuha21.jobsearchplatformclient.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

fun Uri.getBitmap(context: Context): Bitmap {
    val source = ImageDecoder.createSource(context.contentResolver, this)
    return ImageDecoder.decodeBitmap(source)
}

fun Bitmap.toRequestBody(compressionQuality: Int): RequestBody {
    val stream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, compressionQuality, stream)
    val byteArray = stream.toByteArray()
    return byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull(), 0, byteArray.size)
}