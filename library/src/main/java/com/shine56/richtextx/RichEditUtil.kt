package com.shine56.richtextx

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.StyleSpan
import android.widget.EditText
import com.shine56.richtextx.api.DrawableGet
import com.shine56.richtextx.api.ImageClick
import com.shine56.richtextx.api.ImageDelete
import kotlinx.coroutines.*

class RichEditUtil(private val editText: EditText) {

    var fontSize = 15
    var isBold = false

    private var isInsertPhoto = false
    private var imageClick: ImageClick? = null
    private var imageDelete: ImageDelete? = null

    fun setOnImageCLickListener(listener: ImageClick){
        imageClick = listener
    }
    fun setOnImageDeleteListener(listener: ImageDelete){
        imageDelete = listener
    }

    /**
     * 使用协程管理插入图片的逻辑。将用户定义的获取drawable的逻辑方式IO线程，在主线程更新UI
     * @param imagePath String
     * @param getDrawable Function1<[@kotlin.ParameterName] String, Drawable>
     */
    fun insertPhoto(
        imageUrl: String,
        drawableGet: DrawableGet
    ){
        isInsertPhoto = true

        val job = Job()
        val scope = CoroutineScope(job)
        val deferred = scope.async(Dispatchers.IO) {
            drawableGet.getDrawable(imageUrl)
        }

        scope.launch(Dispatchers.Main) {

            //获取drawable
            val mDrawable = deferred.await()
            val width: Int = mDrawable.intrinsicWidth
            val height: Int = mDrawable.intrinsicHeight
            mDrawable.setBounds(0, 0, if (width > 0) width else 0, if (height > 0) height else 0)

            //设置到edit
            val imageSpan = ClickableImageSpan(mDrawable, imageUrl)
            val spannableString = SpannableString(imageUrl)
            spannableString.setSpan(
                imageSpan,
                0,
                spannableString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setText(spannableString)

            //点击事件
            imageSpan.setOnCLickListener(imageClick)

            //删除事件
            imageDelete?.let { imageDelete ->
                imageSpan.setOnDeleteListener(ImageDelete { view, url ->
                    //必须执行的逻辑
                    val text = editText.text
                    val start = text.getSpanStart(imageSpan)
                    val end = text.getSpanEnd(imageSpan)
                    text.delete(start, end)
                    //用户定义的逻辑
                    imageDelete.onDelete(view, url)
                })
            }

            scope.cancel()
            isInsertPhoto = false
        }
    }

    /**
     * 为edit中某段文字设置字号和加粗span
     * @param start Int
     * @param end Int
     * @param before Int
     */
    fun setFontSizeAndBoldSpan(start: Int, end: Int, before: Int){
        if(!isInsertPhoto){
            editText.text.let {
                if(before == 0){
                    val absoluteSizeSpan = AbsoluteSizeSpan(fontSize, true)
                    val spannableString = SpannableString(it)
                    spannableString.setSpan(
                        absoluteSizeSpan,
                        start,
                        end,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

                    if(isBold){
                        val styleSpan = StyleSpan(Typeface.BOLD)
                        spannableString.setSpan(
                            styleSpan,
                            start,
                            end,
                            Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                    }

                    editText.setText(spannableString)
                    editText.setSelection(end)
                }
            }
        }
    }

    fun indent(){
        setText("\u3000\u3000")
    }

    /**
     * 插入spannableString
     * @param spannableString SpannableString
     */
    private fun setText(spannableString: SpannableString){
        setText("\n")
        val index = editText.selectionStart //获取光标所在位置
        val editableText = editText.editableText
        if (index < 0 || index >= editableText.length) {
            editableText.append(spannableString)
        } else {
            editableText.insert(index, spannableString)
        }
        setText("\n")
    }

    /**
     * 插入string
     * @param string String
     */
    private fun setText(string: String){
        val index = editText.selectionStart //获取光标所在位置
        val editableText = editText.editableText
        if (index < 0 || index >= editableText.length) {
            editableText.append(string)
        } else {
            editableText.insert(index, string)
        }
    }
}