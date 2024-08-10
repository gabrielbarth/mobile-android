package com.gabrielbarth.affirmations.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Affirmation(
    @StringRes var stringResourceId: Int,
    @DrawableRes var imageResourceId: Int
)