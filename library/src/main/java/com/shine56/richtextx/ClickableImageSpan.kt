package com.shine56.richtextx

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.style.DynamicDrawableSpan
import android.text.style.ImageSpan
import android.util.Log
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
        Log.d("调试ClickableImageSpan->", "touchX=$touchX, touchY=$touchY, width=$width, height=$height, top=$top")
        if(touchX > width - 100 && touchX < width  &&
            touchY > top && touchY < top+ 100){
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

        this.top = top
        this.width = canvas.width
        this.height = bottom
        super.draw(canvas, text, start, end, x, top, y, bottom, paint)

        Log.d("调试", "top=$top, height=$height, x = $x, y =$y, drawable.intrinsicWidth=${drawable.intrinsicWidth}, " +
                "bgSize=$bgSize, 画布宽${canvas.width} 画布高${canvas.height}")
    }
}