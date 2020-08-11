package com.shine56.richtextx

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.EditText
import kotlinx.coroutines.*

class RichText(private val editText: EditText) {
    private var fontSize = 15

    private var isInsertPhoto = false
    private var isBold = false

    private var onImageClick: ((view: View, imgUrl: String) -> Unit)? = null
    private var onImageDelete: ((view: View, imgUrl: String) -> Unit)? = null

    fun setOnImageCLickListener(listener: (view: View, imgUrl: String) -> Unit){
        onImageClick = listener
    }
    fun setOnImageDeleteListener(listener: (view: View, imgUrl: String) -> Unit){
        onImageDelete = listener
    }

    /**
     * edit插入图片
     * @param imagePath
     */
    fun insertPhoto(
        imagePath: String,
        getDrawable: (imagePath: String) -> Drawable
    ){
        isInsertPhoto = true
        //预处理：缩放， 圆角， 添加删除logo
        val job = Job()
        val scope = CoroutineScope(job)
        val deferred = scope.async(Dispatchers.IO) {
            getDrawable.invoke(imagePath)
        }
        //设置到edit
        scope.launch(Dispatchers.Main) {
            val mDrawable = deferred.await()
            val width: Int = mDrawable.intrinsicWidth
            val height: Int = mDrawable.intrinsicHeight
            mDrawable.setBounds(0, 0, if (width > 0) width else 0, if (height > 0) height else 0)

            val imageSpan = ClickableImageSpan(mDrawable, imagePath)
            val spannableString = SpannableString(imagePath)
            spannableString.setSpan(
                imageSpan,
                0,
                spannableString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setText(spannableString)

            //点击事件
            //editText.movementMethod = LinkMovementMethod.getInstance()
            imageSpan.setOnCLickListener(onImageClick)

            onImageDelete?.let { onDelete ->
                imageSpan.setOnDeleteListener {
                    val text = editText.text
                    val start = text.getSpanStart(imageSpan)
                    val end = text.getSpanEnd(imageSpan)
                    text.delete(start, end)
                    onDelete.invoke(it, imagePath)
                }
            }

            scope.cancel()

            isInsertPhoto = false
        }
    }

    /**
     * 字号
     */
    fun setFontSize(fontSize: Int){
        this.fontSize = fontSize
        //editText.textSize = fontSize
    }
    fun setFontSizeSpan(start: Int, end: Int, before: Int){
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

    /**
     * 加粗
     */
    fun setBold(boolean: Boolean){
        isBold = boolean
    }


    /**
     * 插入spananleString
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