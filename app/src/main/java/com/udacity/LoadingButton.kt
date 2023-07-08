package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var backgroundColor = 0
    private var textColor = 0
    private var loadingBackgroundColor = 0
    private var circleColor = 0
    private var buttonText = ""
    private var progress = 0

    private val valueAnimator = ValueAnimator.ofInt(0, 360).setDuration(2000)

    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

        when (new) {
            ButtonState.Loading -> {
                buttonText = resources.getString(R.string.button_state_loading)
                valueAnimator.start()
            }
            ButtonState.Completed -> {
                buttonText = resources.getString(R.string.button_state_complete)
                valueAnimator.cancel()
                progress = 0
            }
            else -> {
                valueAnimator.start()
                postDelayed({ buttonState = ButtonState.Completed }, 2000)
            }
        }
        invalidate()
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textSize = 80.0f
        textAlign = Paint.Align.CENTER
    }

    init {
        valueAnimator.apply {
            addUpdateListener {
                progress = it.animatedValue as Int
                invalidate()
            }
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
        }

        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            backgroundColor = getColor(R.styleable.LoadingButton_backgroundColor, 0)
            textColor = getColor(R.styleable.LoadingButton_textColor, 0)
            loadingBackgroundColor = getColor(R.styleable.LoadingButton_backgroundColorLoading, 0)
            circleColor = getColor(R.styleable.LoadingButton_buttonCircleColor, 0)
        }

        buttonState = ButtonState.Completed
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minWidth: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val width: Int = resolveSizeAndState(minWidth, widthMeasureSpec, 1)
        val height: Int = resolveSizeAndState(
            MeasureSpec.getSize(width),
            heightMeasureSpec,
            0
        )
        widthSize = width
        heightSize = height
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawBackground(canvas)
        drawLoadingButton(canvas)
        drawText(canvas)
        drawCircle(canvas)
    }

    private fun drawBackground(canvas: Canvas?) {
        paint.color = backgroundColor
        canvas?.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)
    }

    private fun drawLoadingButton(canvas: Canvas?) {
        paint.color = loadingBackgroundColor
        canvas?.drawRect(0f, 0f, widthSize * progress / 360f, heightSize.toFloat(), paint)
    }

    private fun drawText(canvas: Canvas?) {
        paint.color = textColor
        canvas?.drawText(buttonText, widthSize / 2.0f, heightSize / 1.7f, paint)
    }

    private fun drawCircle(canvas: Canvas?) {
        paint.color = circleColor
        canvas?.drawArc(widthSize - 200f, 30f, widthSize - 50f, 170f, 0f, progress.toFloat(), true, paint)
    }
}