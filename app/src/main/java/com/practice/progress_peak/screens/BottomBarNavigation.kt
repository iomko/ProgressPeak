package com.practice.progress_peak.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.practice.progress_peak.R

@Composable
fun BottomBarNavigation(
    onClickHomeIcon: (() -> Unit)? = null,
    onClickInfoIcon: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(255, 165, 0)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(onClick = { onClickHomeIcon?.let { it() } }) {
            Icon(Icons.Default.Home, contentDescription = "", tint = colorResource(R.color.white))
        }
        IconButton(onClick = { onClickInfoIcon?.let { it() } }) {
            Icon(Icons.Default.Info, contentDescription = "", tint = colorResource(R.color.white))
        }
    }
}