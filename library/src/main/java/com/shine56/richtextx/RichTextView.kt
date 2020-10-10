package com.shine56.richtextx

import android.content.Context
import android.text.Html
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.shine56.richtextx.api.HtmlTextX
import com.shine56.richtextx.api.ImageClick
import com.shine56.richtextx.api.ImageDelete
import com.shine56.richtextx.api.PhotoBuilder

class RichTextView: AppCompatTextView, HtmlTextX{
    init {
        movementMethod = RichTextXMovementMethod.INSTANCE
    }

    override fun setTextFromHtml(htmlText: String, imageGetter: Html.ImageGetter?): PhotoBuilder {
        return ParseHtmlBuilder(htmlText, imageGetter)
    }

    inner class ParseHtmlBuilder(htmlText: String, imageGetter: Html.ImageGetter?): PhotoBuilder{
        private var customText: String = ""
        private val tagHandler = RichTextXTagHandler(false, imageGetter)

        init {
            customText = htmlText.replace("span", "myspan")
            customText = customText.replace("img", "myimg")
        }

        override fun setOnCLickListener(listener: ImageClick): PhotoBuilder {
            tagHandler.setOnImageCLickListener(listener)
            return this
        }

        override fun setOnCLickListener(block: (view: View, imgUrl: String) -> Unit): PhotoBuilder {
            tagHandler.setOnImageCLickListener(ImageClick(block))
            return this
        }

        override fun setOnDeleteListener(listener: ImageDelete): PhotoBuilder {
            tagHandler.setOnImageDeleteListener(listener)
            return this
        }

        override fun setOnDeleteListener(block: (view: View, imgUrl: String) -> Unit): PhotoBuilder {
            tagHandler.setOnImageDeleteListener(ImageDelete(block))
            return this
        }

        override fun apply(){
            setText(Html.fromHtml(customText, null, tagHandler))
        }
    }

    constructor(context: Context): super(context){
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
    }
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr){
    }


}