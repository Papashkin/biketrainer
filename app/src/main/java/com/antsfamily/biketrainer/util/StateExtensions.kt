package com.antsfamily.biketrainer.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.antsfamily.biketrainer.presentation.StatefulViewModel

fun <X, Y> StatefulViewModel<X>.mapDistinct(mapFunction: (input: X) -> Y): LiveData<Y> =
    Transformations.map(this.state, mapFunction).distinctUntilChanged()

fun <X> LiveData<X>.distinctUntilChanged(): LiveData<X> = Transformations.distinctUntilChanged(this)
