package com.example.playlistmaker.presentation.player.activity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import com.example.playlistmaker.R

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes){

    private val playImageBitmap: Bitmap?
    private val pauseImageBitmap: Bitmap?
    private var imageRect = RectF(0f, 0f, 0f, 0f)
    private var buttonStatus = BUTTON_PLAY
    set(newValue) {
        field = newValue
        invalidate()
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackButtonView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                playImageBitmap = getDrawable(R.styleable.PlaybackButtonView_playIcon)?.toBitmap()
                pauseImageBitmap = getDrawable(R.styleable.PlaybackButtonView_pauseIcon)?.toBitmap()
            } finally {
                recycle()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        imageRect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        when (buttonStatus) {
            BUTTON_PLAY -> playImageBitmap?.let { canvas.drawBitmap(it, null, imageRect, null) }
            BUTTON_PAUSE -> pauseImageBitmap?.let { canvas.drawBitmap(it, null, imageRect, null) }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                return true
            }
            MotionEvent.ACTION_UP -> {
                if (isEnabled) {
                    buttonStatus = if (buttonStatus == BUTTON_PLAY) BUTTON_PAUSE else BUTTON_PLAY
                    performClick()
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }

    fun setButtonImage(buttonStatus: Int) {
        when (buttonStatus) {
            BUTTON_PLAY -> this.buttonStatus = BUTTON_PLAY
            BUTTON_PAUSE -> this.buttonStatus = BUTTON_PAUSE
        }
    }

    companion object {
        const val BUTTON_PLAY = 0
        const val BUTTON_PAUSE = 1
    }

}