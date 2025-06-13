package com.antsfamily.biketrainer.ui.util

import com.antsfamily.domain.model.Duration

fun Duration?.orEmpty(): Duration = this ?: Duration.Empty
