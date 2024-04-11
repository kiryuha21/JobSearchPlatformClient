package com.kiryuha21.jobsearchplatformclient.data.remote

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type


class NullOnEmptyConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val delegate = retrofit.nextResponseBodyConverter<ResponseBody>(this, type, annotations)
        return Converter<ResponseBody, Any> {
            if (it.contentLength() == 0L) {
                return@Converter null
            }

            return@Converter delegate.convert(it)
        }
    }
}