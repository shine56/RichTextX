package com.shine56.richtextx

import android.text.Selection
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.widget.TextView

class RichTextXMovementMethod: LinkMovementMethod() {

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

//            val xLeft = layout.getPrimaryHorizontal(off)
//            if (xLeft < x) {
//                off += 1
//            } else {
//                off -= 1
//            }

            val links = buffer!!.getSpans(off, off, ClickableSpan::class.java)
            val imageSpans = buffer!!.getSpans(off, off, ClickableImageSpan::class.java)

            when {
                links.isNotEmpty() -> {
                    val link = links[0]
                    if (action == MotionEvent.ACTION_UP) {
                        link.onClick(widget)
                    } else if (action == MotionEvent.ACTION_DOWN) {
                        Selection.setSelection(
                            buffer,
                            buffer!!.getSpanStart(link),
                            buffer!!.getSpanEnd(link)
                        )
                    }
                    return true
                }
                imageSpans.isNotEmpty() -> {

                    val link = imageSpans[0]

                    if (action == MotionEvent.ACTION_UP) {
                        link.onClick(widget, x, y)
                    }else if (action == MotionEvent.ACTION_DOWN) {
                        // 取消图片选中
//                        Selection.setSelection(
//                            buffer,
//                            buffer!!.getSpanStart(link),
//                            buffer!!.getSpanEnd(link)
//                        )
                    }
                    return true
                }
                else -> {
                    Selection.removeSelection(buffer)
                }
            }
        }
//        if (action == MotionEvent.ACTION_MOVE){
//            return true
//        }

        return false
    }
}