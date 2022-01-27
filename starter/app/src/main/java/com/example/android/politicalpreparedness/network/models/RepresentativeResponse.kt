package com.example.android.politicalpreparedness.network.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RepresentativeResponse(
    val offices: List<Office>,
    val officials: List<Official>
) : Parcelable