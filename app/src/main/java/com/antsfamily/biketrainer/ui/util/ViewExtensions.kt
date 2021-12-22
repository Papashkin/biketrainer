package com.antsfamily.biketrainer.ui.util

import android.animation.Animator
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.airbnb.lottie.LottieAnimationView

fun View.hideKeyboard() {
    val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    manager.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboard() {
    val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    manager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun LottieAnimationView.onAnimationEndListener(listener: () -> Unit) {
    this.addAnimatorListener(object :
        Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {}

        override fun onAnimationEnd(animation: Animator?) {
            listener.invoke()
        }

        override fun onAnimationCancel(animation: Animator?) {}

        override fun onAnimationRepeat(animation: Animator?) {}
    })
}
