package com.shine56.richtextx.util

import android.text.Selection
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.MotionEvent
import android.widget.TextView
import com.shine56.richtextx.api.HtmlTextX.TAG
import com.shine56.richtextx.view.ClickableImageSpan
import kotlin.math.abs

class RichTextXMovementMethod: LinkMovementMethod() {
    private var startY = 0f
    private var scrollY = 0f

    companion object{
        val INSTANCE : RichTextXMovementMethod by lazy { RichTextXMovementMethod() }
    }

    override fun onTouchEvent(widget: TextView?, buffer: Spannable?, event: MotionEvent?): Boolean {
        val action = event!!.action
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
            var x = event!!.x.toInt()
            var y = event!!.y.toInt()

            x -= widget!!.totalPaddingLeft
            y -= widget!!.totalPaddingTop

            x += widget!!.scrollX
            y += widget!!.scrollY

            val layout = widget!!.layout
            val line = layout.getLineForVertical(y)
            var off = layout.getOffsetForHorizontal(line, x.toFloat())

            val imageSpans = buffer!!.getSpans(off, off, ClickableImageSpan::class.java)

            when {
                imageSpans.isNotEmpty() -> {
                    val link = imageSpans[0]
                    if (action == MotionEvent.ACTION_UP) {
                        //Log.d(TAG, "onTouchEvent: 抬起手指 isMove=$isMove")
                        if(scrollY < 10){
                            link.onClick(widget, x, y)
                        }

                    }else if (action == MotionEvent.ACTION_DOWN) {
                        //Log.d(TAG, "onTouchEvent: 放下手指")
                        startY = event.y
                       //  取消图片选中
//                        Selection.setSelection(
//                            buffer,
//                            buffer!!.getSpanStart(link),
//                            buffer!!.getSpanEnd(link)
//                        )
                    }
                }
                else -> {
                    Selection.removeSelection(buffer)
                }
            }
        }
        if (action == MotionEvent.ACTION_MOVE){
            scrollY = abs(event.y - startY)
            //Log.d(TAG, "onTouchEvent: 滑动距离=${scrollY}")
        }
        return super.onTouchEvent(widget, buffer, event)
    }
}