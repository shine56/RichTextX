package com.shine56.richtextx

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.shine56.richtextx.api.*

class RichEditText : AppCompatEditText, RichEditX, HtmlTextX {

    internal var needRefreshText = true //判断是否需要更新文本
    private val richEditUtil by lazy { RichEditUtil(this) }
    private var photoBuilder: PhotoBuilder? = null

    init {
        //设置span点击事件movementMethod
        this.movementMethod = RichTextXMovementMethod.INSTANCE

        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //加粗与字号
                if(needRefreshText) {
                    richEditUtil.setFontSizeAndBoldSpan(start, start+count, before)
                }
            }
        })
    }

    override fun insertPhoto(imgUrl: String, drawableGet: DrawableGet): PhotoBuilder {
        return InsertPhotoBuilder(imgUrl, drawableGet)
    }

    fun insertPhoto(imgUrl: String, block: (imgUrl: String) -> Drawable): PhotoBuilder {
        val drawableGet = DrawableGet(block)
        return InsertPhotoBuilder(imgUrl, drawableGet)
    }

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

    @Synchronized
    private fun getPhotoBuilder(imgUrl: String, drawableGet: DrawableGet): PhotoBuilder{
        photoBuilder?.let {
            return it
        }
        return InsertPhotoBuilder(imgUrl, drawableGet).apply {
            photoBuilder = this
        }

    }
    inner class InsertPhotoBuilder(
        private val imgUrl: String,
        private val drawableGet: DrawableGet
    ): PhotoBuilder{
        private var imageClick: ImageClick? = null
        private var imageDelete: ImageDelete? = null

        override fun setOnCLickListener(listener: ImageClick): PhotoBuilder {
            imageClick = listener
            return this
        }

        override fun setOnCLickListener(block: (view: View, imgUrl: String) -> Unit): PhotoBuilder {
            imageClick = ImageClick(block)
            return this
        }

        override fun setOnDeleteListener(listener: ImageDelete): PhotoBuilder {
            imageDelete = listener
            return this
        }

        override fun setOnDeleteListener(block: (view: View, imgUrl: String) -> Unit): PhotoBuilder {
            imageDelete = ImageDelete(block)
            return this
        }

        override fun apply(){
            richEditUtil.insertPhoto(imgUrl, drawableGet, imageClick, imageDelete)
           // richEditUtil.insertPhoto(this)
        }
    }

    inner class ParseHtmlBuilder(htmlText: String, imageGetter: Html.ImageGetter?): PhotoBuilder{
        private var customText: String = ""
        private val tagHandler = RichTextXTagHandler(true, imageGetter)

        init {
            needRefreshText = false

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
            needRefreshText = true
        }
    }

    constructor(context: Context): super(context){
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
    }
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr){

    }


}