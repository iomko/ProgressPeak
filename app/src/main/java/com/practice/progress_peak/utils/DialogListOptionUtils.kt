package com.practice.progress_peak.utils

import com.maxkeppeler.sheets.list.models.ListOption

fun getStringNameFromListOfOptions(list: MutableList<ListOption>, index: Int): String {
    require(index in list.indices) { "Index out of bounds" }
    return list[index].titleText
}

fun updateSelectedElementInListOfOptions(list: MutableList<ListOption>, index: Int, isSelected: Boolean) {
    require(index in list.indices) { "Index out of bounds" }
    val optionToUpdate = list[index]
    val updatedOption = optionToUpdate.copy(selected = isSelected)
    list[index] = updatedOption
}