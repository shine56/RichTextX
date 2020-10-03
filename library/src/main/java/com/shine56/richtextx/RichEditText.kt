package com.shine56.richtextx

import android.content.Context
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.shine56.richtextx.api.*

class RichEditText : AppCompatEditText, RichEditX, HtmlTextX {

    private var isInitTextFromHtml = false //判断是否为初次加载html文本
    private val richEditUtil by lazy { RichEditUtil(this) }

    init {
        //设置span点击事件movementMethod
        this.movementMethod = RichTextXMovementMethod.INSTANCE

        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //s.toString().logD("s = ")
                //richEditUtil.isCanSetFont(s?.isEmpty()?: false)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //加粗与字号
                if(!isInitTextFromHtml)
                    richEditUtil.setFontSizeAndBoldSpan(start, start+count, before)
            }
        })
    }

    override fun insertPhoto(imgUrl: String, drawableGet: DrawableGet): PhotoBuilder {
        return InsertPhotoBuilder(imgUrl, drawableGet)
    }

//    fun insertPhoto(imgUrl: String, block: (imgUrl: String) -> Drawable): PhotoBuilder {
//        val drawableGet = DrawableGet(block)
//        return InsertPhotoBuilder(imgUrl, drawableGet)
//    }

    override fun getBold(): Boolean = richEditUtil.isBold
    override fun setBold(isBold: Boolean) {
        richEditUtil.isBold = isBold
    }

    override fun getFontSize(): Int = richEditUtil.fontSize
    override fun setFontSize(fontSize: Int) {
        richEditUtil.fontSize = fontSize
    }

    override fun indent() = richEditUtil.indent()

    override fun setTextFromHtml(htmlText: String, imageGetter: Html.ImageGetter?): PhotoBuilder {
        return ParseHtmlBuilder(htmlText, imageGetter)
    }
//    private var onTextChanged :
//            ((s: CharSequence?, start: Int, before: Int, count: Int) -> Unit)? = null
//    fun setOnTextChangedListener(listener: (s: CharSequence?, start: Int, before: Int, count: Int) -> Unit){
//        onTextChanged = listener
//    }

    inner class InsertPhotoBuilder(
        private val imgUrl: String,
        private val drawableGet: DrawableGet
    ): PhotoBuilder{

        override fun setOnCLickListener(listener: ImageClick): PhotoBuilder {
            richEditUtil.setOnImageCLickListener(listener)
            return this
        }

        override fun setOnDeleteListener(listener: ImageDelete): com.shine56.richtextx.api.PhotoBuilder {
            richEditUtil.setOnImageDeleteListener(listener)
            return this
        }

        override fun apply(){
            richEditUtil.insertPhoto(imgUrl, drawableGet)
        }
    }

    inner class ParseHtmlBuilder(htmlText: String, imageGetter: Html.ImageGetter?): PhotoBuilder{
        private var customText: String = ""
        private val tagHandler = RichTextXTagHandler(true, imageGetter)

        init {
            isInitTextFromHtml = true

            customText = htmlText.replace("span", "myspan")
            customText = customText.replace("img", "myimg")
        }

        override fun setOnCLickListener(listener: ImageClick): PhotoBuilder {
            tagHandler.setOnImageCLickListener(listener)
            return this
        }

        override fun setOnDeleteListener(listener: ImageDelete): PhotoBuilder {
            tagHandler.setOnImageDeleteListener(listener)
            return this
        }

        override fun apply(){
            setText(Html.fromHtml(customText, null, tagHandler))
            isInitTextFromHtml = false
        }
    }

    constructor(context: Context): super(context){
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
    }
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr){

    }


}