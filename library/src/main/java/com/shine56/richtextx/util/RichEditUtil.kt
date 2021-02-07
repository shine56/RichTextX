package com.shine56.richtextx.util

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.util.Log
import android.widget.EditText
import com.shine56.richtextx.api.ImageClick
import com.shine56.richtextx.api.ImageDelete
import com.shine56.richtextx.bean.Image
import com.shine56.richtextx.test.PrintTest
import com.shine56.richtextx.view.ClickableImageSpan
import kotlinx.coroutines.*


class RichEditUtil(private val editText: EditText) {

    var fontSize = 54
    var isBold = false

    private val inputType = editText.inputType

    /**
     * 设置图片栈，解决快速插入多张图片时顺序出错的问题
     */
    /* private var i=0
     private var isFree = true
     private var isQuery = false
     private val imageList = arrayListOf<RichEditText.InsertPhotoBuilder>()
     fun insertPhoto(image: RichEditText.InsertPhotoBuilder){
         imageList.add(image)
         if (!isQuery){
             query()
         }
     }
     private fun query() {
         isQuery = true
         val job = Job()
         val scope = CoroutineScope(job)
         scope.launch(Dispatchers.IO) {
             Log.d("调试", "-------------------------轮询开始")
             while (imageList.isNotEmpty()){
                 //Log.d("调试", "***************轮询中")
                 if(isFree){
                     isFree = false
                     Log.d("调试", "查询到空闲")
                     val image = imageList[0]
                     imageList.removeAt(0)
                     Log.d("调试", "图片地址${image.imgUrl}")
                     insertPhoto(image.imgUrl, image.drawableGet, image.imageClick, image.imageDelete)
                 }
             }
             isQuery = false
             Log.d("调试", "-------------------------轮询结束")
             while (!isFree){
                // Log.d("调试", "插入图片还没结束")
             }
             scope.cancel()
             Log.d("调试", "协程域取消")

         }
     }*/

    /**
     * 使用协程管理插入图片的逻辑。将用户定义的获取drawable的逻辑方式IO线程，在主线程更新UI
     * @param image Image
     */
    fun insertPhoto(image: Image){

        if (image.drawableGet == null){
            return
        }

        val scope = CoroutineUtil.getScope(editText.hashCode())

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
            setText("\n")
            setText(spannableString)
            setText("\n")

            //点击事件
            // imageSpan.setOnCLickListener(image.click)
            image.click?.let {
                imageSpan.setOnCLickListener(ImageClick { view, imgUrl ->
                    //必须执行的逻辑
                    editText.showSoftInputOnFocus = false
                    editText.isCursorVisible = false
//                    val index = editText.selectionStart
//                    editText.setSelection(0)
                    //用户定义的逻辑

                    it.onClick(view, imgUrl)
                })
            }


            //删除事件，如果事件为空则不设置
            image.delete?.let {
                imageSpan.setOnDeleteListener(ImageDelete { view, url ->
                    //必须执行的逻辑
                    editText.text?.let {
                        val start = it.getSpanStart(imageSpan)
                        val end = it.getSpanEnd(imageSpan)
                        it.delete(start, end)

                        editText.showSoftInputOnFocus = false
                    }
                    //用户定义的逻辑
                    it.onDelete(view, url)
                })
            }
            //editText.needRefreshText = true
            //isFree = true
        }
    }

    /**
     * 为edit中某段文字设置字号和加粗span
     * @param start Int
     * @param end Int
     * @param before Int 被替换文的长度 =0表示新增，
     */
    fun setFontSizeAndBoldSpan(start: Int, end: Int, before: Int){
        editText.text.let {
            //Log.d(TAG, "setFontSizeAndBoldSpan: before=$before start=$start end=$end")

            if(before == 0 && start != end){
                Log.d(PrintTest.TAG, "setFontSizeAndBoldSpan: 设置字号与加粗")
                //字号
                val absoluteSizeSpan = AbsoluteSizeSpan(fontSize, false)
                val spannableString = SpannableString(it)
                spannableString.setSpan(
                    absoluteSizeSpan,
                    start,
                    end,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

                //加粗
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

    /**
     * 为光标所在行设置删除线，如果已经存在则去除。返回转换状态，true表示设置，false表示去除
     * @return Boolean
     */
    fun switchDeleteLineOnThisLine(): Boolean{
        val line = getThisLineInformation()
        val spans = editText.text.getSpans(line[1], line[2], StrikethroughSpan::class.java)

        PrintTest.printLineInformation(line)

        //spannableString实际是edit全部text
        if(spans.isEmpty()){
            val spannableString = SpannableString(editText.text)
            setDeleteLine( spannableString, line[1], line[2])
            editText.setText(spannableString)
            //移动光标
            editText.setSelection(line[3])
            return true
        }else{
            removeSpan(spans)
            return false
        }
    }

    /**
     * 为光标所在行设置颜色，如果已经存在则去除。返回转换状态，true表示设置，false表示去除
     * @param color Int
     * @return Boolean
     */
    fun switchTextColorOnThisLine(color: Int): Boolean{
        val line = getThisLineInformation()
        val spans = editText.text.getSpans(line[1], line[2], ForegroundColorSpan::class.java)

        if(spans.isEmpty()){
            val spannableString = SpannableString(editText.text)
            setTextColor(spannableString, color, line[1], line[2])
            editText.setText(spannableString)
            //移动光标
            editText.setSelection(line[3])
            return true
        }else{
            removeSpan(spans)
            return false
        }
    }

    /**
     * 设置删除线
     */
    fun setDeleteLine(spannableString: SpannableString, start: Int, end: Int){
        val strikethroughSpan = StrikethroughSpan()
        setSpan(spannableString, strikethroughSpan, start, end)
    }
    fun removeDeleteLine(start: Int, end: Int){
        val spans = editText.text.getSpans(start, end, StrikethroughSpan::class.java)
        removeSpan(spans)
    }

    /**
     * 设置文本颜色
     * @param color Int
     */
    fun setTextColor(spannableString: SpannableString, color : Int, start: Int, end: Int){
        val colorSpan = ForegroundColorSpan(color)
        setSpan(spannableString, colorSpan, start, end)
    }
    fun removeTextColor(start: Int, end: Int){
        val spans = editText.text.getSpans(start, end, ForegroundColorSpan::class.java)
        removeSpan(spans)
    }

    /**
     * 缩进
     */
    fun indent(){
        setText("\u3000\u3000")
    }

    /**
     * 获取光标所在行信息
     * @return Array<Int> 0所在行数， 1行起始位置， 2行结束位置, 3光标位置
     */
    fun getThisLineInformation(): IntArray{
        val index = editText.selectionStart
        val lineNum = editText.layout.getLineForOffset(index)
        val start = editText.layout.getLineStart(lineNum)
        val end = editText.layout.getLineEnd(lineNum)

        return intArrayOf(lineNum, start, end, index)
    }

    /**
     * 设置Span
     * @param what Any Span类
     * @param spannableString SpannableString
     */
    private fun setSpan(spannableString: SpannableString, what: Any, start: Int, end: Int){
        spannableString.setSpan(
            what,
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    private fun <T> removeSpan(spans: Array<T>){
        for (span in spans){
            editText.text.removeSpan(span)
        }
    }

    /**
     * 插入spannableString
     * @param spannableString SpannableString
     */
    private fun setText(spannableString: SpannableString){
        val index = editText.selectionStart //获取光标所在位置
        val editableText = editText.editableText
        if (index < 0 || index >= editableText.length) {
            editableText.append(spannableString)
        } else {
            editableText.insert(index, spannableString)
        }
    }
    private fun setText(spannableString: SpannableString, index: Int){
        val editableText = editText.editableText
        editableText.insert(index, spannableString)
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