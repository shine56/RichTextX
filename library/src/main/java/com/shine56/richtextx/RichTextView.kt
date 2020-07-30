package com.shine56.richtextx

import android.content.Context
import android.text.Html
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView

class RichTextView: AppCompatTextView {
    constructor(context: Context): super(context){

    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
    }
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr){

    }

    private val textView = this

    init {
        this.movementMethod = RichTextXMovementMethod.INSTANCE
    }

    /**
     * 初始化html文本
     */
    fun setTextFromHtml(htmlText: String, imageGetter: Html.ImageGetter): ParseHtmlBuilder {
        return ParseHtmlBuilder(htmlText, imageGetter)
    }

    inner class ParseHtmlBuilder(htmlText: String, imageGetter: Html.ImageGetter){
        private var properText: String = ""

        private val tagHandler = RichTextXTagHandler(null, imageGetter)

        init {
            properText = htmlText.replace("span", "myspan")
            properText = properText.replace("img", "myimg")
        }

        fun setOnCLickListener(listener: (view: View, imgUrl: String) -> Unit): ParseHtmlBuilder{
            tagHandler.setOnImageCLickListener(listener)
            return this
        }
        fun setOnDeleteListener(listener: (view: View, imgUrl: String) -> Unit): ParseHtmlBuilder{
            tagHandler.setOnImageDeleteListener(listener)
            return this
        }

        fun apply(){
            textView.setText(Html.fromHtml(properText, null, tagHandler))
        }
    }
}