package ru.netology.nmedia

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable
import java.net.HttpURLConnection

@Serializable
@Parcelize
class PostEditableContent(
    val content: String?,
    val videoUrl: String? = null
) : Parcelable {

}