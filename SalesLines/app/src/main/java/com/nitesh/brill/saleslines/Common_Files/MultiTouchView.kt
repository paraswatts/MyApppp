package com.nitesh.brill.saleslines.Common_Files

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.LinearLayout

class MultiTouchView : LinearLayout {
    internal var mScaleFactor = 1f
    internal var mPivotX: Float = 0.toFloat()
    internal var mPivotY: Float = 0.toFloat()
    internal val MAX_SCALE = 20.0f
    internal val MIN_SCALE = 20.0f

    constructor(context: Context) : super(context) {
        // TODO Auto-generated constructor stub
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        // TODO Auto-generated constructor stub
    }

    constructor(context: Context, attrs: AttributeSet,
                defStyle: Int) : super(context, attrs, defStyle) {
        // TODO Auto-generated constructor stub
    }

    override fun dispatchDraw(canvas: Canvas) {
        canvas.save(Canvas.MATRIX_SAVE_FLAG)
        canvas.scale(mScaleFactor, mScaleFactor, mPivotX, mPivotY)
        super.dispatchDraw(canvas)
        canvas.restore()
    }

    fun scale(scaleFactor: Float, pivotX: Float, pivotY: Float) {
        mScaleFactor = scaleFactor
        mPivotX = pivotX
        mPivotY = pivotY
        this.invalidate()
    }

    fun restore() {
        mScaleFactor = 1f
        this.invalidate()
    }

    fun relativeScale(scaleFactor: Float, pivotX: Float, pivotY: Float) {
        var pivotX = pivotX
        var pivotY = pivotY
        mScaleFactor *= scaleFactor

        if (scaleFactor >= 1) {
            mPivotX = mPivotX + (pivotX - mPivotX) * (1 - 1 / scaleFactor)
            mPivotY = mPivotY + (pivotY - mPivotY) * (1 - 1 / scaleFactor)
        } else {
            pivotX = (width / 2).toFloat()
            pivotY = (height / 2).toFloat()

            mPivotX = mPivotX + (pivotX - mPivotX) * (1 - scaleFactor)
            mPivotY = mPivotY + (pivotY - mPivotY) * (1 - scaleFactor)
        }

        this.invalidate()
    }

    fun release() {
        if (mScaleFactor < MIN_SCALE) {
            val startScaleFactor = mScaleFactor

            val a = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                    scale(startScaleFactor + (MIN_SCALE - startScaleFactor) * interpolatedTime, mPivotX, mPivotY)
                }
            }

            a.duration = 300
            startAnimation(a)
        } else if (mScaleFactor > MAX_SCALE) {
            val startScaleFactor = mScaleFactor

            val a = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                    scale(startScaleFactor + (MAX_SCALE - startScaleFactor) * interpolatedTime, mPivotX, mPivotY)
                }
            }

            a.duration = 300
            startAnimation(a)
        }
    }


}

