package com.nitesh.brill.saleslines.Common_Files

/**
 * Created by Nitesh Android on 03-10-2017.
 */

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.nitesh.brill.saleslines.R
import java.util.*


class FunnelChart(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    internal var data: List<ChartData_>? = null


    override fun onDraw(canvas: Canvas) {

        var startTop = 50
        val startLeft = 50
        val endBottom = height - 50
        val endRight = width - 50
        val startLeftSegment = 50
        val differenceHeight = endBottom - startTop
        val paint = Paint()
        paint.style = Paint.Style.FILL
        val colors = resources.getIntArray(R.array.colors)

        if (this.data != null) {
            val segmentHeight = differenceHeight / this.data!!.size
            for (i in this.data!!.indices) {
                val rnd = Random()

                Log.e("Index in funnel chart", i.toString())
                //val color = colors[i+1]
                // val myColor = Color.parseColor(this.data!![i].pyramidColor)

                paint.color = colors[i]

                val total = total
                Log.e("segment total", "" + total)
                Log.e("segment", "" + this.data!![i].pyramid_value)
                val segment = (this.data!![i].pyramid_value * 100 / total).toFloat()

                Log.e("segment", "" + segment)
                val bottom = startTop + segmentHeight
                // val bottom = startTop + (endBottom * segment.toInt() / 100).toInt()
                Log.e("start and bottom", startTop.toString() + " " + bottom)
                val xMax = endRight / 4 + 25 - 50


                val rect = Rect(startLeft, startTop, endRight, bottom)
                canvas.drawRect(rect, paint)
                val paintText = Paint()
                val widthDifference = endRight - startLeft


                paintText.setTextSize(32f)
//                var textWidth = paintText.measureText(this.data!![i].pyramidLabel+" -: "+this.data!![i].pyramid_value)
//                Log.e( "text Width", (textWidth).toString())
//                if (textWidth >= differenceWidth) {
//                    //Log.e("Long Text Size", String.valueOf(differenceWidth * 50 / endRight))
//                    paintText.setTextSize((differenceWidth * 50 / endRight).toFloat())
//                    textWidth = paintText.measureText(this.data!![i].pyramidLabel+" -: "+this.data!![i].pyramid_value)
//                } else {
//                    paintText.setTextSize(50f)
//                    textWidth = paintText.measureText(this.data!![i].pyramidLabel+" -: "+this.data!![i].pyramid_value)
//                }
                //paintText.setTextSize(35F);
                // canvas.drawText(""+this.data!![i].pyramidLabel+" -: "+this.data!![i].pyramid_value, startLeft+170f, bottom-(segmentHeight/3f) , paintText);
                //val textWidth = paintText.measureText(this.data!![i].pyramidLabel+" -: "+this.data!![i].pyramid_value)
                val textWidth = paintText.measureText(this.data!![i].pyramidLabel + " -: " + this.data!![i].pyramid_value)

                Log.e("widthDifference========", "" + widthDifference)
                //Log.e("textWidth========", "" + textWidth)

                canvas.drawText(this.data!![i].pyramidLabel + " - " + this.data!![i].pyramid_value, startLeft + (widthDifference / 2 - (textWidth / 2)), bottom - (segmentHeight / 3f), paintText);

                startTop = bottom


            }
        }
        val path = Path()
        path.reset()


        path.moveTo(50f, 50f)
        //path.lineTo(endRight / 4 + 25.toFloat(), endBottom / 2 + 50.toFloat())
        path.lineTo(endRight / 4 + 25.toFloat(), endBottom + 50.toFloat())
        //path.lineTo(endRight / 4, (endBottom/3)+50);
        path.lineTo(50.toFloat(), endBottom + 50.toFloat())
        path.moveTo(endRight.toFloat(), 50.toFloat())
        //path.lineTo(25 + (endRight - endRight / 4).toFloat(), endBottom / 2 + 50.toFloat())
        path.lineTo(25 + (endRight - endRight / 4.toFloat()), endBottom + 50.toFloat())
        //path.lineTo(endRight, endBottom+50);
        path.lineTo(endRight.toFloat(), endBottom + 50.toFloat())
        path.close()

        paint.color = Color.WHITE
        // Clipping canvas to path and drawing small area
        canvas.save()
        canvas.clipPath(path)
        canvas.drawPath(path, paint)
        canvas.restore()
    }


    private val total: Int
        get() {

            Log.e("data string", this.data!!.size.toString() + "")
            var total = 0
            for (i in this.data!!.indices) {

                total += this.data!![i].pyramid_value
            }
            Log.e("data string", total.toString() + "")
            return total
        }

    fun setData(data: List<ChartData_>) {

        this.data = data
        invalidate()
    }
}
