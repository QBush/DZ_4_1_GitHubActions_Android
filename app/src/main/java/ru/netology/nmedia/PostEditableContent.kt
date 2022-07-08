package ru.netology.nmedia

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.net.HttpURLConnection

@Parcelize
class PostEditableContent(
    val content: String?,
    val videoUrl: String? = null
) : Parcelable {

}