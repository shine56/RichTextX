package com.shine56.richtextx

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View

class RichEditText : androidx.appcompat.widget.AppCompatEditText {

    constructor(context: Context): super(context){

    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
    }
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr){

    }

    var isBold = false

    private val editText = this

    private val richText = RichText(this)

    private var isTextFromHtml = false

    private var onTextChanged :
            ((s: CharSequence?, start: Int, before: Int, count: Int) -> Unit)? = null
    fun setOnTextChangedListener(listener: (s: CharSequence?, start: Int, before: Int, count: Int) -> Unit){
        onTextChanged = listener
    }

    init {
        //设置span点击事件movementMethod
        this.movementMethod = RichTextXMovementMethod.INSTANCE

        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //s.toString().logD("s = ")
                //richText.isCanSetFont(s?.isEmpty()?: false)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // s.toString().logD("输入中：")
                //加粗
                richText.setBold(isBold)
                //字体
                if(!isTextFromHtml)
                    richText.setFontSizeSpan(start, start+count, before)

                onTextChanged?.invoke(s, start, before, count)
            }
        })
    }

    /**
     * 初始化html文本
     */

    fun setTextFromHtml(htmlText: String, imageGetter: Html.ImageGetter): ParseHtmlBuilder{
        return ParseHtmlBuilder(htmlText, imageGetter)
    }

    /**
     * 插入图片
     */
    fun insertPhoto(
        imageUrl: String,
        getDrawable: (imagePath: String) -> Drawable
    ): InsertPhotoBuilder{
        return InsertPhotoBuilder(imageUrl, getDrawable)
    }

    /**
     * 缩进
     */
    fun indent(){
        val index = selectionStart //获取光标所在位置
        val editableText = editableText
        if (index < 0 || index >= editableText.length) {
            editableText.append("\u3000\u3000")
        } else {
            editableText.insert(index, "\u3000\u3000")
        }
    }

    /**
     * 字号
     */
    fun setFontSize(fontSize: Int){
        richText.setFontSize(fontSize)
    }

    /**
     * 加粗
     */
    fun setFontStyleBold(boolean: Boolean){
        isBold = boolean
    }

    inner class InsertPhotoBuilder(
        private val imageUrl: String,
        private val getDrawable: (imagePath: String) -> Drawable
    ){
//        private var onClick: ((view: View, imgUrl: String) -> Unit)? = null
//        private var onDelete: ((view: View, imgUrl: String) -> Unit)? = null

        fun setOnCLickListener(listener: (view: View, imgUrl: String) -> Unit): InsertPhotoBuilder{
            richText.setOnImageCLickListener(listener)
            return this
        }
        fun setOnDeleteListener(listener: (view: View, imgUrl: String) -> Unit): InsertPhotoBuilder{
            richText.setOnImageDeleteListener(listener)
            return this
        }

        fun apply(){
            richText.insertPhoto(imageUrl, getDrawable)
        }
    }

    inner class ParseHtmlBuilder(htmlText: String, imageGetter: Html.ImageGetter){
        private var properText: String = ""

        private val tagHandler = RichTextXTagHandler(editText, imageGetter)

        init {
            isTextFromHtml = true

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
            editText.setText(Html.fromHtml(properText, null, tagHandler))
            isTextFromHtml = false
        }
    }
}