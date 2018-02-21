package com.nitesh.brill.saleslines.Common_Files

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * Created by Gursimar on 21-12-2016.
 */
class CustomView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    private val radius: Float
    private val path = Path()
    private val rect = RectF()

    init {
        this.radius = attrs.getAttributeFloatValue(null, "corner_radius", 0f)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // compute the path
        val halfWidth = w / 2f
        val halfHeight = h / 2f
        val centerX = halfWidth
        val centerY = halfHeight
        path.reset()
        path.addCircle(centerX, centerY, Math.min(halfWidth, halfHeight), Path.Direction.CW)
        path.close()

    }

    override fun dispatchDraw(canvas: Canvas) {
        val save = canvas.save()
        canvas.clipPath(path)
        super.dispatchDraw(canvas)
        canvas.restoreToCount(save)
    }

}
