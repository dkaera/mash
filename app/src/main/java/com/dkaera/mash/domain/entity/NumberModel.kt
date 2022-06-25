package com.dkaera.mash.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NumberModel(val number: Int, val date: Long) : Parcelable