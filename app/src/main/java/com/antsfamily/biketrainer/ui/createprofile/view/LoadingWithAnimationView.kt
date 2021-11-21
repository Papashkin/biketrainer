package com.antsfamily.biketrainer.ui.createprofile.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.antsfamily.biketrainer.databinding.ViewLoadingWithAnimationBinding
import com.antsfamily.biketrainer.presentation.createprofile.model.LoadingState
import com.antsfamily.biketrainer.ui.util.onAnimationEndListener

class LoadingWithAnimationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: ViewLoadingWithAnimationBinding =
        ViewLoadingWithAnimationBinding.inflate(LayoutInflater.from(context), this, true)

    private var onAnimationFinishedListener: (() -> Unit)? = null

    var state: LoadingState = LoadingState.Nothing
        set(value) {
            field = value
            handleState(value)
        }

    init {
        setupView()
    }

    fun setOnAnimationFinishedListener(listener: () -> Unit) {
        onAnimationFinishedListener = listener
    }

    private fun setupView() {
        binding.lottieAnimationView.onAnimationEndListener {
            onAnimationFinishedListener?.invoke()
        }
    }

    private fun handleState(state: LoadingState) {
        when (state) {
            LoadingState.Loading -> showLoadingView()
            LoadingState.Nothing -> hideView()
            is LoadingState.Success -> showLottieAnimation(state.fileName)
        }
    }

    private fun hideView() {
        binding.loadingWithAnimationView.isVisible = false
    }

    private fun showLoadingView() {
        with(binding) {
            loadingWithAnimationView.isVisible = true
            loadingPb.isVisible = true
            lottieAnimationView.isVisible = false
        }
    }

    private fun showLottieAnimation(fileName: String) {
        with(binding) {
            loadingWithAnimationView.isVisible = true
            loadingPb.isVisible = false
            lottieAnimationView.apply {
                setAnimation(fileName)
                isVisible = true
                playAnimation()
            }
        }
    }
}
