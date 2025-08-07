package com.example.rtlvodapp.model

import android.os.Parcel
import android.os.Parcelable

data class VideoItem(
    val _id: String,
    val title: String,
    val description: String,
    val category: String,
    val filePath: String,
    val thumbnailPath: String? = null,
    val date: String  // Fixed: Changed to String to match API's ISO format "2025-07-29T22:01:52.401Z"
) : Parcelable {  // Keep Parcelable label

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readString() ?: ""  // Updated to read String
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(category)
        parcel.writeString(filePath)
        parcel.writeString(thumbnailPath)
        parcel.writeString(date)  // Updated to write String
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VideoItem> {
        override fun createFromParcel(parcel: Parcel): VideoItem {
            return VideoItem(parcel)
        }

        override fun newArray(size: Int): Array<VideoItem?> {
            return arrayOfNulls(size)
        }
    }
}