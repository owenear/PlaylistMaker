package com.example.playlistmaker.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R

// Set of Material typography styles to start with
val Typography = Typography(

    titleLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.ys_display_medium, FontWeight.Bold)),
        fontWeight = FontWeight.W500,
        fontSize = 22.sp,
        letterSpacing = 0.sp
    ),

    bodyLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.ys_display_regular, FontWeight.Normal)),
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        letterSpacing = 0.sp
    )

)