package com.antsfamily.biketrainer.ui.createworkout.view

import android.content.Context
import android.text.InputFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.databinding.ViewDurationBinding
import com.antsfamily.biketrainer.ui.util.afterTextChange
import com.antsfamily.biketrainer.ui.util.getStyledAttributes
import com.antsfamily.biketrainer.ui.util.setOnDoneActionListener
import com.antsfamily.biketrainer.ui.util.showKeyboard
import com.antsfamily.domain.antservice.orZero

class DurationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: ViewDurationBinding =
        ViewDurationBinding.inflate(LayoutInflater.from(context), this, true)

    private var onCodeChangedListener: (() -> Unit)? = null

    var text: String = ""
        set(value) {
            field = value
            renderCode()
        }

    var error: String? = null
        set(value) {
            field = value
            setupErrorView(value)
        }

    init {
        context.getStyledAttributes(attrs, R.styleable.DurationView) {
            binding.titleTv.text = getString(R.styleable.DurationView_title)
        }

        with(binding) {
            durationEt.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(CODE_LENGTH))
            durationEt.afterTextChange { this@DurationView.text = it }
            root.setOnClickListener { setFocus() }
        }
    }

    fun getDurationValue(): Long = with(binding) {
        secondsValueTv.text.toString().toIntOrNull().orZero() +
                minutesValueTv.text.toString().toIntOrNull().orZero()
                    .times(MINUTES_TO_SECONDS_MULTIPLIER) +
                hoursValueTv.text.toString().toIntOrNull().orZero()
                    .times(HOURS_TO_SECONDS_MULTIPLIER)
    }

    fun setFocus() {
        with(binding) {
            durationEt.requestFocus()
            durationEt.focusOnLastLetter()
            // A delay added here as per https://stackoverflow.com/a/43516620
            durationEt.postDelayed({ durationEt.showKeyboard() }, 250)
        }
    }

    private fun renderCode() {
        val basicDuration = ZEROS.plus(text).takeLast(6)

        binding.hoursValueTv.text = basicDuration.take(2)
        binding.minutesValueTv.text = basicDuration.take(4).takeLast(2)
        binding.secondsValueTv.text = basicDuration.takeLast(2)

        onCodeChangedListener?.invoke()
    }

    private fun EditText.focusOnLastLetter() = setSelection(text.length)

    fun setOnDoneActionListener(function: () -> Unit) {
        binding.durationEt.setOnDoneActionListener(function)
    }

    fun setOnCodeChangedListener(function: () -> Unit) {
        onCodeChangedListener = function
    }

    private fun setupErrorView(error: String?) {
        with(binding.errorTv) {
            isVisible = !error.isNullOrBlank()
            text = error
        }
    }

    companion object {
        private const val MINUTES_TO_SECONDS_MULTIPLIER = 60L
        private const val HOURS_TO_SECONDS_MULTIPLIER = 3600L
        private const val CODE_LENGTH = 6
        private const val ZEROS = "000000"
    }
}
