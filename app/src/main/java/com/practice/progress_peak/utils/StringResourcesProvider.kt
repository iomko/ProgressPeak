package com.practice.progress_peak.utils

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

//Kód použitý z odkazu: https://dev.to/sandeepsatpute9271/resourcesrstring-in-viewmodel-in-android-553g
@Singleton
class StringResourcesProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getString(@StringRes stringResId: Int): String {
        return context.getString(stringResId)
    }
}