package com.shine56.richtextx.util

import android.graphics.*
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.widget.EditText
import com.shine56.richtextx.api.ImageDelete
import com.shine56.richtextx.bean.Image
import com.shine56.richtextx.view.ClickableImageSpan
import kotlinx.coroutines.*

class RichEditUtil(private val editText: EditText) {

    var fontSize = 15
    var isBold = false

    /**
     * 设置图片栈，解决快速插入多张图片时顺序出错的问题
     */
//    private var i=0
//    private var isFree = true
//    private var isQuery = false
//    private val imageList = arrayListOf<RichEditText.InsertPhotoBuilder>()
//    fun insertPhoto(image: RichEditText.InsertPhotoBuilder){
//        imageList.add(image)
//        if (!isQuery){
//            query()
//        }
//    }
//    private fun query() {
//        isQuery = true
//        val job = Job()
//        val scope = CoroutineScope(job)
//        scope.launch(Dispatchers.IO) {
//            Log.d("调试", "-------------------------轮询开始")
//            while (imageList.isNotEmpty()){
//                //Log.d("调试", "***************轮询中")
//                if(isFree){
//                    isFree = false
//                    Log.d("调试", "查询到空闲")
//                    val image = imageList[0]
//                    imageList.removeAt(0)
//                    Log.d("调试", "图片地址${image.imgUrl}")
//                    insertPhoto(image.imgUrl, image.drawableGet, image.imageClick, image.imageDelete)
//                }
//            }
//            isQuery = false
//            Log.d("调试", "-------------------------轮询结束")
//            while (!isFree){
//               // Log.d("调试", "插入图片还没结束")
//            }
//            scope.cancel()
//            Log.d("调试", "协程域取消")
//
//        }
//    }

    /**
     * 使用协程管理插入图片的逻辑。将用户定义的获取drawable的逻辑方式IO线程，在主线程更新UI
     * @param image Image
     */
    fun insertPhoto(image: Image){

        if (image.drawableGet == null){
            return
        }

        val job = Job()
        val scope = CoroutineScope(job)

        //IO线程制作drawable
        val deferred = scope.async(Dispatchers.IO) {
            image.drawableGet.getDrawable(image.imageUrl)
        }

        scope.launch(Dispatchers.Main) {
            //获取drawable
            val mDrawable = deferred.await()
            val width: Int = mDrawable.intrinsicWidth
            val height: Int = mDrawable.intrinsicHeight
            mDrawable.setBounds(0, 0, if (width > 0) width else 0, if (height > 0) height else 0)

            //设置到edit
            val imageSpan = ClickableImageSpan(
                mDrawable,
                image.imageUrl
            )
            val spannableString = SpannableString(image.imageUrl)
            spannableString.setSpan(
                imageSpan,
                0,
                spannableString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            //editText.needRefreshText = false
            setText(spannableString)

            //点击事件，无需判空
            Log.d("调试", "insertPhoto: 设置点击事件")
            if(image.click == null){
                Log.d("调试", "insertPhoto: 点击事件为空")
            }
            imageSpan.setOnCLickListener(image.click)

            //删除事件，如果事件为空则不设置
            image.delete?.let {
                imageSpan.setOnDeleteListener(ImageDelete { view, url ->
                    //必须执行的逻辑
                    editText.text?.let {
                        val start = it.getSpanStart(imageSpan)
                        val end = it.getSpanEnd(imageSpan)
                        it.delete(start, end)
                    }
                    //用户定义的逻辑
                    it.onDelete(view, url)
                })
            }
            //editText.needRefreshText = true
            //isFree = true
            scope.cancel()
        }
    }

    /**
     * 为edit中某段文字设置字号和加粗span
     * @param start Int
     * @param end Int
     * @param before Int
     */
    fun setFontSizeAndBoldSpan(start: Int, end: Int, before: Int){
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