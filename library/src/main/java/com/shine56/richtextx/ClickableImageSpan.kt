package com.shine56.richtextx

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.style.DynamicDrawableSpan
import android.text.style.ImageSpan
import android.view.View
import com.shine56.richtextx.api.ImageClick
import com.shine56.richtextx.api.ImageDelete

class ClickableImageSpan(drawable: Drawable, private val imgUrl: String) :
    ImageSpan(drawable, imgUrl, DynamicDrawableSpan.ALIGN_BASELINE) {

    private var top = 0
    private var width = 0
    private var height = 0
    private var onClick: ImageClick? = null
    private var onDelete: ImageDelete? = null

    fun setOnCLickListener(listener: ImageClick?){
        onClick = listener
    }
    fun setOnDeleteListener(listener: ImageDelete){
        onDelete = listener
    }

    fun onClick(view: View, touchX: Int, touchY: Int){
        if(touchX > width - 100 && touchX < width - 20 &&
            touchY > top +20 && touchY < top+ 100){
            onDelete?.onDelete(view, imgUrl)
        }else if(touchX > 30 && touchX < width - 30 &&
            touchY > top +50 && touchY < height - 50){
            onClick?.onClick(view, imgUrl)
        }
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        val bgSize = getSize(paint, text?.substring(start, end), start, end, paint.fontMetricsInt)
        //"top = $top, bt = $bottom, x = $x, y =$y, bitmap.height = ${bitmap.height}, 宽=$bgSize".logD()
        this.top = top
        this.width = bgSize
        this.height = bottom
        super.draw(canvas, text, start, end, x, top, y, bottom, paint)

    }
}