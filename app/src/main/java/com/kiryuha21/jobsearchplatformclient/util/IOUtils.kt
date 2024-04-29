package com.kiryuha21.jobsearchplatformclient.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.ByteArrayOutputStream
import java.io.IOException

const val DEBUG_TAG = "debug_tag"

fun Uri.getBitmap(context: Context): Bitmap {
    val source = ImageDecoder.createSource(context.contentResolver, this)
    return ImageDecoder.decodeBitmap(source)
}

suspend fun Bitmap.toRequestBody(compressionQuality: Int): RequestBody {
    return withContext(Dispatchers.IO) {
        val stream = ByteArrayOutputStream()
        this@toRequestBody.compress(Bitmap.CompressFormat.JPEG, compressionQuality, stream)
        val byteArray = stream.toByteArray()
        byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull(), 0, byteArray.size)
    }
}

suspend fun networkCallWrapper(
    networkCall: suspend () -> Unit,
    onHttpError: (HttpException) -> Unit = {},
    onIOError: (IOException) -> Unit = {},
    onOtherErrors: (Exception) -> Unit = {},
    onAnyError: () -> Unit = {},
): Boolean {
    try {
        networkCall()
        return true
    } catch (e: HttpException) {
        Log.d(DEBUG_TAG, e.message.toString())
        onHttpError(e)
    } catch (e: IOException) {
        Log.d(DEBUG_TAG, e.message.toString())
        onIOError(e)
    } catch (e: Exception) {
        Log.d(DEBUG_TAG, e.message.toString())
        onOtherErrors(e)
    }
    onAnyError()
    return false
}

suspend fun <T : Any> networkCallWithReturnWrapper(
    networkCall: suspend () -> T,
    onHttpError: (HttpException) -> Unit = {},
    onIOError: (IOException) -> Unit = {},
    onOtherErrors: (Exception) -> Unit = {},
    onAnyError: () -> Unit = {},
): T? {
    try {
        return networkCall()
    } catch (e: HttpException) {
        Log.d(DEBUG_TAG, e.message.toString())
        onHttpError(e)
    } catch (e: IOException) {
        Log.d(DEBUG_TAG, e.message.toString())
        onIOError(e)
    } catch (e: Exception) {
        Log.d(DEBUG_TAG, e.message.toString())
        onOtherErrors(e)
    }
    onAnyError()
    return null
}