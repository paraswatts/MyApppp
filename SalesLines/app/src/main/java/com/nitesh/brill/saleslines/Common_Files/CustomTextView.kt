package com.nitesh.brill.saleslines.Common_Files

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
import com.nitesh.brill.saleslines.R


/**
 * Created by Nitesh Android on 22-08-2017.
 */

class CustomTextView : TextView {
    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }


    private fun init(attrs: AttributeSet?) {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView)
            val fontName = a.getString(R.styleable.CustomTextView_font)

            try {
                if (fontName != null) {
                    // val myTypeface = Typeface.createFromAsset(context.assets, "fonts/" + fontName)
                    val myTypeface = Typeface.createFromAsset(context.assets, "fonts/OpenSans-Regular.ttf")

                    typeface = myTypeface
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            a.recycle()
        }
    }


}