package com.antsfamily.biketrainer.util

import android.content.ContentResolver
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppContentResolver @Inject constructor(@ApplicationContext context: Context) :
    ContentResolver(context)
